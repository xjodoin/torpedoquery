package com.netappsid.jpaquery.internal;

import com.netappsid.jpaquery.NumberFunction;

public class SumFunctionHandler<T extends Number> extends AggregateFunctionHandler<T, NumberFunction<T>> implements NumberFunction<T> {

	@Override
	protected String getFunctionName() {
		return "sum";
	}

}
