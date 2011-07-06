package com.netappsid.jpaquery;

public interface OnGoingCollectionCondition<T> {

	OnGoingLogicalCondition isEmpty();

	OnGoingLogicalCondition isNotEmpty();

	OnGoingCondition<Integer> size();

}
