package com.netappsid.jpaquery.internal;


public class NotInSubQueryCondition<T> extends InSubQueryCondition<T> {

	public NotInSubQueryCondition(Selector selector, QueryBuilder query) {
		super(selector, query);
	}

	@Override
	protected String getFragment() {
		return "not " + super.getFragment();
	}
}
