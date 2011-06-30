package com.netappsid.jpaquery.internal;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IsNullCondition implements Condition {
	private final SimpleMethodCallSelector simpleMethodCallSelector;

	public IsNullCondition(SimpleMethodCallSelector simpleMethodCallSelector) {
		this.simpleMethodCallSelector = simpleMethodCallSelector;
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder, AtomicInteger incrementor) {
		return simpleMethodCallSelector.createQueryFragment(queryBuilder, incrementor) + " is null";
	}

	@Override
	public List getParameters() {
		return Collections.emptyList();
	}
}
