package com.netappsid.jpaquery.internal;

import java.util.List;
import java.util.Map;

import com.netappsid.jpaquery.OnGoingCondition;

public class WhereClauseHandler<T> implements QueryHandler<com.netappsid.jpaquery.OnGoingCondition<T>> {

	private final boolean registerWhereClause;

	public WhereClauseHandler() {
		this(true);
	}

	public WhereClauseHandler(boolean registerWhereClause) {
		this.registerWhereClause = registerWhereClause;
	}

	@Override
	public OnGoingCondition<T> handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, List<MethodCall> methodCalls) {
		final QueryBuilder queryImpl = proxyQueryBuilders.get(methodCalls.get(0).getProxy());
		final WhereClause<T> whereClause = new WhereClause<T>(queryImpl, methodCalls.get(0).getMethod());
		if (registerWhereClause) {
			queryImpl.setWhereClause(whereClause);
		}
		return whereClause;
	}
}
