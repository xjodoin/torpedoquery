package com.netappsid.jpaquery.internal;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class SingleParameterCondition<T> extends AbstractCondition<T> {

	private final Parameter<T> parameter;

	public SingleParameterCondition(Selector selector, Parameter parameter) {
		super(selector, Arrays.asList(parameter));
		this.parameter = parameter;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return getSelector().createQueryFragment(incrementor) + " " + getComparator() + " :" + parameter.generate(incrementor);
	}

	protected abstract String getComparator();

}
