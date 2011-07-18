package com.netappsid.jpaquery.internal;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GroupingCondition implements Condition {

	private final Condition condition;

	public GroupingCondition(Condition condition) {
		this.condition = condition;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return "( " + condition.createQueryFragment(incrementor) + " )";
	}

	@Override
	public List<Parameter> getParameters() {
		return condition.getParameters();
	}

}
