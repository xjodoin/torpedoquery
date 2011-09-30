package org.torpedoquery.jpa.internal;

public class LtCondition<T> extends SingleParameterCondition<T> {

	public LtCondition(Selector selector, Parameter<T> parameter) {
		super(selector, parameter);
	}

	@Override
	protected String getComparator() {
		return "<";
	}

}
