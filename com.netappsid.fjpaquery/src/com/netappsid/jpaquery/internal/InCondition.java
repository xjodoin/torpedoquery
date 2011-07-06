package com.netappsid.jpaquery.internal;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InCondition<T> extends AbstractCondition<List<T>> {

	private final Selector selector;
	private final Parameter<List<T>> parameter;

	public InCondition(Selector selector, Parameter parameter) {
		super(selector, Arrays.asList(parameter));
		this.selector = selector;
		this.parameter = parameter;
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder, AtomicInteger incrementor) {
		return selector.createQueryFragment(queryBuilder, incrementor) + " " + getFragment() + " ( :" + parameter.generate(incrementor) + " ) ";
	}

	protected String getFragment() {
		return "in";
	}

}
