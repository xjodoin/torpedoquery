package org.torpedoquery.jpa.internal.conditions;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.Selector;

public class BetweenCondition<T> extends AbstractCondition<T> {

	public BetweenCondition(Selector selector, List<Parameter> parameters) {
		super(selector, parameters);
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return getSelector().createQueryFragment(incrementor) + " between " + getParameters().get(0).generate(incrementor) + " and "
				+ getParameters().get(1).generate(incrementor);
	}

}
