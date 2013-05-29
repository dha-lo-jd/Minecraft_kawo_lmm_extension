package org.lo.d.minecraft.littlemaid.mode;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.LMM_ContainerInventory;
import net.minecraft.src.LMM_EntityLittleMaid;
import net.minecraft.src.LMM_GuiInventory;

public interface LMMModeExGuiHandler extends LMMModeExHandler {

	/**
	 * 開かせたい拡張コンテナ
	 * @param var1
	 * @param lentity
	 * @return
	 */
	public LMM_ContainerInventory getContainerInventory(EntityPlayer player, LMM_EntityLittleMaid maid, int maidMode);

	/**
	 * 開かせたい拡張インベントリ
	 * @param var1
	 * @param lentity
	 * @return
	 */
	public LMM_GuiInventory getOpenGuiInventory(EntityPlayer var1, LMM_EntityLittleMaid maid, int maidMode);
}
