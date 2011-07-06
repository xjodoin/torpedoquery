package com.netappsid.jpaquery;

public interface OnGoingStringCondition<T> extends OnGoingCondition<T> {
	OnGoingLikeCondition like();
}
