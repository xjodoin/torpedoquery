package com.netappsid.jpaquery;

public interface OnGoingCondition<T> {
	OnGoingLogicalOperation eq(T value);

	OnGoingLogicalOperation isNull();
}
