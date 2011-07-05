package com.netappsid.jpaquery;

public interface OnGoingLogicalCondition {
	<T1> OnGoingCondition<T1> and(T1 property);
	<T1> OnGoingCondition<T1> or(T1 property);
}
