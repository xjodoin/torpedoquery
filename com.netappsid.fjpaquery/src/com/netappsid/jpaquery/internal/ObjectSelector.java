package com.netappsid.jpaquery.internal;

import java.util.concurrent.atomic.AtomicInteger;

public class ObjectSelector implements Selector {

	private final QueryBuilder<?> builder;

	public ObjectSelector(QueryBuilder<?> builder) {
		this.builder = builder;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return builder.getAlias(incrementor);
	}

	@Override
	public String getName() {
		return "empty";
	}

}
