package org.torpedoquery.jpa.internal;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.Function;

public abstract class AggregateFunctionHandler<T, F extends Function<T>> implements QueryHandler<F>, Function<T> {

	private MethodCall method;
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
	public F handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methods) {

		if (!methods.isEmpty()) {
			method = methods.pollFirst();
			proxy = method.getProxy();
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
