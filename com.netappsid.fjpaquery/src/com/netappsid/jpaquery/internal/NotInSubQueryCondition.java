package com.netappsid.jpaquery.internal;

import com.netappsid.jpaquery.Query;

public class NotInSubQueryCondition<T> extends InSubQueryCondition<T> {

	public NotInSubQueryCondition(Selector selector, Query<T> query) {
		super(selector, query);
	}

	@Override
	protected String getFragment() {
		return "not " + super.getFragment();
	}
}
