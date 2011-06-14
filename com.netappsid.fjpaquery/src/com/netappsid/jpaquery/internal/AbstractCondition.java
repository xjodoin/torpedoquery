package com.netappsid.jpaquery.internal;

public abstract class AbstractCondition<T> implements Condition {
	private final T value;
	private final String variableName;
	private final Selector selector;

	public AbstractCondition(Selector selector, String variableName, T value) {
		this.selector = selector;
		this.variableName = variableName;
		this.value = value;
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder) {
		return selector.createQueryFragment(queryBuilder) + " " + getComparator() + " :" + variableName;
	}

	@Override
	public String getVariableName() {
		return variableName;
	}

	@Override
	public T getValue() {
		return value;
	}

	protected abstract String getComparator();
}
