package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;

import com.netappsid.jpaquery.OnGoingWhereClause;

public class WhereClauseHandler<T> implements
		QueryHandler<com.netappsid.jpaquery.OnGoingWhereClause<T>> {

	@Override
	public OnGoingWhereClause<T> handleCall(QueryBuilder queryImpl,
			Method thisMethod) {
		WhereClause<T> whereClause = new WhereClause<T>(queryImpl,queryImpl.getFieldName(thisMethod));
		queryImpl.addWhereClause(whereClause);
		return whereClause;
	}

}
