package org.lo.d.minecraft.littlemaid;

import java.lang.reflect.InvocationTargetException;

import net.minecraft.src.LMM_EntityLittleMaid;
import net.minecraft.src.LMM_EntityModeBase;

import org.lo.d.commons.configuration.ConfigurationSupport;
import org.lo.d.commons.configuration.ConfigurationSupport.IntConfig;
import org.lo.d.minecraft.littlemaid.LMMExtension.ModeExWorker.State;
import org.lo.d.minecraft.littlemaid.entity.BaseEntityLittleMaidEx;
import org.lo.d.minecraft.littlemaid.entity.EntityLittleMaidEx;
import org.lo.d.minecraft.littlemaid.mode.LMMModeExHandler;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.server.FMLServerHandler;

@Mod(modid = "LMMExtension", name = "Kawo_LMM_Extension", version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {
		"",
})
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

	@IntConfig(defaultValue = 0, name = "guiId")
	public static int guiId;

	public static LMM_EntityLittleMaid changeMaidToEx(LMM_EntityLittleMaid maid) {
		return maidChangeManager.changeMaidToEx(maid);
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

	@Mod.Init
	public void init(FMLInitializationEvent event) {
		FMLServerHandler.instance();
		int entityId = EntityRegistry.findGlobalUniqueEntityId();
		EntityRegistry.registerModEntity(BaseEntityLittleMaidEx.class, "EntityLittleMaidEx", entityId, this, 80, 3,
				true);
		EntityRegistry.registerGlobalEntityID(BaseEntityLittleMaidEx.class, "EntityLittleMaidEx", entityId);
		NetworkRegistry.instance().registerGuiHandler(this, new LMMExGuiHandler());
	}

	@Mod.PostInit
	public void postInit(FMLPostInitializationEvent event) {
		maidChangeManager.initialize();
	}

	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		ConfigurationSupport.load(getClass(), event);
	}
}
