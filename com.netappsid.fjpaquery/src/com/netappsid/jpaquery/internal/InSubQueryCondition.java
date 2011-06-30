package com.netappsid.jpaquery.internal;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.FJPAQuery;
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
		return selector.createQueryFragment(queryBuilder, incrementor) + " in ( " + FJPAQuery.query(query) + " ) ";
	}

	@Override
	public List<Parameter> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

}
