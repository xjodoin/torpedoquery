package com.netappsid.jpaquery.internal;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IsNotNullCondition implements Condition {

	private final Selector selector;

	public IsNotNullCondition(Selector selector) {
		this.selector = selector;
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder, AtomicInteger incrementor) {
		return selector.createQueryFragment(queryBuilder, incrementor) + " is not null";
	}

	@Override
	public List getParameters() {
		return Collections.emptyList();
	}

}
