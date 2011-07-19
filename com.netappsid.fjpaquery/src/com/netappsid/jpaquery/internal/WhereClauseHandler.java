package com.netappsid.jpaquery.internal;

import java.util.Deque;
import java.util.Map;

import com.netappsid.jpaquery.OnGoingCondition;

//TODO duplicate avec WhereClauseCollectionHandler
public class WhereClauseHandler<T, E extends OnGoingCondition<T>> implements QueryHandler<E> {

	private final boolean registerWhereClause;
	private final LogicalCondition logicalCondition;

	public WhereClauseHandler() {
		this(null, true);
	}

	public WhereClauseHandler(boolean registerWhereClause) {
		this(null, registerWhereClause);
	}

	public WhereClauseHandler(LogicalCondition logicalCondition, boolean registerWhereClause) {
		this.logicalCondition = logicalCondition;
		this.registerWhereClause = registerWhereClause;
	}

	@Override
	public E handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, Deque<MethodCall> methodCalls) {
		MethodCall pollFirst = methodCalls.pollFirst();
		final QueryBuilder queryImpl = proxyQueryBuilders.get(pollFirst.getProxy());
		final ConditionBuilder<T> whereClause = logicalCondition != null ? new ConditionBuilder<T>(logicalCondition, queryImpl, pollFirst.getMethod())
				: new ConditionBuilder<T>(queryImpl, pollFirst.getMethod());
		if (registerWhereClause) {
			queryImpl.setWhereClause(whereClause);
		}
		return (E) whereClause;
	}
}
