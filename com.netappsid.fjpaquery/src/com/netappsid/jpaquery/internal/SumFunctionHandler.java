package com.netappsid.jpaquery.internal;

public class SumFunctionHandler extends AggregateFunctionHandler {

	@Override
	protected String getFunctionName() {
		return "sum";
	}

}
