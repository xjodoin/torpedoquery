package com.netappsid.jpaquery.internal;

public class IsNullCondition implements Condition {
	private final SimpleMethodCallSelector simpleMethodCallSelector;

	public IsNullCondition(SimpleMethodCallSelector simpleMethodCallSelector) {
		this.simpleMethodCallSelector = simpleMethodCallSelector;
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder) {
		return simpleMethodCallSelector.createQueryFragment(queryBuilder) + " is null";
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
