package com.netappsid.jpaquery.internal;

public class GtCondition<T> extends AbstractCondition<T> {

	public GtCondition(Selector selector, String variableName, T value) {
		super(selector, variableName, value);
	}

	@Override
	protected String getComparator() {
		return ">";
	}

}
