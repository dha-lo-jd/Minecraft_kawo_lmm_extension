package org.lo.d.minecraft.littlemaid.mode;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.LMM_ContainerInventory;
import net.minecraft.src.LMM_EntityLittleMaid;
import net.minecraft.src.LMM_EntityModeBase;
import net.minecraft.src.LMM_GuiInventory;
import net.minecraft.src.ModLoader;

public abstract class LMM_EntityModeBaseEx extends LMM_EntityModeBase implements LMMModeExGuiHandler,
		LMMModeExAIHandler, LMMModeExInteractHandler, LMMModeExHandleHealthUpdateHandler {

	public LMM_EntityModeBaseEx(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public float getAIMoveSpeed(LMM_EntityLittleMaid maid, int maidMode) {
		return -1;
	}

	@Override
	public LMM_ContainerInventory getContainerInventory(EntityPlayer player, LMM_EntityLittleMaid maid, int maidMode) {
		return null;
	}

	@Override
	public LMM_GuiInventory getOpenGuiInventory(EntityPlayer var1, LMM_EntityLittleMaid maid, int maidMode) {
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
	public void updateTask(LMM_EntityLittleMaid maid, int maidMode) {
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
