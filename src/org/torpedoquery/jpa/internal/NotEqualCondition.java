package org.torpedoquery.jpa.internal;

public class NotEqualCondition<T> extends SingleParameterCondition<T> {

	public NotEqualCondition(Selector selector, Parameter<T> parameter) {
		super(selector, parameter);
	}

	@Override
	protected String getComparator() {
		return "<>";
	}

}
