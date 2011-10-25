package org.torpedoquery.jpa;

import org.torpedoquery.jpa.internal.BaseFunctionHandler;

public class IndexFunctionHandler extends BaseFunctionHandler<Integer, ComparableFunction<Integer>> implements ComparableFunction<Integer> {

	public IndexFunctionHandler(Object object) {
		super(object);
	}

	@Override
	protected String getFunctionName() {
		return "index";
	}

}
