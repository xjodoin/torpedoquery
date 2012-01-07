package org.torpedoquery.jpa.internal;

import org.torpedoquery.jpa.Function;

public class CustomFunctionHandler<T> extends BaseFunctionHandler<T, Function<T>> {

	private final String name;

	public CustomFunctionHandler(String name, Object value) {
		super(value);
		this.name = name;
	}

	@Override
	protected String getFunctionName() {
		return name;
	}

}
