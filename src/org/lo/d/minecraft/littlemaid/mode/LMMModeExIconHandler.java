package org.lo.d.minecraft.littlemaid.mode;

import java.util.List;

import org.lo.d.minecraft.littlemaid.MaidExIcon;

public interface LMMModeExIconHandler extends LMMModeExRenderingHandler {
	public float getIconHeight(int maidMode);

	public List<MaidExIcon> getIcons(int maidMode);
}
