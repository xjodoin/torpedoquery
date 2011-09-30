package org.torpedoquery.jpa.internal;

import java.util.concurrent.atomic.AtomicInteger;

public class ObjectSelector<T> implements Selector<T> {

	private final QueryBuilder<?> builder;

	public ObjectSelector(QueryBuilder<?> builder) {
		this.builder = builder;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return builder.getAlias(incrementor);
	}

	@Override
	public Parameter<T> generateParameter(Object value) {
		return new SelectorParameter<T>(this);
	}

}
