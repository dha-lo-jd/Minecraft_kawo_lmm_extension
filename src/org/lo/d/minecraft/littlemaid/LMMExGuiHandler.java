package org.lo.d.minecraft.littlemaid;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.src.LMM_Client;
import net.minecraft.src.LMM_ContainerInventory;
import net.minecraft.src.LMM_EntityLittleMaid;
import net.minecraft.src.LMM_GuiInventory;
import net.minecraft.world.World;

import org.lo.d.minecraft.littlemaid.LMMExtension.ModeExWorker;
import org.lo.d.minecraft.littlemaid.entity.EntityLittleMaidEx;
import org.lo.d.minecraft.littlemaid.mode.LMMModeExGuiHandler;

import cpw.mods.fml.common.network.IGuiHandler;

public class LMMExGuiHandler implements IGuiHandler {

	@Override
	public Object getClientGuiElement(int ID, final EntityPlayer player, World world, int x, int y, int z) {
		Entity lentity = world.getEntityByID(x);
		if (lentity instanceof EntityLittleMaidEx && lentity instanceof LMM_EntityLittleMaid) {
			LMM_GuiInventory gui = LMMExtension.delegateModeExs(LMMModeExGuiHandler.class,
					(LMM_EntityLittleMaid) lentity,
					new ModeExWorker<LMMModeExGuiHandler, LMM_GuiInventory>() {
						private LMM_GuiInventory gui = null;

						@Override
						public LMM_GuiInventory getResult() {
							return gui;
						}

						@Override
						public State work(LMM_EntityLittleMaid maid, LMMModeExGuiHandler modeEx) {
							gui = modeEx.getOpenGuiInventory(player, maid, maid.maidMode);
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
			return LMM_Client.getContainerGUI((EntityClientPlayerMP) player, ID, x, y, z);
		}
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, final EntityPlayer player, World world, int x, int y, int z) {
		Entity lentity = world.getEntityByID(x);
		if (lentity instanceof EntityLittleMaidEx && lentity instanceof LMM_EntityLittleMaid) {
			LMM_EntityLittleMaid maid = (LMM_EntityLittleMaid) lentity;
			LMM_ContainerInventory container = LMMExtension.delegateModeExs(LMMModeExGuiHandler.class, maid,
					new ModeExWorker<LMMModeExGuiHandler, LMM_ContainerInventory>() {
						private LMM_ContainerInventory container = null;

						@Override
						public LMM_ContainerInventory getResult() {
							return container;
						}

						@Override
						public State work(LMM_EntityLittleMaid maid, LMMModeExGuiHandler modeEx) {
							container = modeEx
									.getContainerInventory(player, maid, maid.maidMode);
							if (container != null) {
								return State.BREAK;
							}
							return State.CONTINUE;
						}
					});
			if (container != null) {
				return container;
			}
			Container lcontainer = new LMM_ContainerInventory(player.inventory, maid.maidInventory);
			return lcontainer;
		}
		return null;
	}

}
