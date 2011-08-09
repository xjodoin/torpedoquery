package com.netappsid.jpaquery;

public interface OnGoingComparableCondition<T> extends OnGoingCondition<T> {
	OnGoingLogicalCondition lt(T value);

	OnGoingLogicalCondition lt(ComparableFunction<T> value);

	OnGoingLogicalCondition lte(T value);

	OnGoingLogicalCondition lte(ComparableFunction<T> value);

	OnGoingLogicalCondition gt(T value);

	OnGoingLogicalCondition gt(ComparableFunction<T> value);

	OnGoingLogicalCondition gte(T value);

	OnGoingLogicalCondition gte(ComparableFunction<T> value);
}
