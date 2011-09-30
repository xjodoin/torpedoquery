package org.torpedoquery.jpa.internal;

import org.torpedoquery.jpa.ComparableFunction;

public class MaxFunctionHandler<T> extends AggregateFunctionHandler<T, ComparableFunction<T>> implements ComparableFunction<T> {

	@Override
	protected String getFunctionName() {
		return "max";
	}

}
