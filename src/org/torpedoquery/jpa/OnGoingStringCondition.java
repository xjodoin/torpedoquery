package org.torpedoquery.jpa;

public interface OnGoingStringCondition<T> extends ValueOnGoingCondition<T> {
	OnGoingLikeCondition like();
}
