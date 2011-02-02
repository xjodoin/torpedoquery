package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.util.List;

import com.netappsid.jpaquery.OnGoingWhereClause;

public class WhereClauseHandler<T> implements QueryHandler<com.netappsid.jpaquery.OnGoingWhereClause<T>> {
    @Override
    public OnGoingWhereClause<T> handleCall(QueryBuilder queryImpl, List<Method> methods) {
	WhereClause<T> whereClause = new WhereClause<T>(queryImpl, queryImpl.getFieldName(methods.get(0)));
	queryImpl.addWhereClause(whereClause);
	return whereClause;
    }
}
