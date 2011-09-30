package org.torpedoquery.jpa.internal;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.Function;

public abstract class OrderByFunctionHandler<T> implements QueryHandler<Function<T>>, Function<T> {

	private MethodCall method;
	private Object proxy;
	private QueryBuilder queryBuilder;

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {

		SimpleMethodCallSelector simpleMethodCallSelector = new SimpleMethodCallSelector(queryBuilder, method);
		return simpleMethodCallSelector.createQueryFragment(incrementor) + " " + getFunctionName();
	}

	@Override
	public Object getProxy() {
		return proxy;
	}

	@Override
	public Function<T> handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methods) {

		if (!methods.isEmpty()) {
			method = methods.pollFirst();
			proxy = method.getProxy();
			queryBuilder = proxyQueryBuilders.get(proxy);
		}

		return this;
	}

	protected abstract String getFunctionName();

	@Override
	public Parameter generateParameter(Object value) {
		return null;
	}

}
