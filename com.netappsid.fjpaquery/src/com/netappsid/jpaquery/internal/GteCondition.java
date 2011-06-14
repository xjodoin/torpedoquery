package com.netappsid.jpaquery.internal;

public class GteCondition<T> extends AbstractCondition<T> {

	public GteCondition(Selector selector, String variableName, T value) {
		super(selector, variableName, value);
	}

	@Override
	protected String getComparator() {
		return ">=";
	}

}
