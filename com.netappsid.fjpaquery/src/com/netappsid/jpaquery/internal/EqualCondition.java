package com.netappsid.jpaquery.internal;

public class EqualCondition<T> implements Condition {
    private final T value;
    private final String fieldPath;
    private final String variableName;

    public EqualCondition(String fieldPath, String variableName, T value) {
	this.fieldPath = fieldPath;
	this.variableName = variableName;
	this.value = value;
    }

    @Override
    public String getString() {
	return fieldPath + " = :" + variableName;
    }

    public String getVariableName() {
	return variableName;
    }

    public T getValue() {
	return value;
    }
}
