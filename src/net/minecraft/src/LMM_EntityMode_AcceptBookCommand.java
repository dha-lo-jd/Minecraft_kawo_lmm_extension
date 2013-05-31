package net.minecraft.src;

import java.util.Set;

import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lo.d.commons.books.BookReaderSupport;
import org.lo.d.commons.books.BookReaderSupport.BookCommandBean;
import org.lo.d.commons.books.BookReaderSupport.BookCommandBeanFactory;
import org.lo.d.minecraft.littlemaid.LMMExtension;
import org.lo.d.minecraft.littlemaid.entity.EntityLittleMaidEx;

import com.google.common.collect.Sets;

public class LMM_EntityMode_AcceptBookCommand extends LMM_EntityModeBase {

	public static class MaidModeCommand implements BookCommandBean {
		@Required
		@Aliases({
			@Alias("m")
		})
		private String mode;
	}

	public static class ModeAlias {
		private final String[] keys;

		private final int modeId;

		public ModeAlias(int modeId, String... aliases) {
			keys = aliases;
			this.modeId = modeId;
		}

		public boolean hasValue(String alias) {
			for (String k : keys) {
				if (k.toUpperCase().equals(alias)) {
					return true;
				}
			}
			return false;
		}
	}

	public static final int MODE_ID = 0x0a03;
	public static final String MODE_NAME = "AcceptBookCommand";

	protected static final Set<ModeAlias> modes;
	static {
		Set<ModeAlias> set = Sets.newHashSet();
		set.add(new ModeAlias(LMM_EntityMode_Archer.mmode_Archer, "Archer", "Ar"));
		set.add(new ModeAlias(LMM_EntityMode_Archer.mmode_Blazingstar, "Blazingstar", "Blz"));
		set.add(new ModeAlias(LMM_EntityMode_Basic.mmode_Escorter, "Escorter", "Es"));
		set.add(new ModeAlias(LMM_EntityMode_Cooking.mmode_Cooking, "Cooking", "Co"));
		set.add(new ModeAlias(LMM_EntityMode_Fencer.mmode_Fencer, "Fencer", "Fe"));
		set.add(new ModeAlias(LMM_EntityMode_Fencer.mmode_Bloodsucker, "Bloodsucker", "Bld"));
		set.add(new ModeAlias(LMM_EntityMode_Healer.mmode_Healer, "Healer", "He"));
		set.add(new ModeAlias(LMM_EntityMode_Pharmacist.mmode_Pharmacist, "Pharmacist", "Ph"));
		set.add(new ModeAlias(LMM_EntityMode_Ripper.mmode_Ripper, "Ripper", "Ri"));
		set.add(new ModeAlias(LMM_EntityMode_Torcher.mmode_Torcher, "Torcher", "To"));
		modes = set;
	}

	public static boolean add(ModeAlias alias) {
		return modes.add(alias);
	}

	public LMM_EntityMode_AcceptBookCommand(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		owner.addMaidMode(ltasks, MODE_NAME, MODE_ID);

	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.maidInventory.getStackInSlot(0);
		return changeMode(litemstack, false);
	}

	@Override
	public boolean interact(EntityPlayer pentityplayer, ItemStack pitemstack) {
		if (owner.isMaidWait() || owner.isOpenInventory()) {
			return false;
		}
		return changeMode(pitemstack, true);
	}

	@Override
	public int priority() {
		return 10;
	}

	private boolean changeMode(ItemStack litemstack, boolean overInteract) {
		MaidModeCommand command = BookReaderSupport.readBookCommandToBean(litemstack,
				new BookCommandBeanFactory<MaidModeCommand>() {
					@Override
					public MaidModeCommand createBean() {
						return new MaidModeCommand();
					}
				});

		if (command != null && command.mode != null) {
			if (command.mode.toUpperCase().equals("Erase".toUpperCase())) {
				owner.isDead = true;
			}
			for (ModeAlias alias : modes) {
				if (alias.hasValue(command.mode)) {
					if (alias.modeId == owner.getMaidModeInt()) {
						break;
					}
					if (!owner.worldObj.isRemote) {
						owner.setMaidMode(alias.modeId);
						LMM_EntityLittleMaid currentMaid = LMMExtension.changeMaidToEx(owner);
						if (overInteract) {
							currentMaid.worldObj.setEntityState(owner, (byte) 11);
							currentMaid.playSound("random.pop");
						}
					}
					if (!(owner instanceof EntityLittleMaidEx)) {
						owner.isDead = true;
					}
					return true;
				}
			}
		}
		return false;
	}
}
