package org.lo.d.minecraft.littlemaid;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Set;

import net.minecraftforge.common.Configuration;

import org.lo.d.minecraft.littlemaid.LittleMaidModeConfiguration.ResolveModeId;
import org.lo.d.minecraft.littlemaid.entity.EntityLittleMaidModeIdResolverDummy;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class LittleMaidModeIdResolver {
	public static class ConfigurableMode extends Mode {
		private final int defaultValue;
		private final String key;

		public ConfigurableMode(int modeIndex, String modeName, String key, int defaultValue) {
			super(modeIndex, modeName);
			this.key = key;
			this.defaultValue = defaultValue;
		}

		@Override
		protected void doGet(Configuration config, String comment) {
			config.get(CONFIG_CATEGORY, key, defaultValue, comment);
		}

		@Override
		protected String getNormalComment() {
			return "defaultValue = " + defaultValue;
		}
	}

	public static abstract class Mode {
		private final int modeIndex;
		private final String modeName;

		public Mode(int modeIndex, String modeName) {
			this.modeIndex = modeIndex;
			this.modeName = modeName;
		}

		public void getConflictedConfigValue(Configuration config, Collection<Mode> conflictModes) {
			StringBuilder sb = new StringBuilder(getNormalComment());
			sb.append(" ");
			sb.append("!Confilcts! [");
			String sep = "";
			for (Mode mode : conflictModes) {
				sb.append(sep);
				sb.append(mode.getModeName());
				sep = ", ";
			}
			sb.append("]");
			String comment = sb.toString();
			doGet(config, comment);
		}

		public int getModeIndex() {
			return modeIndex;
		}

		public String getModeName() {
			return modeName;
		}

		protected abstract void doGet(Configuration config, String comment);

		protected abstract String getNormalComment();
	}

	private static class ConfiguratedIdInjector extends AbstractConfiguratedIdInjector<ResolveModeId> {
		private final LittleMaidModeIdResolver idResolver;

		protected ConfiguratedIdInjector(LittleMaidModeIdResolver littleMaidModeIdResolver, Configuration config) {
			super(CONFIG_CATEGORY, CONFIG_KEY_PREFIX, config, LittleMaidModeConfiguration.class, ResolveModeId.class);
			idResolver = littleMaidModeIdResolver;
		}

		@Override
		protected String getKeyName(ResolveModeId annotation) {
			return annotation.modeName();
		}

		@Override
		protected void workToValue(ResolveModeId annotation, String key, int value, int defaultValue, Field field)
				throws IllegalArgumentException, IllegalAccessException {
			super.workToValue(annotation, key, value, defaultValue, field);
			idResolver.putMode(key, new ConfigurableMode(value, annotation.modeName(), key, defaultValue));
		}

	}

	public static final String CONFIG_CATEGORY = "id_resolver";

	public static final String CONFIG_KEY_PREFIX = "resolver.littlemaid.mode.id";

	protected final Multimap<Integer, Mode> modes = HashMultimap.create();
	protected final Set<String> loadedKeys = Sets.newHashSet();

	public boolean isLoaded(String key) {
		return loadedKeys.contains(key);
	}

	public void postInit(Configuration config) {
		new EntityLittleMaidModeIdResolverDummy(this, config).postInit();

		for (Integer key : modes.keySet()) {
			Collection<Mode> ms = modes.get(key);
			if (ms.size() > 1) {
				for (Mode mode : ms) {
					mode.getConflictedConfigValue(config, ms);
				}
			}
		}
	}

	public void preInit(FMLPreInitializationEvent event, Configuration config) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		ConfiguratedIdInjector worker = new ConfiguratedIdInjector(this, config);
		LMMExtension.registClassWorkers(worker);
	}

	public boolean putMode(String key, Mode value) {
		loadedKeys.add(key);
		return modes.put(value.getModeIndex(), value);
	}
}
