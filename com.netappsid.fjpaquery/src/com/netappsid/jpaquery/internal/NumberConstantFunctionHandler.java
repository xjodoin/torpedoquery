package com.netappsid.jpaquery.internal;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.NumberFunction;

public class NumberConstantFunctionHandler<T extends Number> implements NumberFunction<T, T>, QueryHandler<NumberFunction<T, T>> {

	private final T constant;

	public NumberConstantFunctionHandler(T constant) {
		this.constant = constant;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return constant.toString();
	}

	@Override
	public NumberFunction<T, T> handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, Deque<MethodCall> methods) {
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
