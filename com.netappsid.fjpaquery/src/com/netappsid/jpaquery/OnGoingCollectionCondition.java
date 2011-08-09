package com.netappsid.jpaquery;

public interface OnGoingCollectionCondition<T> {

	OnGoingLogicalCondition isEmpty();

	OnGoingLogicalCondition isNotEmpty();

	OnGoingComparableCondition<Integer> size();

}
