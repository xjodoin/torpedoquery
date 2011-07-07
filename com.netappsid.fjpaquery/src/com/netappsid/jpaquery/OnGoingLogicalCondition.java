package com.netappsid.jpaquery;

public interface OnGoingLogicalCondition {
	<T1> OnGoingCondition<T1> and(T1 property);

	<T1> OnGoingCondition<T1> or(T1 property);

	<T1 extends Number> OnGoingNumberCondition<T1> and(T1 property);

	<T1 extends Number> OnGoingNumberCondition<T1> or(T1 property);

	OnGoingLogicalCondition and(OnGoingLogicalCondition condition);

	OnGoingLogicalCondition or(OnGoingLogicalCondition condition);

}
