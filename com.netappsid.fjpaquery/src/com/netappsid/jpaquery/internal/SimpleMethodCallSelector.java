package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;

public class SimpleMethodCallSelector implements Selector {

	private final Method method;

	public SimpleMethodCallSelector(Method method) {
		this.method = method;
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder) {
		return queryBuilder.getAlias() + "." + queryBuilder.getFieldName(method);
	}

}
