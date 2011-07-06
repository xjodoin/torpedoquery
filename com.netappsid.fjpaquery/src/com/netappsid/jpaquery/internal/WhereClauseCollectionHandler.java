package com.netappsid.jpaquery.internal;

import java.util.Deque;
import java.util.Map;

import com.netappsid.jpaquery.OnGoingCollectionCondition;

public class WhereClauseCollectionHandler<T> implements QueryHandler<com.netappsid.jpaquery.OnGoingCollectionCondition<T>> {

	private final boolean registerWhereClause;

	public WhereClauseCollectionHandler() {
		this(true);
	}

	public WhereClauseCollectionHandler(boolean registerWhereClause) {
		this.registerWhereClause = registerWhereClause;
	}

	@Override
	public OnGoingCollectionCondition<T> handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, Deque<MethodCall> methodCalls) {
		MethodCall pollFirst = methodCalls.pollFirst();
		final QueryBuilder queryImpl = proxyQueryBuilders.get(pollFirst.getProxy());
		final WhereClause<T> whereClause = new WhereClause<T>(queryImpl, pollFirst.getMethod());
		if (registerWhereClause) {
			queryImpl.setWhereClause(whereClause);
		}
		return whereClause;
	}
}
