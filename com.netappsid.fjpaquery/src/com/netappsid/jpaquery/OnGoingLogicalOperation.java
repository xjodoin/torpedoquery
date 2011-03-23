package com.netappsid.jpaquery;

public interface OnGoingLogicalOperation {
	<T1> OnGoingCondition<T1> and(T1 property);
	<T1> OnGoingCondition<T1> or(T1 property);
}
