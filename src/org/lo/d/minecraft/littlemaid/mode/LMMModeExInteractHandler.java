package org.lo.d.minecraft.littlemaid.mode;

import net.minecraft.entity.player.EntityPlayer;

/**
 * interactを完全に上書きするためのハンドラ
 * @author Administrator
 *
 */
public interface LMMModeExInteractHandler extends LMMModeExHandler {

	/**
	 * true返却でinteractの完全な乗っ取り
	 * @param par1EntityPlayer
	 * @return
	 */
	public boolean overInteract(EntityPlayer par1EntityPlayer);
}
