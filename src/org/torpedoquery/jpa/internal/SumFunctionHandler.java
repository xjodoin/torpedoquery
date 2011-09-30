package com.netappsid.jpaquery.internal;

import com.netappsid.jpaquery.ComparableFunction;

public class SumFunctionHandler<T> extends AggregateFunctionHandler<T, ComparableFunction<T>> implements ComparableFunction<T> {

	@Override
	protected String getFunctionName() {
		return "sum";
	}

}
