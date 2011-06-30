package com.netappsid.jpaquery.internal;

public class GtCondition<T> extends SingleParameterCondition<T> {

	public GtCondition(Selector selector, Parameter<T> parameter) {
		super(selector, parameter);
	}

	@Override
	protected String getComparator() {
		return ">";
	}

}
