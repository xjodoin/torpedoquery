package com.netappsid.jpaquery.internal;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.ComparableFunction;

public class ComparableConstantFunctionHandler<T> implements ComparableFunction<T>, QueryHandler<ComparableFunction<T>> {

	private final T constant;

	public ComparableConstantFunctionHandler(T constant) {
		this.constant = constant;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return constant.toString();
	}

	@Override
	public ComparableFunction<T> handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, Deque<MethodCall> methods) {
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
