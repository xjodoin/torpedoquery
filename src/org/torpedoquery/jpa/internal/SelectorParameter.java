package org.torpedoquery.jpa.internal;

import java.util.concurrent.atomic.AtomicInteger;

public class SelectorParameter<T> implements Parameter<T> {

	private final Selector selector;

	public SelectorParameter(Selector selector) {
		this.selector = selector;
	}

	@Override
	public String generate(AtomicInteger incrementor) {
		return selector.createQueryFragment(incrementor);
	}

}
