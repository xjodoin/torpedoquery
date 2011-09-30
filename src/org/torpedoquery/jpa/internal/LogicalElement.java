package com.netappsid.jpaquery.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class LogicalElement implements Condition {

	private final Condition left;
	private final Condition right;

	public LogicalElement(Condition left, Condition right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public List<Parameter> getParameters() {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.addAll(left.getParameters());
		parameters.addAll(right.getParameters());
		return parameters;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {

		return left.createQueryFragment(incrementor) + getCondition() + right.createQueryFragment(incrementor);
	}

	protected abstract String getCondition();

}
