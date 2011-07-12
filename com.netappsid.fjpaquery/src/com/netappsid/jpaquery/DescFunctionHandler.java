package com.netappsid.jpaquery;

import com.netappsid.jpaquery.internal.OrderByFunctionHandler;

public class DescFunctionHandler extends OrderByFunctionHandler {

	@Override
	protected String getFunctionName() {
		return "desc";
	}

}
