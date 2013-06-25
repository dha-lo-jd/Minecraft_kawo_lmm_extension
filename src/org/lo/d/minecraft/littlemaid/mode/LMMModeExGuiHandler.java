package org.lo.d.minecraft.littlemaid.mode;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.src.LMM_EntityLittleMaid;

public interface LMMModeExGuiHandler extends LMMModeExHandler {

	/**
	 * 開かせたい拡張コンテナ
	 * @param var1
	 * @param lentity
	 * @return
	 */
	public Container getContainerInventory(int guiId, EntityPlayer player, LMM_EntityLittleMaid maid, int maidMode);

	/**
	 * 開かせたい拡張インベントリ
	 * @param var1
	 * @param lentity
	 * @return
	 */
	public GuiContainer getOpenGuiInventory(int guiId, EntityPlayer var1, LMM_EntityLittleMaid maid, int maidMode);
}
