package com.netappsid.jpaquery.internal;

import java.util.concurrent.atomic.AtomicInteger;

public class ObjectSelector implements Selector {

	private final Proxy proxy;

	public ObjectSelector(Proxy proxy) {
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
