package com.netappsid.jpaquery.internal;

import java.util.List;
import java.util.Map;

import com.netappsid.jpaquery.OnGoingWhereClause;

public class WhereClauseHandler<T> implements QueryHandler<com.netappsid.jpaquery.OnGoingWhereClause<T>> {
    @Override
    public OnGoingWhereClause<T> handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, List<MethodCall> methodCalls) {
	final QueryBuilder queryImpl = proxyQueryBuilders.get(methodCalls.get(0).proxy);
	final WhereClause<T> whereClause = new WhereClause<T>(queryImpl, queryImpl.getFieldName(methodCalls.get(0).method));
	queryImpl.addWhereClause(whereClause);
	return whereClause;
    }
}
