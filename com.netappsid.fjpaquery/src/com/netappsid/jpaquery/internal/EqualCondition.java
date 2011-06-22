package com.netappsid.jpaquery.internal;

public class EqualCondition<T> extends AbstractCondition<T> {

	public EqualCondition(Selector selector, String variableName, T value) {
		super(selector, variableName, value);
	}

	@Override
	protected String getComparator() {
		return "=";
	}

}
