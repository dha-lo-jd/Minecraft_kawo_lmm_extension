package org.lo.d.minecraft.littlemaid.entity;

import java.util.Collection;
import java.util.List;

import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.src.LMM_EntityLittleMaid;
import net.minecraft.src.MMM_TextureBox;
import net.minecraftforge.common.Configuration;

import org.lo.d.minecraft.littlemaid.LittleMaidModeIdResolver;
import org.lo.d.minecraft.littlemaid.LittleMaidModeIdResolver.Mode;

import com.google.common.collect.Lists;

@AntiEntityLittleMaidEx
public class EntityLittleMaidModeIdResolverDummy extends LMM_EntityLittleMaid {
	public static class FixedMode extends Mode {
		private final String key;

		public FixedMode(int modeIndex, String modeName, String key) {
			super(modeIndex, modeName);
			this.key = key;
		}

		protected void doGet(Configuration config,String comment) {
			config.get(LittleMaidModeIdResolver.CONFIG_CATEGORY, key, DEFAULT_VALUE, comment);
		}

		protected String getNormalComment() {
			return "id = " + getModeIndex();
		}

		public String getKey() {
			return key;
		}
	}

	private static final String DEFAULT_VALUE = "locked";

	private final Configuration config;

	private List<FixedMode> modes;

	private final LittleMaidModeIdResolver idResolver;

	public EntityLittleMaidModeIdResolverDummy(LittleMaidModeIdResolver littleMaidModeIdResolver, Configuration config) {
		super(null);
		this.config = config;
		idResolver = littleMaidModeIdResolver;
	}

	@Override
	public void addMaidMode(EntityAITasks[] peaiTasks, String pmodeName, int pmodeIndex) {
		if (modes == null) {
			modes = Lists.newArrayList();
		}
		modes.add(new FixedMode(pmodeIndex, pmodeName, LittleMaidModeIdResolver.CONFIG_KEY_PREFIX + "." + pmodeName));
	}

	public void postInit() {
		for (FixedMode mode : modes) {

			String key = mode.getKey();
			if (idResolver.isLoaded(key)) {
				continue;
			}

			config.get(LittleMaidModeIdResolver.CONFIG_CATEGORY, key, DEFAULT_VALUE, "id = " + mode.getModeIndex());
			idResolver.putMode(key, mode);
		}
	}

	@Override
	public void setTexturePackName(MMM_TextureBox[] pTextureBox) {
	}
}