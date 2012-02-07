package org.torpedoquery.jpa.internal.selectors;

import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.Selector;

public class NotSelector<T> implements Selector<T> {

	private final Selector<T> selector;

	public NotSelector(Selector<T> selector) {
		this.selector = selector;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return selector.createQueryFragment(incrementor) + " not";
	}

	@Override
	public Parameter<T> generateParameter(T value) {
		return selector.generateParameter(value);
	}

}
