package com.netappsid.jpaquery;

import java.util.Collection;

public interface OnGoingLogicalCondition {
	<T> OnGoingCondition<T> and(T property);

	<T> OnGoingCondition<T> or(T property);

	<V, T extends Comparable<V>> OnGoingComparableCondition<V> and(T property);

	<V, T extends Comparable<V>> OnGoingComparableCondition<V> or(T property);

	<T> OnGoingCollectionCondition<T> and(Collection<T> object);

	<T> OnGoingCollectionCondition<T> or(Collection<T> object);

	OnGoingStringCondition<String> and(String property);

	OnGoingStringCondition<String> or(String property);

	OnGoingLogicalCondition and(OnGoingLogicalCondition condition);

	OnGoingLogicalCondition or(OnGoingLogicalCondition condition);

}
