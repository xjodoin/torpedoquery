package com.netappsid.jpaquery.internal;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IsNotNullCondition implements Condition {
	private final SimpleMethodCallSelector simpleMethodCallSelector;

	public IsNotNullCondition(SimpleMethodCallSelector simpleMethodCallSelector) {
		this.simpleMethodCallSelector = simpleMethodCallSelector;
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder, AtomicInteger incrementor) {
		return simpleMethodCallSelector.createQueryFragment(queryBuilder, incrementor) + " is not null";
	}

	@Override
	public List getParameters() {
		return Collections.emptyList();
	}

}
