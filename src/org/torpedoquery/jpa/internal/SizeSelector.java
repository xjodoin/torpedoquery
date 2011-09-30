package org.torpedoquery.jpa.internal;

import java.util.concurrent.atomic.AtomicInteger;

public class SizeSelector<T> implements Selector<T> {

	private final Selector<T> selector;

	public SizeSelector(Selector selector) {
		this.selector = selector;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return selector.createQueryFragment(incrementor) + ".size";
	}

	@Override
	public Parameter<T> generateParameter(T value) {
		return selector.generateParameter(value);
	}

}
