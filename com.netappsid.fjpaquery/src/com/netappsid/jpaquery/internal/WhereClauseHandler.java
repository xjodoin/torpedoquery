package com.netappsid.jpaquery.internal;

import java.util.List;
import java.util.Map;

import com.netappsid.jpaquery.OnGoingCondition;

public class WhereClauseHandler<T> implements QueryHandler<com.netappsid.jpaquery.OnGoingCondition<T>> {
	@Override
	public OnGoingCondition<T> handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, List<MethodCall> methodCalls) {
		final QueryBuilder queryImpl = proxyQueryBuilders.get(methodCalls.get(0).getProxy());
		final WhereClause<T> whereClause = new WhereClause<T>(queryImpl, methodCalls.get(0).getMethod());
		queryImpl.addWhereClause(whereClause);
		return whereClause;
	}
}
