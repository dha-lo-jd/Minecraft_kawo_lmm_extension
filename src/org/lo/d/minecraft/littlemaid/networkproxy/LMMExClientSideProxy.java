package org.lo.d.minecraft.littlemaid.networkproxy;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.LMM_Client;
import net.minecraft.src.LMM_EntityLittleMaid;
import net.minecraft.world.World;

import org.lo.d.minecraft.littlemaid.LMMExtension;
import org.lo.d.minecraft.littlemaid.LMMExtension.ModeExWorker;
import org.lo.d.minecraft.littlemaid.entity.EntityLittleMaidEx;
import org.lo.d.minecraft.littlemaid.mode.LMMModeExGuiHandler;
import org.lo.d.minecraft.littlemaid.renderer.RenderLittleMaidEx;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LMMExClientSideProxy extends LMMExNetworkSideProxy {

	@Override
	public Object getClientGuiElement(final int guiId, final EntityPlayer player, World world, int x, int y, int z) {
		Entity lentity = world.getEntityByID(x);
		if (lentity instanceof EntityLittleMaidEx && lentity instanceof LMM_EntityLittleMaid) {
			GuiContainer gui = LMMExtension.delegateModeExs(LMMModeExGuiHandler.class, (LMM_EntityLittleMaid) lentity,
					new ModeExWorker<LMMModeExGuiHandler, GuiContainer>() {
						private GuiContainer gui = null;

						@Override
						public GuiContainer getResult() {
							return gui;
						}

						@Override
						public State work(LMM_EntityLittleMaid maid, LMMModeExGuiHandler modeEx) {
							gui = modeEx.getOpenGuiInventory(guiId, player, maid, maid.maidMode);
							if (gui != null) {
								return State.BREAK;
							}
							return State.CONTINUE;
						}
					});
			if (gui != null) {
				return gui;
			}
		}
		if (player instanceof EntityClientPlayerMP) {
			return LMM_Client.getContainerGUI((EntityClientPlayerMP) player, guiId, x, y, z);
		}
		return null;
	}

	@Override
	public void registMaidEntityAndRenderers(Class<? extends LMM_EntityLittleMaid> cls, String entityName) {
		super.registMaidEntityAndRenderers(cls, entityName);
		RenderingRegistry.registerEntityRenderingHandler(cls, new RenderLittleMaidEx(0.3F));
	}
}
