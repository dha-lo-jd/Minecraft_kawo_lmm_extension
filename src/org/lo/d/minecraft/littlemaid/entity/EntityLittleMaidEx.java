package org.lo.d.minecraft.littlemaid.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.LMM_EntityLittleMaid;
import net.minecraft.world.World;

import org.lo.d.minecraft.littlemaid.LMMExtension;
import org.lo.d.minecraft.littlemaid.LMMExtension.ModeExWorker;
import org.lo.d.minecraft.littlemaid.mode.LMMModeExAIHandler;

public class EntityLittleMaidEx extends LMM_EntityLittleMaid {

	int p = 0;

	public EntityLittleMaidEx(LMM_EntityLittleMaid maid) {
		super(maid.worldObj);
		copyDataFrom(maid, true);
	}

	public EntityLittleMaidEx(World par1World) {
		super(par1World);
	}

	@Override
	public void displayGUIMaidInventory(EntityPlayer pEntityPlayer) {
		if (!worldObj.isRemote) {
			// server
			pEntityPlayer.openGui(LMMExtension.instance, LMMExtension.guiId, pEntityPlayer.worldObj, entityId, 0, 0);
		}
	}

	@Override
	public float getAIMoveSpeed() {
		LMMExtension.delegateModeExs(LMMModeExAIHandler.class, this, new ModeExWorker<LMMModeExAIHandler, Float>() {
			Float speed;

			@Override
			public Float getResult() {
				return speed;
			}

			@Override
			public ModeExWorker.State work(EntityLittleMaidEx maid,
					LMMModeExAIHandler modeEx) {
				speed = modeEx.getAIMoveSpeed(maid, maid.getMaidModeInt());
				return State.CONTINUE;
			}
		});
		return super.getAIMoveSpeed();
	}

	@Override
	public boolean interact(EntityPlayer par1EntityPlayer) {
		// TODO 自動生成されたメソッド・スタブ
		return super.interact(par1EntityPlayer);
	}
}
