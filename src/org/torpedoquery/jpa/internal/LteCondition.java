package org.torpedoquery.jpa.internal;

public class LteCondition<T> extends SingleParameterCondition<T> {

	public LteCondition(Selector selector, Parameter<T> parameter) {
		super(selector, parameter);
	}

	@Override
	protected String getComparator() {
		return "<=";
	}

}
