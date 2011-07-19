package com.netappsid.jpaquery;

public interface OnGoingCollectionCondition<T> {

	OnGoingLogicalCondition isEmpty();

	OnGoingLogicalCondition isNotEmpty();

	OnGoingNumberCondition<Integer, Integer> size();

}
