package net.minecraft.src;

import net.minecraft.item.ItemStack;

import org.lo.d.commons.books.BookReaderSupport;
import org.lo.d.commons.books.BookReaderSupport.BookCommandBean;
import org.lo.d.commons.books.BookReaderSupport.BookCommandBeanFactory;

public interface BookCommandProcessor<T extends BookCommandBean> {
	public static abstract class AbstractBookCommandProcessor<T extends BookCommandBean> implements
			BookCommandProcessor<T> {
		@Override
		public State doBookCommandProcess(LMM_EntityLittleMaid owner, ItemStack litemstack, boolean overInteract) {
			T command = BookReaderSupport.readBookCommandToBean(litemstack,
					getFactory());
			return doProcess(owner, command, overInteract);
		}

		protected abstract State doProcess(LMM_EntityLittleMaid owner, T command, boolean overInteract);

		protected abstract BookCommandBeanFactory<T> getFactory();
	}

	public enum State {
		CONTINUE_AND_EXIT,
		CONTINUE,
		BREAK,
	}

	State doBookCommandProcess(LMM_EntityLittleMaid owner, ItemStack litemstack, boolean overInteract);
}