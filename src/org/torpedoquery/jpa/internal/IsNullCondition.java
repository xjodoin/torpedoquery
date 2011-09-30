package org.torpedoquery.jpa.internal;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IsNullCondition implements Condition {

	private final Selector selector;

	public IsNullCondition(Selector selector) {
		this.selector = selector;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return selector.createQueryFragment(incrementor) + " is null";
	}

	@Override
	public List getParameters() {
		return Collections.emptyList();
	}
}
