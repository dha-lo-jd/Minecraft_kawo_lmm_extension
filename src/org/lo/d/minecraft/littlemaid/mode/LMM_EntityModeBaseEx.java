package org.lo.d.minecraft.littlemaid.mode;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.LMM_EntityLittleMaid;
import net.minecraft.src.LMM_EntityModeBase;
import net.minecraft.src.ModLoader;

import org.lo.d.minecraft.littlemaid.MaidExIcon;

import com.google.common.collect.Lists;

public abstract class LMM_EntityModeBaseEx extends LMM_EntityModeBase implements LMMModeExGuiHandler,
		LMMModeExAIHandler, LMMModeExInteractHandler, LMMModeExHandleHealthUpdateHandler, LMMModeExNBTHandler,
		LMMModeExIconHandler {

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
	public List<MaidExIcon> getIcons(int maidMode) {
		return Lists.newArrayList();
	}

	@Override
	public GuiContainer getOpenGuiInventory(final int guiId, EntityPlayer var1, LMM_EntityLittleMaid maid, int maidMode) {
		return null;
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

	protected void addLocalization(String keyPrefix, String prefix, String modeName) {
		String name = prefix + modeName;
		String key = new StringBuilder(keyPrefix).append(".").append(name).toString();
		ModLoader.addLocalization(key, name);
	}
}
