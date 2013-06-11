package org.lo.d.minecraft.littlemaid.mode.strategy;

public interface Strategy {
	public static abstract class DefaultImpl implements Strategy {
		@Override
		public void onChangeStrategy() {
		}

		@Override
		public boolean onCurrentStrategyUpdate() {
			return false;
		}

		@Override
		public void onUpdateStrategy() {
		}

		@Override
		public boolean shouldStrategy() {
			return false;
		}

		@Override
		public void startStrategy() {
		}

		@Override
		public void stopStrategy() {
		}
	}

	public abstract void onChangeStrategy();

	public abstract boolean onCurrentStrategyUpdate();

	public abstract void onUpdateStrategy();

	public abstract boolean shouldStrategy();

	public abstract void startStrategy();

	public abstract void stopStrategy();

}