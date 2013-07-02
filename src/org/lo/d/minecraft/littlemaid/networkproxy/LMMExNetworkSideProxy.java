package org.lo.d.minecraft.littlemaid.networkproxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.src.LMM_ContainerInventory;
import net.minecraft.src.LMM_EntityLittleMaid;
import net.minecraft.world.World;

import org.lo.d.minecraft.littlemaid.LMMExtension;
import org.lo.d.minecraft.littlemaid.LMMExtension.ModeExWorker;
import org.lo.d.minecraft.littlemaid.entity.EntityLittleMaidEx;
import org.lo.d.minecraft.littlemaid.mode.LMMModeExGuiHandler;
import org.lo.d.minecraft.littlemaid.renderer.RenderLittleMaidEx;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.EntityRegistry;

public class LMMExNetworkSideProxy implements IGuiHandler {

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getServerGuiElement(final int guiId, final EntityPlayer player, World world, int x, int y, int z) {
		Entity lentity = world.getEntityByID(x);
		if (lentity instanceof EntityLittleMaidEx && lentity instanceof LMM_EntityLittleMaid) {
			LMM_EntityLittleMaid maid = (LMM_EntityLittleMaid) lentity;
			Container container = LMMExtension.delegateModeExs(LMMModeExGuiHandler.class, maid,
					new ModeExWorker<LMMModeExGuiHandler, Container>() {
						private Container container = null;

						@Override
						public Container getResult() {
							return container;
						}

						@Override
						public State work(LMM_EntityLittleMaid maid, LMMModeExGuiHandler modeEx) {
							container = modeEx.getContainerInventory(guiId, player, maid, maid.maidMode);
							if (container != null) {
								return State.BREAK;
							}
							return State.CONTINUE;
						}
					});
			if (container != null) {
				return container;
			}
			Container lcontainer = new LMM_ContainerInventory(player.inventory, maid);
			return lcontainer;
		}
		return null;
	}

	public void registMaidEntityAndRenderers(Class<? extends LMM_EntityLittleMaid> cls, int entityId, String entityName) {
		EntityRegistry.registerModEntity(cls, entityName, entityId, LMMExtension.instance, 80, 3, true);
		RenderingRegistry.registerEntityRenderingHandler(cls, new RenderLittleMaidEx(0.3F));
	}
}
