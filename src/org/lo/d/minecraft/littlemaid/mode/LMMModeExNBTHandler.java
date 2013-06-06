package org.lo.d.minecraft.littlemaid.mode;

import net.minecraft.nbt.NBTTagCompound;

public interface LMMModeExNBTHandler extends LMMModeExHandler {
	public void readFromNBT(NBTTagCompound par1nbtTagCompound);

	public void writeToNBT(NBTTagCompound par1nbtTagCompound);
}
