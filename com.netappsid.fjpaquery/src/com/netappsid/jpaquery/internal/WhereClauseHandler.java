package com.netappsid.jpaquery.internal;

import java.util.Deque;
import java.util.Map;

import com.netappsid.jpaquery.Function;
import com.netappsid.jpaquery.OnGoingCondition;

//TODO duplicate avec WhereClauseCollectionHandler
public class WhereClauseHandler<T, E extends OnGoingCondition<T>> implements QueryHandler<E> {

	private final boolean registerWhereClause;
	private final LogicalCondition logicalCondition;
	private final Function<T> function;

	public WhereClauseHandler() {
		this(null, true);
	}

	public WhereClauseHandler(boolean registerWhereClause) {
		this(null, registerWhereClause);
	}

	public WhereClauseHandler(LogicalCondition logicalCondition, boolean registerWhereClause) {
		this(null, logicalCondition, registerWhereClause);
	}

	public WhereClauseHandler(Function<T> function, LogicalCondition logicalCondition, boolean registerWhereClause) {
		this.function = function;
		this.logicalCondition = logicalCondition;
		this.registerWhereClause = registerWhereClause;
	}

	@Override
	public E handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, Deque<MethodCall> methodCalls) {

		Selector conditionSelector = function;

		final QueryBuilder queryImpl;

		if (conditionSelector == null) {
			MethodCall pollFirst = methodCalls.pollFirst();
			queryImpl = proxyQueryBuilders.get(pollFirst.getProxy());
			conditionSelector = new SimpleMethodCallSelector(queryImpl, pollFirst.getMethod());
		} else {
			queryImpl = proxyQueryBuilders.get(function.getProxy());
		}

		final ConditionBuilder<T> whereClause = logicalCondition != null ? new ConditionBuilder<T>(logicalCondition, conditionSelector)
				: new ConditionBuilder<T>(conditionSelector);

		if (registerWhereClause) {
			queryImpl.setWhereClause(whereClause);
		}

		return (E) whereClause;
	}
}
