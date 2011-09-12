package com.netappsid.jpaquery.internal;

public interface QueryConfigurator<T> {
	void configure(QueryBuilder<T> builder, ConditionBuilder<T> condition);
}
