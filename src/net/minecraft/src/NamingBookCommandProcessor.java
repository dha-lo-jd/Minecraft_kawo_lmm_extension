package net.minecraft.src;

import net.minecraft.src.BookCommandProcessor.AbstractBookCommandProcessor;

import org.lo.d.commons.books.BookReaderSupport.BookCommandBean;
import org.lo.d.commons.books.BookReaderSupport.BookCommandBeanFactory;

public class NamingBookCommandProcessor extends AbstractBookCommandProcessor<NamingBookCommandProcessor.NamingCommand> {
	protected static class NamingCommand implements BookCommandBean {
		@Required
		@Aliases({ @Alias("n") })
		String name;
	}

	@Override
	protected State doProcess(LMM_EntityLittleMaid owner, NamingCommand command, boolean overInteract) {
		if (command != null && command.name != null) {
			String name = command.name.trim();
			if (name == null) {
				name = "";
			}
			if (owner.func_94056_bM() && name.equals(owner.func_94057_bL())) {
				return State.CONTINUE;
			}
			owner.func_94058_c(name);
			return State.CONTINUE_AND_EXIT;
		}
		return State.CONTINUE;
	}

	@Override
	protected BookCommandBeanFactory<NamingCommand> getFactory() {
		return new BookCommandBeanFactory<NamingCommand>() {
			@Override
			public NamingCommand createBean() {
				return new NamingCommand();
			}
		};
	}

}
