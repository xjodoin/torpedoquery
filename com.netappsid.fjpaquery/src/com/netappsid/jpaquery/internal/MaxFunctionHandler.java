package com.netappsid.jpaquery.internal;

import com.netappsid.jpaquery.NumberFunction;

public class MaxFunctionHandler<T extends Number> extends AggregateFunctionHandler<T, NumberFunction<T>> implements NumberFunction<T> {

	@Override
	protected String getFunctionName() {
		return "max";
	}

}
