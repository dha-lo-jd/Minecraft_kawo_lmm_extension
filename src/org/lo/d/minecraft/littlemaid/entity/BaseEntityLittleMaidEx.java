package org.lo.d.minecraft.littlemaid.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.src.LMM_EntityLittleMaid;
import net.minecraft.src.LMM_EntityModeBase;
import net.minecraft.world.World;

import org.lo.d.minecraft.littlemaid.LMMExtension;
import org.lo.d.minecraft.littlemaid.mode.LMMModeExAIHandler;
import org.lo.d.minecraft.littlemaid.mode.LMMModeExHandleHealthUpdateHandler;
import org.lo.d.minecraft.littlemaid.mode.LMMModeExHandler.TaskState;
import org.lo.d.minecraft.littlemaid.mode.LMMModeExInteractHandler;
import org.lo.d.minecraft.littlemaid.mode.LMMModeExNBTHandler;

public class BaseEntityLittleMaidEx extends LMM_EntityLittleMaid implements EntityLittleMaidEx {

	public BaseEntityLittleMaidEx(LMM_EntityLittleMaid maid) {
		super(maid.worldObj);
		copyDataFrom(maid, true);
	}

	public BaseEntityLittleMaidEx(World par1World) {
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
		float speed = LMMExtension.delegateModeExs(LMMModeExAIHandler.class, this,
				new ModeExAIMoveSpeedWorker());
		if (speed < 0) {
			return super.getAIMoveSpeed();
		}
		return speed;
	}

	@Override
	public void handleHealthUpdate(byte par1) {
		LMM_EntityModeBase mode = getActiveModeClass();
		if (mode instanceof LMMModeExHandleHealthUpdateHandler) {
			if (((LMMModeExHandleHealthUpdateHandler) mode).handleHealthUpdate(this, getMaidModeInt(), par1) == TaskState.BREAK) {
				return;
			}
		}
		super.handleHealthUpdate(par1);
	}

	@Override
	public boolean interact(EntityPlayer par1EntityPlayer) {
		System.out.println("Ex!");
		LMM_EntityModeBase mode = getActiveModeClass();
		if (mode instanceof LMMModeExInteractHandler) {
			if (((LMMModeExInteractHandler) mode).overInteract(par1EntityPlayer)) {
				return true;
			}
		}
		return super.interact(par1EntityPlayer);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		final NBTTagCompound finalTagCompound = par1nbtTagCompound;
		super.readEntityFromNBT(finalTagCompound);
		LMMExtension.delegateModeExs(LMMModeExNBTHandler.class, this,
				new ReadEntityFromNBTWorker(finalTagCompound));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		final NBTTagCompound finalTagCompound = par1nbtTagCompound;
		super.writeEntityToNBT(finalTagCompound);
		LMMExtension.delegateModeExs(LMMModeExNBTHandler.class, this,
				new WriteEntityToNBTWorker(finalTagCompound));
	}
}
