package com.netappsid.jpaquery.internal;

import com.netappsid.jpaquery.OnGoingCondition;
import com.netappsid.jpaquery.OnGoingWhereClause;

public class WhereClause<T> implements OnGoingWhereClause<T>, OnGoingCondition<T> {
	private Condition<T> condition;
	private final QueryBuilder queryBuilder;
	private final String fieldName;

	public WhereClause(QueryBuilder queryBuilder, String fieldName) {
		this.queryBuilder = queryBuilder;
		this.fieldName = fieldName;
	}

	@Override
	public OnGoingCondition<T> eq(T value) {
		condition = new EqualCondition<T>(queryBuilder.getAlias() + "." + fieldName, queryBuilder.generateVariable(fieldName), value);
		return this;
	}
	
	@Override
	public OnGoingCondition<T> isNull() {
		condition = new IsNullCondition(queryBuilder.getAlias() + "." + fieldName);
		return this;
	}

	public String getCondition() {
		return condition.getString();
	}

	public String getVariableName() {
		return condition.getVariableName();
	}

	public T getValue() {
		return condition.getValue();
	}
}
