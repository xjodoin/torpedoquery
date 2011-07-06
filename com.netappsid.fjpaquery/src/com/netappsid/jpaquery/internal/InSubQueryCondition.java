package com.netappsid.jpaquery.internal;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.Query;

public class InSubQueryCondition<T> implements Condition {

	private final Selector selector;
	private final Query<T> query;

	public InSubQueryCondition(Selector selector, Query<T> query) {
		this.selector = selector;
		this.query = query;
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder, AtomicInteger incrementor) {
		String queryFragment = selector.createQueryFragment(queryBuilder, incrementor);

		InternalQuery subQuery = (InternalQuery) query;
		String subQueryString = subQuery.getQuery(query, incrementor);
		return queryFragment + " " + getFragment() + " ( " + subQueryString + " ) ";
	}

	protected String getFragment() {
		return "in";
	}

	@Override
	public List<Parameter> getParameters() {
		InternalQuery subQuery = (InternalQuery) query;
		return subQuery.getParameters(subQuery);
	}

}
