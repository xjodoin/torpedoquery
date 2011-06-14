package com.netappsid.jpaquery.internal;

public class LtCondition<T> extends AbstractCondition<T> {

	public LtCondition(Selector selector, String variableName, T value) {
		super(selector, variableName, value);
	}

	@Override
	protected String getComparator() {
		return "<";
	}

}
