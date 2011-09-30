package org.torpedoquery.jpa.internal;


public class WithQueryConfigurator<T> implements QueryConfigurator<T> {

	@Override
	public void configure(QueryBuilder<T> builder, ConditionBuilder<T> condition) {
		builder.setWithClause(condition);
	}

}
