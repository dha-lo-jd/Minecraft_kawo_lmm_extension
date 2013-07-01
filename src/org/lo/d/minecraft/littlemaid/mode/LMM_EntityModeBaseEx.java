package org.lo.d.minecraft.littlemaid.mode;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.LMM_EntityLittleMaid;
import net.minecraft.src.LMM_EntityModeBase;

import org.lo.d.minecraft.littlemaid.MaidExIcon;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.registry.LanguageRegistry;

public abstract class LMM_EntityModeBaseEx extends LMM_EntityModeBase implements LMMModeExGuiHandler,
		LMMModeExAIHandler, LMMModeExInteractHandler, LMMModeExHandleHealthUpdateHandler, LMMModeExNBTHandler,
		LMMModeExIconHandler, LMMModeExSugarCountHandler {

	public static abstract class JPNameProvider {
		public abstract String getLocalization();

		public String getLocalizationFreedom() {
			return getLocalization();
		}

		public String getLocalizationTracer() {
			return getLocalization();
		}
	}

	public LMM_EntityModeBaseEx(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public float getAIMoveSpeed(LMM_EntityLittleMaid maid, int maidMode) {
		return -1;
	}

	@Override
	public Container getContainerInventory(final int guiId, EntityPlayer player, LMM_EntityLittleMaid maid, int maidMode) {
		return null;
	}

	@Override
	public float getIconHeight(int maidMode) {
		return 0;
	}

	@Override
	public List<MaidExIcon> getIcons(int maidMode) {
		return Lists.newArrayList();
	}

	@Override
	public GuiContainer getOpenGuiInventory(final int guiId, EntityPlayer var1, LMM_EntityLittleMaid maid, int maidMode) {
		return null;
	}

	@Override
	public int getSugarAmount() {
		return 0;
	}

	@Override
	public LMMModeExHandleHealthUpdateHandler.TaskState handleHealthUpdate(LMM_EntityLittleMaid maid, int maidMode,
			byte par1) {
		return LMMModeExHandleHealthUpdateHandler.TaskState.CONTINUE;
	}

	@Override
	public LMMModeExAIHandler.TaskState onValidateTask(LMM_EntityLittleMaid maid, int maidMode) {
		return LMMModeExAIHandler.TaskState.CONTINUE;
	}

	@Override
	public boolean overInteract(EntityPlayer par1EntityPlayer) {
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1nbtTagCompound) {
	}

	@Override
	public void updateTask(LMM_EntityLittleMaid maid, int maidMode) {
	}

	@Override
	public void writeToNBT(NBTTagCompound par1nbtTagCompound) {
	}

	protected void addLocalization(String modeName) {
		addLocalization(modeName, true, true);
	}

	protected void addLocalization(String modeName, boolean addFreedom, boolean addTracer) {
		String prefix = "littleMaidMob.mode";
		addLocalization(prefix, "", modeName);
		if (addFreedom) {
			addLocalization(prefix, "F-", modeName);
		}
		if (addTracer) {
			addLocalization(prefix, "T-", modeName);
		}
	}

	protected void addLocalization(String modeName, JPNameProvider nameProvider) {
		addLocalization(modeName);

		String prefix = "littleMaidMob.mode";
		addLocalizationJp(prefix, "", modeName, nameProvider.getLocalization());
		addLocalizationJp(prefix, "F-", modeName, nameProvider.getLocalizationFreedom());
		addLocalizationJp(prefix, "T-", modeName, nameProvider.getLocalizationTracer());
	}

	protected void addLocalization(String keyPrefix, String prefix, String modeName) {
		String name = prefix + modeName;
		String key = new StringBuilder(keyPrefix).append(".").append(name).toString();
		LanguageRegistry.instance().addStringLocalization(key, name);
	}

	protected void addLocalizationJp(String keyPrefix, String prefix, String modeName, String jpName) {
		String name = prefix + modeName;
		String key = new StringBuilder(keyPrefix).append(".").append(name).toString();
		LanguageRegistry.instance().addStringLocalization(key, "ja_JP", jpName);
	}
}
