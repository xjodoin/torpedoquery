package com.netappsid.jpaquery.internal;

import java.util.concurrent.atomic.AtomicInteger;

public class SimpleMethodCallSelector<T> implements Selector<T> {

	private final MethodCall method;
	private final QueryBuilder<?> queryBuilder;

	public SimpleMethodCallSelector(QueryBuilder<?> queryBuilder, MethodCall method) {
		this.queryBuilder = queryBuilder;
		this.method = method;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return queryBuilder.getAlias(incrementor) + "." + method.getFullPath();
	}

	@Override
	public Parameter<T> generateParameter(T value) {
		return new ValueParameter<T>(method.getParamName(), value);
	}

}
