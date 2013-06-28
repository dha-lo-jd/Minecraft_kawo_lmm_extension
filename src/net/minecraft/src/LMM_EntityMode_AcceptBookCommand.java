package net.minecraft.src;

import java.util.Set;

import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.src.BookCommandProcessor.AbstractBookCommandProcessor;
import net.minecraft.src.BookCommandProcessor.State;

import org.lo.d.commons.books.BookReaderSupport.BookCommandBean;
import org.lo.d.commons.books.BookReaderSupport.BookCommandBeanFactory;
import org.lo.d.minecraft.littlemaid.LittleMaidModeConfiguration;
import org.lo.d.minecraft.littlemaid.entity.EntityLittleMaidEx;

import com.google.common.collect.Sets;

@LittleMaidModeConfiguration
public class LMM_EntityMode_AcceptBookCommand extends LMM_EntityModeBase {

	public static class ModeAlias {
		private final String[] keys;

		private final int modeId;

		private final boolean allowNotExMaid;

		public ModeAlias(int modeId, boolean allowNotExMaid, String... aliases) {
			keys = aliases;
			this.modeId = modeId;
			this.allowNotExMaid = allowNotExMaid;
		}

		public ModeAlias(int modeId, String... aliases) {
			this(modeId, false, aliases);
		}

		public boolean hasValue(String alias) {
			for (String k : keys) {
				if (k.toUpperCase().equals(alias.toUpperCase())) {
					return true;
				}
			}
			return false;
		}
	}

	private static class ModeChangeBookCommandProcessor extends
			AbstractBookCommandProcessor<ModeChangeBookCommandProcessor.MaidModeCommand> {
		private static class MaidModeCommand implements BookCommandBean {
			@Required
			@Aliases({ @Alias("m") })
			String mode;
		}

		@Override
		protected State doProcess(LMM_EntityLittleMaid owner, MaidModeCommand command, boolean overInteract) {
			if (command != null && command.mode != null) {
				if (command.mode.toUpperCase().equals("Erase".toUpperCase())) {
					owner.isDead = true;
					return State.BREAK;
				}
				for (ModeAlias alias : LMM_EntityMode_AcceptBookCommand.modes) {
					if (alias.hasValue(command.mode)) {
						if (!alias.allowNotExMaid && !(owner instanceof EntityLittleMaidEx)) {
							continue;
						}
						if (alias.modeId == owner.getMaidModeInt()) {
							break;
						}
						if (!owner.worldObj.isRemote) {
							owner.setMaidMode(alias.modeId);
							if (overInteract) {
								owner.worldObj.setEntityState(owner, (byte) 11);
								owner.playSound("random.pop");
							}
						}
						return State.CONTINUE_AND_EXIT;
					}
				}
			}
			return State.CONTINUE;
		}

		@Override
		protected BookCommandBeanFactory<MaidModeCommand> getFactory() {
			return new BookCommandBeanFactory<MaidModeCommand>() {
				@Override
				public MaidModeCommand createBean() {
					return new MaidModeCommand();
				}
			};
		}

	}

	public static final String MODE_NAME = "AcceptBookCommand";

	@LittleMaidModeConfiguration.ResolveModeId(modeName = MODE_NAME)
	public static int MODE_ID = 0x0a03;

	protected static final Set<ModeAlias> modes;

	protected static final Set<BookCommandProcessor<?>> processors;
	static {
		{
			Set<ModeAlias> set = Sets.newHashSet();
			modes = set;
		}
		{
			Set<BookCommandProcessor<?>> set = Sets.newHashSet();
			set.add(new ModeChangeBookCommandProcessor());
			set.add(new NamingBookCommandProcessor());
			processors = set;
		}
	}

	public static boolean add(BookCommandProcessor<?> e) {
		return processors.add(e);
	}

	public static boolean add(ModeAlias alias) {
		return modes.add(alias);
	}

	public static void setupDefaultModeCommands() {
		add(new ModeAlias(LMM_EntityMode_Archer.mmode_Archer, true, "Archer", "Ar"));
		add(new ModeAlias(LMM_EntityMode_Archer.mmode_Blazingstar, true, "Blazingstar", "Blz"));
		add(new ModeAlias(LMM_EntityMode_Basic.mmode_Escorter, true, "Escorter", "Es"));
		add(new ModeAlias(LMM_EntityMode_Cooking.mmode_Cooking, true, "Cooking", "Co"));
		add(new ModeAlias(LMM_EntityMode_Fencer.mmode_Fencer, true, "Fencer", "Fe"));
		add(new ModeAlias(LMM_EntityMode_Fencer.mmode_Bloodsucker, true, "Bloodsucker", "Bld"));
		add(new ModeAlias(LMM_EntityMode_Healer.mmode_Healer, true, "Healer", "He"));
		add(new ModeAlias(LMM_EntityMode_Pharmacist.mmode_Pharmacist, true, "Pharmacist", "Ph"));
		add(new ModeAlias(LMM_EntityMode_Ripper.mmode_Ripper, true, "Ripper", "Ri"));
		add(new ModeAlias(LMM_EntityMode_Torcher.mmode_Torcher, true, "Torcher", "To"));
	}

	public LMM_EntityMode_AcceptBookCommand(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.maidInventory.getStackInSlot(0);
		return changeMode(litemstack, false);
	}

	@Override
	public void init() {
		super.init();
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
		boolean isExit = false;
		for (BookCommandProcessor<?> processor : processors) {
			State state = processor.doBookCommandProcess(owner, litemstack, overInteract);
			switch (state) {
			case BREAK:
				return false;
			case CONTINUE:
				continue;
			case CONTINUE_AND_EXIT:
				isExit = true;
				continue;
			}
		}

		return isExit;
	}
}
