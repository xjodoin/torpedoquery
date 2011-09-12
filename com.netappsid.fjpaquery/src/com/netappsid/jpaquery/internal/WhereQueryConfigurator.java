package com.netappsid.jpaquery.internal;

public class WhereQueryConfigurator<T> implements QueryConfigurator<T> {

	@Override
	public void configure(QueryBuilder<T> builder, ConditionBuilder<T> condition) {
		builder.setWhereClause(condition);
	}

}
