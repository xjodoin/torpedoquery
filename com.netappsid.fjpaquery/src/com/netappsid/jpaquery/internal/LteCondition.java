package com.netappsid.jpaquery.internal;

public class LteCondition<T> extends AbstractCondition<T> {

	public LteCondition(Selector selector, String variableName, T value) {
		super(selector, variableName, value);
	}

	@Override
	protected String getComparator() {
		return "<=";
	}

}
