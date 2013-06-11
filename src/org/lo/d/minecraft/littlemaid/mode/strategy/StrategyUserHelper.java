package org.lo.d.minecraft.littlemaid.mode.strategy;

import java.util.List;

import com.google.common.collect.Lists;

public class StrategyUserHelper<S extends Strategy> {
	private S currentStrategy = null;
	private S defaultStrategy = null;
	private final List<S> strategies = Lists.newArrayList();

	public <T extends S> StrategyUserHelper(T defaultStrategy) {
		this.defaultStrategy = defaultStrategy;
		this.currentStrategy = defaultStrategy;
	}

	public boolean add(S arg0) {
		return strategies.add(arg0);
	}

	public S getCurrentStrategy() {
		return currentStrategy != null ? currentStrategy : defaultStrategy;
	}

	public Iterable<S> getStrategies() {
		return strategies;
	}

	public boolean updateCurrentStrategy() {
		boolean onChange = false;
		S newStrategy = defaultStrategy;
		for (S strategy : strategies) {
			if (strategy.shouldStrategy()) {
				newStrategy = strategy;
				break;
			}
		}
		if (newStrategy != currentStrategy) {
			currentStrategy.stopStrategy();
			currentStrategy.onChangeStrategy();
			currentStrategy = newStrategy;
			newStrategy.startStrategy();
			onChange = true;
		}
		onChange = onChange | currentStrategy.onCurrentStrategyUpdate();
		return onChange;
	}
}
