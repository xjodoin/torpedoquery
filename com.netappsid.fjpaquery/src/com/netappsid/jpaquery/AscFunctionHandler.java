package com.netappsid.jpaquery;

import com.netappsid.jpaquery.internal.OrderByFunctionHandler;

public class AscFunctionHandler extends OrderByFunctionHandler {

	@Override
	protected String getFunctionName() {
		return "asc";
	}

}
