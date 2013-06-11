package org.lo.d.minecraft.littlemaid;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import net.minecraft.src.LMM_EntityLittleMaid;
import net.minecraft.src.LMM_EntityModeBase;
import net.minecraft.src.LMM_EntityMode_AcceptBookCommand;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

import org.lo.d.commons.configuration.ConfigurationSupport;
import org.lo.d.commons.configuration.ConfigurationSupport.BooleanConfig;
import org.lo.d.commons.configuration.ConfigurationSupport.ConfigurationMod;
import org.lo.d.commons.configuration.ConfigurationSupport.IntConfig;
import org.lo.d.commons.reflections.ReflectionSupport;
import org.lo.d.commons.reflections.ReflectionSupport.Worker;
import org.lo.d.minecraft.littlemaid.LMMExtension.ModeExWorker.State;
import org.lo.d.minecraft.littlemaid.entity.BaseEntityLittleMaidEx;
import org.lo.d.minecraft.littlemaid.entity.EntityLittleMaidEx;
import org.lo.d.minecraft.littlemaid.mode.LMMModeExHandler;

import com.google.common.collect.Sets;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;

@Mod(modid = "LMMExtension", name = "Kawo_LMM_Extension", version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { "", })
@ConfigurationMod
public class LMMExtension {

	public interface ModeExWorker<MH extends org.lo.d.minecraft.littlemaid.mode.LMMModeExHandler, T> {
		enum State {
			CONTINUE, BREAK;
		}

		public static abstract class VoidWorker<MH extends org.lo.d.minecraft.littlemaid.mode.LMMModeExHandler>
				implements ModeExWorker<MH, Void> {
			@Override
			public Void getResult() {
				return null;
			}
		}

		public T getResult();

		public State work(LMM_EntityLittleMaid maid, MH modeEx);
	}

	public static final LMMExMaidChangeManager maidChangeManager = new LMMExMaidChangeManager();

	@Instance("LMMExtension")
	public static LMMExtension instance;

	private static final EntityLittleMaidSpawnEventHandler MAID_SPAWN_EVENT_HANDLER = new EntityLittleMaidSpawnEventHandler();

	@IntConfig(defaultValue = 0, name = "guiId")
	public static int guiId;

	@BooleanConfig(defaultValue = true, name = "enablePresetMaidModeCommands", comment = "書ける本での指示で、デフォルトで用意された各モードへの変更を可能にします")
	public static boolean enablePresetMaidModeCommands;

	@BooleanConfig(defaultValue = true, name = "enableUninstallMode", comment = "アンインストール処理(modを消す際にtrueにして一度ワールドを読み込んでから消さないとメイドさんが全て消えます)")
	public static boolean enableUninstallMode;

	private static final Set<Worker> classWorkers = Sets.newHashSet();

	private static final LittleMaidModeIdResolver LITTLE_MAID_MODE_ID_RESOLVER = new LittleMaidModeIdResolver();

	public static LMM_EntityLittleMaid changeMaidToEx(LMM_EntityLittleMaid maid) {
		return maidChangeManager.changeAndSpawnMaidToEx(maid);
	}

	public static <MH extends LMMModeExHandler, T> T delegateModeExs(Class<MH> handlerType, LMM_EntityLittleMaid maid,
			ModeExWorker<MH, T> worker) {
		if (!(maid instanceof EntityLittleMaidEx)) {
			return null;
		}
		for (LMM_EntityModeBase mode : maid.maidEntityModeList) {
			if (!handlerType.isAssignableFrom(mode.getClass())) {
				continue;
			}
			@SuppressWarnings("unchecked")
			MH modeEx = (MH) mode;
			if (worker.work(maid, modeEx) == State.BREAK) {
				break;
			}
		}

		return worker.getResult();

	}

	public static <T extends LMM_EntityModeBase> T getMaidModeInstance(Class<T> modeType, LMM_EntityLittleMaid maid) {
		for (LMM_EntityModeBase modeBase : maid.maidEntityModeList) {
			if (modeType.isAssignableFrom(modeBase.getClass())) {
				@SuppressWarnings("unchecked")
				T m = (T) modeBase;
				return m;
			}
		}
		return null;
	}

	public static void registClassWorkers(Worker worker) {
		classWorkers.add(worker);
	}

	private Configuration config;

	@Mod.Init
	public void init(FMLInitializationEvent event) {
		ReflectionSupport.getClasses("", classWorkers);

		int entityId = EntityRegistry.findGlobalUniqueEntityId();
		EntityRegistry.registerModEntity(BaseEntityLittleMaidEx.class, "EntityLittleMaidEx", entityId, this, 80, 3,
				true);
		NetworkRegistry.instance().registerGuiHandler(this, new LMMExGuiHandler());
		MinecraftForge.EVENT_BUS.register(MAID_SPAWN_EVENT_HANDLER);
	}

	@Mod.PostInit
	public void postInit(FMLPostInitializationEvent event) {
		LITTLE_MAID_MODE_ID_RESOLVER.postInit(config);
		if (config != null) {
			config.save();
		}
	}

	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		ConfigurationSupport.load(getClass(), event, config);
		if (enableUninstallMode) {
			maidChangeManager.initializeForUninstall(config);
		} else {
			maidChangeManager.initialize(config);
		}
		LITTLE_MAID_MODE_ID_RESOLVER.preInit(event, config);
		if (enablePresetMaidModeCommands) {
			LMM_EntityMode_AcceptBookCommand.setupDefaultModeCommands();
		}
	}
}
