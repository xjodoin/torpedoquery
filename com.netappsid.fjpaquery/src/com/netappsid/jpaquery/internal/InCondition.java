package com.netappsid.jpaquery.internal;

import java.util.List;

public class InCondition<T> implements Condition<List<T>> {

	private final Selector selector;
	private final List<T> values;
	private final String variableName;

	public InCondition(Selector selector,String variableName, List<T> values) {
		this.selector = selector;
		this.variableName = variableName;
		this.values = values;
	}

	@Override
	public String getVariableName() {
		return variableName;
	}

	@Override
	public List<T> getValue() {
		return values;
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder) {
		return selector.createQueryFragment(queryBuilder) + " in ( :" + variableName +" ) ";
	}

}
