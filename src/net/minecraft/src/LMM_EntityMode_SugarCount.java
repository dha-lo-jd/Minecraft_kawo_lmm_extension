package net.minecraft.src;

import java.util.List;

import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lo.d.minecraft.littlemaid.LittleMaidModeConfiguration;
import org.lo.d.minecraft.littlemaid.MaidExIcon;
import org.lo.d.minecraft.littlemaid.mode.LMM_EntityModeBaseEx;

import com.google.common.collect.Lists;

@LittleMaidModeConfiguration
public class LMM_EntityMode_SugarCount extends LMM_EntityModeBaseEx {

	private static class MaidSugarCountIcon extends MaidExIcon {
		private final LMM_EntityMode_SugarCount ownerMode;

		public MaidSugarCountIcon(LMM_EntityMode_SugarCount ownerMode) {
			this.ownerMode = ownerMode;
		}

		@Override
		public double getOffsetX() {
			return 4F / 16F;
		}

		@Override
		public String getText() {
			return String.valueOf(ownerMode.getSugarAmount());
		}

		@Override
		public int getTextColor() {
			if (ownerMode.getSugarAmount() > 0) {
				return 0xffffff;
			}
			return 0xff0000;
		}

		@Override
		public String getTexture() {
			return "/textures/items/sugar.png";
		}
	}

	private MaidSugarCountIcon icon = new MaidSugarCountIcon(this);

	private List<MaidExIcon> icons = Lists.<MaidExIcon> newArrayList(icon);

	public LMM_EntityMode_SugarCount(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
	}

	@Override
	public float getIconHeight(int maidMode) {
		return 8;
	}

	@Override
	public List<MaidExIcon> getIcons(int maidMode) {
		return icons;
	}

	@Override
	public int getSugarAmount() {
		int count = 0;
		ItemStack[] mainInventory = owner.maidInventory.mainInventory;
		for (ItemStack element : mainInventory) {
			if (element != null && element.itemID == Item.sugar.itemID) {
				count += element.stackSize;
			}
		}
		return count;
	}

	@Override
	public int priority() {
		return 10;
	}
}
