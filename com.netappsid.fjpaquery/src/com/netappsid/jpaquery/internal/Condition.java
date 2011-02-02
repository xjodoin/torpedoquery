package com.netappsid.jpaquery.internal;

public interface Condition<T> {
    String getString();

    String getVariableName();

    T getValue();
}
