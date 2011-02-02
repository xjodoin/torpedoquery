package com.netappsid.jpaquery.internal;

import com.netappsid.jpaquery.OnGoingWhereClause;

public class WhereClause<T> implements OnGoingWhereClause<T> {
    private Condition<T> condition;
    private final QueryBuilder queryBuilder;
    private final String fieldName;

    public WhereClause(QueryBuilder queryBuilder, String fieldName) {
	this.queryBuilder = queryBuilder;
	this.fieldName = fieldName;
    }

    @Override
    public OnGoingWhereClause<T> eq(T value) {
	condition = new EqualCondition<T>(queryBuilder.getAlias() + "." + fieldName, queryBuilder.generateVariable(fieldName), value);
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
