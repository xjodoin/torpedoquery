package com.netappsid.jpaquery.internal;

public class AvgFunctionHandler extends AggregateFunctionHandler {

	@Override
	protected String getFunctionName() {
		return "avg";
	}

}
