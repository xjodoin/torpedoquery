package com.netappsid.jpaquery.internal;

import com.netappsid.jpaquery.OnGoingCondition;
import com.netappsid.jpaquery.OnGoingLogicalOperation;

public class WhereClause<T> implements OnGoingCondition<T>, OnGoingLogicalOperation {
	private Condition<T> condition;
	private final QueryBuilder queryBuilder;
	private final String fieldName;

	public WhereClause(QueryBuilder queryBuilder, String fieldName) {
		this.queryBuilder = queryBuilder;
		this.fieldName = fieldName;
	}

	@Override
	public OnGoingLogicalOperation eq(T value) {
		condition = new EqualCondition<T>(queryBuilder.getAlias() + "." + fieldName, queryBuilder.generateVariable(fieldName), value);
		return this;
	}

	@Override
	public OnGoingLogicalOperation isNull() {
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

	@Override
	public <T1> OnGoingCondition<T1> and(T1 property) {
		return (OnGoingCondition<T1>) this;
	}

	@Override
	public <T1> OnGoingCondition<T1> or(T1 property) {
		return (OnGoingCondition<T1>) this;
	}
}
