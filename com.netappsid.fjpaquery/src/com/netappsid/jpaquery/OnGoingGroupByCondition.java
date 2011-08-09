package com.netappsid.jpaquery;

import java.util.Collection;

public interface OnGoingGroupByCondition {

	public <T> OnGoingCondition<T> having(Function<T> object);

	public <V, T extends Comparable<V>> OnGoingComparableCondition<V> having(ComparableFunction<V> object);

	public <T> OnGoingCondition<T> having(T object);

	public <V, T extends Comparable<V>> OnGoingComparableCondition<V> having(T object);

	public OnGoingStringCondition<String> having(String object);

	public <T> OnGoingCollectionCondition<T> having(Collection<T> object);

}
