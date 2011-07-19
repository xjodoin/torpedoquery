package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.Function;

public abstract class AggregateFunctionHandler<T, F extends Function<T>> implements QueryHandler<F>, Function<T> {

	private Method method;
	private Object proxy;
	private QueryBuilder queryBuilder;

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {

		SimpleMethodCallSelector simpleMethodCallSelector = new SimpleMethodCallSelector(queryBuilder, method);
		return getFunctionName() + "(" + simpleMethodCallSelector.createQueryFragment(incrementor) + ")";
	}

	@Override
	public Object getProxy() {
		return proxy;
	}

	@Override
	public F handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, Deque<MethodCall> methods) {

		if (!methods.isEmpty()) {
			MethodCall methodCall = methods.pollFirst();
			method = methodCall.getMethod();
			proxy = methodCall.getProxy();
			queryBuilder = proxyQueryBuilders.get(proxy);
		}

		return (F) this;
	}

	protected abstract String getFunctionName();

	@Override
	public Parameter<T> generateParameter(T value) {
		return new SelectorParameter<T>(this);
	}

}
