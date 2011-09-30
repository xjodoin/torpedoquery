package org.torpedoquery.jpa.internal;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.Function;

public class ConstantFunctionHandler<T> implements Function<T>, QueryHandler<Function<T>> {

	private final Object constant;

	public ConstantFunctionHandler(T constant) {
		this.constant = constant;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return constant.toString();
	}

	@Override
	public Function<T> handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methods) {
		return this;
	}

	@Override
	public Object getProxy() {
		return null;
	}

	@Override
	public Parameter<T> generateParameter(T value) {
		return new SelectorParameter<T>(this);
	}
}
