package com.netappsid.jpaquery.internal;

import com.netappsid.jpaquery.FJPAQuery;
import com.netappsid.jpaquery.Query;

public class InSubQueryCondition<T> implements Condition<Void> {

	private final Selector selector;
	private final Query<T> query;

	public InSubQueryCondition(Selector selector, Query<T> query) {
		this.selector = selector;
		this.query = query;
	}

	@Override
	public String getVariableName() {
		return null;
	}

	@Override
	public Void getValue() {
		return null;
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder) {
		return selector.createQueryFragment(queryBuilder) + " in ( " + FJPAQuery.query(query) + " ) ";
	}

}
