package org.lo.d.minecraft.littlemaid;

import net.minecraft.entity.Entity;
import net.minecraft.src.LMM_EntityLittleMaid;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EntityLittleMaidSpawnEventHandler {

	@ForgeSubscribe(priority = EventPriority.NORMAL)
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.world.isRemote)
		{
			return;
		}

		Entity entity = event.entity;
		if (!(entity instanceof LMM_EntityLittleMaid)) {
			return;
		}

		LMM_EntityLittleMaid newMaid = LMMExtension.maidChangeManager.changeMaidToEx((LMM_EntityLittleMaid) entity);
		if (newMaid != null && newMaid != entity) {
			entity.setDead();
			event.setCanceled(true);
			event.world.spawnEntityInWorld(newMaid);
		}
	}
}
