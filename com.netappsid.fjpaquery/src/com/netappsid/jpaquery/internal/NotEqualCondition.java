package com.netappsid.jpaquery.internal;

public class NotEqualCondition<T> extends AbstractCondition<T> {

	public NotEqualCondition(Selector selector, String variableName, T value) {
		super(selector, variableName, value);
	}

	@Override
	protected String getComparator() {
		return "<>";
	}

}
