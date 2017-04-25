package org.torpedoquery.jpa;

import java.util.function.Function;

public interface JoinBuilder<T> {

	T on(Function<T, OnGoingLogicalCondition> onBuilder);
	
}
