package org.lo.d.minecraft.littlemaid.mode;

import net.minecraft.src.LMM_EntityLittleMaid;

public interface LMMModeExAIHandler extends LMMModeExHandler {
	public enum TaskState {
		CONTINUE, BREAK;
	}

	public float getAIMoveSpeed(LMM_EntityLittleMaid maid, int maidMode);

	public TaskState onValidateTask(LMM_EntityLittleMaid maid, int maidMode);

	public void updateTask(LMM_EntityLittleMaid maid, int maidMode);
}
