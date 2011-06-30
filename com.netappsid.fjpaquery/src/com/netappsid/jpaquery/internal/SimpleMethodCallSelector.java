package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleMethodCallSelector implements Selector {

	private final Method method;

	public SimpleMethodCallSelector(Method method) {
		this.method = method;
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder, AtomicInteger incrementor) {
		return queryBuilder.getAlias(incrementor) + "." + queryBuilder.getFieldName(method);
	}

}
