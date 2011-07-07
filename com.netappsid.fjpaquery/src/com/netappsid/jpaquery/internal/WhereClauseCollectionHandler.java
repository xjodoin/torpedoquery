package com.netappsid.jpaquery.internal;

import java.util.Deque;
import java.util.Map;

import com.netappsid.jpaquery.OnGoingCollectionCondition;

public class WhereClauseCollectionHandler<T> implements QueryHandler<com.netappsid.jpaquery.OnGoingCollectionCondition<T>> {

	private final boolean registerWhereClause;
	private final LogicalCondition logicalCondition;

	public WhereClauseCollectionHandler() {
		this(null, true);
	}

	public WhereClauseCollectionHandler(boolean registerWhereClause) {
		this(null, registerWhereClause);
	}

	public WhereClauseCollectionHandler(LogicalCondition logicalCondition, boolean registerWhereClause) {
		this.logicalCondition = logicalCondition;
		this.registerWhereClause = registerWhereClause;
	}

	@Override
	public OnGoingCollectionCondition<T> handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, Deque<MethodCall> methodCalls) {
		MethodCall pollFirst = methodCalls.pollFirst();
		final QueryBuilder queryImpl = proxyQueryBuilders.get(pollFirst.getProxy());
		final WhereClause<T> whereClause = logicalCondition != null ? new WhereClause<T>(logicalCondition, queryImpl, pollFirst.getMethod())
				: new WhereClause<T>(queryImpl, pollFirst.getMethod());
		if (registerWhereClause) {
			queryImpl.setWhereClause(whereClause);
		}
		return whereClause;
	}
}
