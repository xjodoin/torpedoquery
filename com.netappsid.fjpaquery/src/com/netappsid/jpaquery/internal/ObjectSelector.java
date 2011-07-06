package com.netappsid.jpaquery.internal;

import java.util.concurrent.atomic.AtomicInteger;

public class ObjectSelector implements Selector {

	private final InternalQuery proxy;

	public ObjectSelector(InternalQuery proxy) {
		this.proxy = proxy;
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder, AtomicInteger incrementor) {
		return queryBuilder.getAlias(incrementor);
	}

	@Override
	public String getName() {
		return "empty";
	}

}
