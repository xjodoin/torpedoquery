package org.torpedoquery.jpa.internal;

import java.util.List;

public abstract class AbstractCondition<T> implements Condition {
	private final Selector selector;
	private final List<Parameter> parameters;

	public AbstractCondition(Selector selector, List<Parameter> parameters) {
		this.selector = selector;
		this.parameters = parameters;
	}

	public Selector getSelector() {
		return selector;
	}

	@Override
	public List<Parameter> getParameters() {
		return parameters;
	}

}
