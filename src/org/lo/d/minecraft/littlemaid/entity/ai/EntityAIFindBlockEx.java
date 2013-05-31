package org.lo.d.minecraft.littlemaid.entity.ai;

import net.minecraft.src.LMM_EntityAIFindBlock;
import net.minecraft.src.LMM_EntityLittleMaid;

import org.lo.d.minecraft.littlemaid.mode.LMMModeExAIHandler;
import org.lo.d.minecraft.littlemaid.mode.LMMModeExHandler.TaskState;

public class EntityAIFindBlockEx extends LMM_EntityAIFindBlock {
	public EntityAIFindBlockEx(LMM_EntityLittleMaid pEntityLittleMaid) {
		super(pEntityLittleMaid);
	}

	@Override
	public boolean continueExecuting() {
		if (llmode instanceof LMMModeExAIHandler) {
			if (((LMMModeExAIHandler) llmode).onValidateTask(theMaid, theMaid.getMaidModeInt()) == TaskState.BREAK) {
				return false;
			}
		}
		return super.continueExecuting();
	}

	@Override
	public boolean shouldExecute() {
		if (llmode instanceof LMMModeExAIHandler) {
			if (((LMMModeExAIHandler) llmode).onValidateTask(theMaid, theMaid.getMaidModeInt()) == TaskState.BREAK) {
				return false;
			}
		}
		return super.shouldExecute();
	}

	@Override
	public void updateTask() {
		if (llmode instanceof LMMModeExAIHandler) {
			((LMMModeExAIHandler) llmode).updateTask(theMaid, theMaid.getMaidModeInt());
		}
		super.updateTask();
	}

}
