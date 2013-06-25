package org.lo.d.minecraft.littlemaid;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import net.minecraft.src.LMM_EntityLittleMaid;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

import org.lo.d.minecraft.littlemaid.entity.BaseEntityLittleMaidEx;
import org.lo.d.minecraft.littlemaid.entity.EntityLittleMaidEx;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class LMMExMaidChangeManager {

	public interface MaidChanger {
		LMM_EntityLittleMaid change(LMM_EntityLittleMaid maid);

		int validate(LMM_EntityLittleMaid maid);
	}

	private static class DefaultMaidChanger implements MaidChanger {
		@Override
		public LMM_EntityLittleMaid change(LMM_EntityLittleMaid maid) {
			return new BaseEntityLittleMaidEx(maid);
		}

		@Override
		public int validate(LMM_EntityLittleMaid maid) {
			return LMM_EntityLittleMaid.class.equals(maid.getClass()) ? 1 : 0;
		}
	}

	private static class UninstallMaidChanger implements MaidChanger {
		@Override
		public LMM_EntityLittleMaid change(LMM_EntityLittleMaid maid) {
			Class<?> maidClass = maid.getClass();
			while (EntityLittleMaidEx.class.isAssignableFrom(maidClass)) {
				maidClass = maidClass.getSuperclass();
			}

			Constructor<?> constructor = null;
			while (constructor == null && LMM_EntityLittleMaid.class.isAssignableFrom(maidClass)) {
				try {
					constructor = maidClass.getConstructor(World.class);
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}

			if (constructor == null) {
				return maid;//なにかおかしいが、とりかえしもつかなさそう
			}

			LMM_EntityLittleMaid newMaid;
			try {
				newMaid = (LMM_EntityLittleMaid) constructor.newInstance(maid.worldObj);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
				return maid;
			}
			newMaid.copyDataFrom(maid, true);
			System.out.println("change uninstall: " + newMaid);
			return newMaid;
		}

		@Override
		public int validate(LMM_EntityLittleMaid maid) {
			return EntityLittleMaidEx.class.isAssignableFrom(maid.getClass()) ? 1 : 0;
		}
	}

	public static void main(String[] args) {
		new LMMExMaidChangeManager().initialize(null);
	}

	private Set<MaidChanger> registedChangers = Sets.newHashSet();

	private Map<Class<?>, MaidChanger> cachedChangers = Maps.newHashMap();

	public LMM_EntityLittleMaid changeAndSpawnMaidToEx(LMM_EntityLittleMaid maid) {
		if (!maid.worldObj.isRemote) {
			LMM_EntityLittleMaid littleMaidEx;
			MaidChanger changer = getPrimaryChanger(maid);
			if (changer != null) {
				littleMaidEx = changer.change(maid);
				littleMaidEx.setMaidWait(false);
				littleMaidEx.rotationYawHead = maid.rotationYawHead;
				maid.worldObj.spawnEntityInWorld(littleMaidEx);
				littleMaidEx.getNextEquipItem();
				maid = littleMaidEx;
			}
		}
		return maid;
	}

	public LMM_EntityLittleMaid changeMaidToEx(LMM_EntityLittleMaid maid) {
		LMM_EntityLittleMaid littleMaidEx;
		MaidChanger changer = getPrimaryChanger(maid);
		if (changer != null) {
			littleMaidEx = changer.change(maid);
			littleMaidEx.rotationYawHead = maid.rotationYawHead;
			maid = littleMaidEx;
		}
		return maid;
	}

	public void initialize(Configuration config) {
		registMaidChanger(new DefaultMaidChanger());
		LMMExtension.registClassWorkers(new AnnotatedMaidChangerProvidor(this));
		LMMExtension.registClassWorkers(new GeneratedMaidEntityClassProvidor(config));
	}

	public void initializeForUninstall(Configuration config) {
		registMaidChanger(new UninstallMaidChanger());
		LMMExtension.registClassWorkers(new GeneratedMaidEntityClassProvidor(config));
	}

	public boolean registMaidChanger(MaidChanger maidChanger) {
		return registedChangers.add(maidChanger);
	}

	private MaidChanger doGetPrimaryChanger(LMM_EntityLittleMaid maid) {
		int maxPriority = 0;
		MaidChanger primaryChanger = null;
		for (MaidChanger changer : registedChangers) {
			int priority = changer.validate(maid);
			if (maxPriority < priority) {
				maxPriority = priority;
				primaryChanger = changer;
			}
		}
		return primaryChanger;

	}

	private MaidChanger getPrimaryChanger(LMM_EntityLittleMaid maid) {
		Class<?> maidClass = maid.getClass();
		if (!cachedChangers.containsKey(maidClass)) {
			MaidChanger changer = doGetPrimaryChanger(maid);
			cachedChangers.put(maidClass, changer);
		}
		MaidChanger changer = cachedChangers.get(maidClass);

		return changer;

	}

}
