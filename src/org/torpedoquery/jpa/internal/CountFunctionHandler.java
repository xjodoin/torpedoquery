package org.torpedoquery.jpa.internal;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.Function;

public class CountFunctionHandler implements Function<Long>, QueryHandler<Function<Long>> {

	private Object proxy;
	private MethodCall method;
	private QueryBuilder<?> queryBuilder;

	public CountFunctionHandler(Object proxy) {
		this.proxy = proxy;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		if (method != null) {
			SimpleMethodCallSelector simpleMethodCallSelector = new SimpleMethodCallSelector(queryBuilder, method);
			return "count(" + simpleMethodCallSelector.createQueryFragment(incrementor) + ")";
		} else {
			return "count(*)";
		}
	}

	@Override
	public Function<Long> handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methods) {

		if (!methods.isEmpty()) {
			method = methods.pollFirst();
			proxy = method.getProxy();
			queryBuilder = proxyQueryBuilders.get(proxy);
		}

		return this;
	}

	@Override
	public Object getProxy() {
		return proxy;
	}

	@Override
	public Parameter<Long> generateParameter(Long value) {
		return new SelectorParameter<Long>(this);
	}
}
