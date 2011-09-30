package org.torpedoquery.jpa.internal;


public class AscFunctionHandler<T> extends OrderByFunctionHandler<T> {

	@Override
	protected String getFunctionName() {
		return "asc";
	}

}
