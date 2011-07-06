package com.netappsid.jpaquery;

public interface OnGoingNumberCondition<T> extends OnGoingCondition<T> {

	OnGoingLogicalCondition lt(T value);

	OnGoingLogicalCondition lte(T value);

	OnGoingLogicalCondition gt(T value);

	OnGoingLogicalCondition gte(T value);
}
