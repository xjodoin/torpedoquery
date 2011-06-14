package com.netappsid.jpaquery.internal;

public class MaxFunctionHandler extends AggregateFunctionHandler {

	@Override
	protected String getFunctionName() {
		return "max";
	}

}
