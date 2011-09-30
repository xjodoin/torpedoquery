package org.torpedoquery.jpa.internal;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.Function;

public class DistinctFunctionHandler<T> implements Function<T>, QueryHandler<Function<T>> {

	private Object proxy;
	private MethodCall method;
	private QueryBuilder queryBuilder;

	public DistinctFunctionHandler(Object proxy) {
		this.proxy = proxy;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		if (method != null) {
			SimpleMethodCallSelector simpleMethodCallSelector = new SimpleMethodCallSelector(queryBuilder, method);
			return "distinct " + simpleMethodCallSelector.createQueryFragment(incrementor);
		} else {
			return "distinct " + TorpedoMagic.getTorpedoMethodHandler().getQueryBuilder(proxy).getAlias(incrementor);
		}
	}

	@Override
	public Function<T> handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methods) {

		if (proxy == null) {
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
	public Parameter<T> generateParameter(T value) {
		return new SelectorParameter<T>(this);
	}
}
