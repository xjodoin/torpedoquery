package org.torpedoquery.jpa.internal;


public class DescFunctionHandler<T> extends OrderByFunctionHandler<T> {

	@Override
	protected String getFunctionName() {
		return "desc";
	}

}
