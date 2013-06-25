package org.lo.d.minecraft.littlemaid.mode;

import java.util.List;

import org.lo.d.minecraft.littlemaid.MaidExIcon;

public interface LMMModeExIconHandler extends LMMModeExHandler {
	public List<MaidExIcon> getIcons(int maidMode);
}
