package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleMethodCallSelector<T> implements Selector<T> {

	private final Method method;
	private final QueryBuilder<?> queryBuilder;

	public SimpleMethodCallSelector(QueryBuilder<?> queryBuilder, Method method) {
		this.queryBuilder = queryBuilder;
		this.method = method;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return queryBuilder.getAlias(incrementor) + "." + getName();
	}

	@Override
	public Parameter<T> generateParameter(T value) {
		return new ValueParameter<T>(getName(), value);
	}

	private String getName() {
		return FieldUtils.getFieldName(method);
	}

}
