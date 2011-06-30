package com.netappsid.jpaquery.internal;

public class GteCondition<T> extends SingleParameterCondition<T> {

	public GteCondition(Selector selector, Parameter<T> parameter) {
		super(selector, parameter);
	}

	@Override
	protected String getComparator() {
		return ">=";
	}

}
