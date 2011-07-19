package com.netappsid.jpaquery;

import java.util.Collection;

public interface OnGoingLogicalCondition {
	<T1> OnGoingCondition<T1> and(T1 property);

	<T1> OnGoingCondition<T1> or(T1 property);

	<T1 extends Number> OnGoingNumberCondition<T1, T1> and(T1 property);

	<T1 extends Number> OnGoingNumberCondition<T1, T1> or(T1 property);

	<T1> OnGoingCollectionCondition<T1> and(Collection<T1> object);

	<T1> OnGoingCollectionCondition<T1> or(Collection<T1> object);

	OnGoingStringCondition<String> and(String property);

	OnGoingStringCondition<String> or(String property);

	OnGoingLogicalCondition and(OnGoingLogicalCondition condition);

	OnGoingLogicalCondition or(OnGoingLogicalCondition condition);

}
