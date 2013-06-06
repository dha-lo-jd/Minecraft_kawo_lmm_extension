package org.lo.d.minecraft.littlemaid.entity;

import net.minecraft.src.LMM_EntityLittleMaid;

import org.lo.d.minecraft.littlemaid.LMMExtension.ModeExWorker;
import org.lo.d.minecraft.littlemaid.mode.LMMModeExAIHandler;

public class ModeExAIMoveSpeedWorker implements ModeExWorker<LMMModeExAIHandler, Float> {
	private Float speed;

	@Override
	public Float getResult() {
		return speed;
	}

	@Override
	public ModeExWorker.State work(LMM_EntityLittleMaid maid,
			LMMModeExAIHandler modeEx) {
		speed = modeEx.getAIMoveSpeed(maid, maid.getMaidModeInt());
		if (speed < 0) {
			return State.CONTINUE;
		}
		return State.BREAK;
	}
}