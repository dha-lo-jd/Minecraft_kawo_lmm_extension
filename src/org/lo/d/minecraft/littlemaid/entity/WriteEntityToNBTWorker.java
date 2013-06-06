package org.lo.d.minecraft.littlemaid.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.LMM_EntityLittleMaid;

import org.lo.d.minecraft.littlemaid.LMMExtension.ModeExWorker;
import org.lo.d.minecraft.littlemaid.mode.LMMModeExNBTHandler;

public class WriteEntityToNBTWorker extends ModeExWorker.VoidWorker<LMMModeExNBTHandler> {
	private final NBTTagCompound finalTagCompound;

	public WriteEntityToNBTWorker(NBTTagCompound finalTagCompound) {
		this.finalTagCompound = finalTagCompound;
	}

	@Override
	public ModeExWorker.State work(LMM_EntityLittleMaid maid,
			LMMModeExNBTHandler modeEx) {
		modeEx.writeToNBT(finalTagCompound);
		return State.CONTINUE;
	}
}