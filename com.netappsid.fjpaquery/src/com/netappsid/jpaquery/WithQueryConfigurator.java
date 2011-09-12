package com.netappsid.jpaquery;

import com.netappsid.jpaquery.internal.ConditionBuilder;
import com.netappsid.jpaquery.internal.QueryBuilder;
import com.netappsid.jpaquery.internal.QueryConfigurator;

public class WithQueryConfigurator<T> implements QueryConfigurator<T> {

	@Override
	public void configure(QueryBuilder<T> builder, ConditionBuilder<T> condition) {
		builder.setWithClause(condition);
	}

}
