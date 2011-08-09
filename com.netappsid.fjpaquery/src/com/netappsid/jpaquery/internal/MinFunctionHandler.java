package com.netappsid.jpaquery.internal;

import com.netappsid.jpaquery.ComparableFunction;

public class MinFunctionHandler<T> extends AggregateFunctionHandler<T, ComparableFunction<T>> implements ComparableFunction<T> {

	@Override
	protected String getFunctionName() {
		return "min";
	}

}
