package org.lo.d.minecraft.littlemaid.mode;

import net.minecraft.src.LMM_EntityLittleMaid;

public interface LMMModeExHandleHealthUpdateHandler extends LMMModeExHandler {
	public TaskState handleHealthUpdate(LMM_EntityLittleMaid maid, int maidMode, byte par1);
}
