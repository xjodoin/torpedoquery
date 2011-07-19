package com.netappsid.jpaquery.internal;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InSubQueryCondition<T> implements Condition {

	private final Selector selector;
	private final QueryBuilder subQuery;

	public InSubQueryCondition(Selector selector, QueryBuilder query) {
		this.selector = selector;
		this.subQuery = query;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		String queryFragment = selector.createQueryFragment(incrementor);

		String subQueryString = subQuery.getQuery(incrementor);
		return queryFragment + " " + getFragment() + " ( " + subQueryString + " ) ";
	}

	protected String getFragment() {
		return "in";
	}

	@Override
	public List<Parameter> getParameters() {
		return subQuery.getValueParameters();
	}

}
