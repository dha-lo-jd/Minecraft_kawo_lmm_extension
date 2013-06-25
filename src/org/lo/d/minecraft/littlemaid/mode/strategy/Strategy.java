package org.lo.d.minecraft.littlemaid.mode.strategy;

public interface Strategy {
	public static abstract class DefaultImpl implements Strategy {

		@Override
		public void onChangeStrategy() {
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

	/**
	 * カレントのStrategyが他のStrategyに変更された時のイベント
	 */
	public abstract void onChangeStrategy();

	/**
	 * カレントのStrategyのTickごとイベント
	 */
	public abstract void onUpdateStrategy();

	/**
	 * StrategyをカレントStrategyとすべきかどうか
	 * @return
	 */
	public abstract boolean shouldStrategy();

	/**
	 * StrategyがカレントStrategyになった後の初回呼び出し
	 */
	public abstract void startStrategy();

	/**
	 * Strategyの停止処理
	 */
	public abstract void stopStrategy();

}