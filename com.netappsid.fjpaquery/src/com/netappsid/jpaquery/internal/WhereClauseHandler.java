package com.netappsid.jpaquery.internal;

import java.util.Deque;
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
	public OnGoingCondition<T> handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, Deque<MethodCall> methodCalls) {
		MethodCall pollFirst = methodCalls.pollFirst();
		final QueryBuilder queryImpl = proxyQueryBuilders.get(pollFirst.getProxy());
		final WhereClause<T> whereClause = new WhereClause<T>(queryImpl, pollFirst.getMethod());
		if (registerWhereClause) {
			queryImpl.setWhereClause(whereClause);
		}
		return whereClause;
	}
}
