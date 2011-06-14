package com.netappsid.jpaquery.internal;

public interface Condition<T> {

	String getVariableName();

	T getValue();

	String createQueryFragment(QueryBuilder queryBuilder);
}
