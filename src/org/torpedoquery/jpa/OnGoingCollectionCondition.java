package org.torpedoquery.jpa;

public interface OnGoingCollectionCondition<T> extends OnGoingCondition<T> {

	OnGoingLogicalCondition isEmpty();

	OnGoingLogicalCondition isNotEmpty();

	OnGoingComparableCondition<Integer> size();

}
