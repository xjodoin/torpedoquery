package org.torpedoquery.jpa.internal.conditions;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.Selector;

public abstract class PolymorphicCondition<T> extends AbstractCondition<T> {

	private final Class<? extends T> condition;
	
	public PolymorphicCondition(Selector selector,Class<? extends T> condition) {
		super(selector, Collections.<Parameter>emptyList());
		this.condition = condition;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return getSelector().createQueryFragment(incrementor) + ".class "+getComparator()+" " + condition.getSimpleName();
	}
	
	protected abstract String getComparator();

}
