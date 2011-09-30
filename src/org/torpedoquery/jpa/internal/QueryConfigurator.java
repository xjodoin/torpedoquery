package org.torpedoquery.jpa.internal;

public interface QueryConfigurator<T> {
	void configure(QueryBuilder<T> builder, ConditionBuilder<T> condition);
}
