package com.netappsid.jpaquery.internal;

public class IsNotNullCondition implements Condition {
	private final SimpleMethodCallSelector simpleMethodCallSelector;

	public IsNotNullCondition(SimpleMethodCallSelector simpleMethodCallSelector) {
		this.simpleMethodCallSelector = simpleMethodCallSelector;
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder) {
		return simpleMethodCallSelector.createQueryFragment(queryBuilder) + " is not null";
	}

	@Override
	public String getVariableName() {
		return null;
	}

	@Override
	public Object getValue() {
		return null;
	}
}
