package com.netappsid.jpaquery.internal;

public class IsNullCondition implements Condition {
	private final String fieldPath;
	
	public IsNullCondition(String fieldPath) {
		this.fieldPath = fieldPath;
	}
	
	@Override
	public String getString() {
		return fieldPath + " is null";
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
