package org.torpedoquery.jpa.internal;

public class DoNothingQueryConfigurator<T> implements QueryConfigurator<T> {

	@Override
	public void configure(QueryBuilder<T> builder, ConditionBuilder<T> condition) {
	}

}
