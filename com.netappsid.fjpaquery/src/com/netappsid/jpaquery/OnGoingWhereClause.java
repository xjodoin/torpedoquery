package com.netappsid.jpaquery;

public interface OnGoingWhereClause<T> {
    OnGoingCondition<T> eq(T value);
}
