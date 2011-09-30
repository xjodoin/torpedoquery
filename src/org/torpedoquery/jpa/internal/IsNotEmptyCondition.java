package org.torpedoquery.jpa.internal;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IsNotEmptyCondition implements Condition {

	private final Selector selector;

	public IsNotEmptyCondition(Selector selector) {
		this.selector = selector;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return selector.createQueryFragment(incrementor) + " is not empty ";
	}

	@Override
	public List<Parameter> getParameters() {
		return Collections.emptyList();
	}

}
