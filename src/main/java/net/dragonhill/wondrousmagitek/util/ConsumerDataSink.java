package net.dragonhill.wondrousmagitek.util;

import java.util.function.Consumer;

public class ConsumerDataSink<T> implements Consumer<T> {
	private T lastValue = null;

	public T getLastValue() {
		return this.lastValue;
	}

	@Override
	public void accept(T t) {
		this.lastValue = t;
	}
}
