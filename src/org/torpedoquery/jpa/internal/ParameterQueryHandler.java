package org.torpedoquery.jpa.internal;

import java.util.Deque;
import java.util.Map;

public class ParameterQueryHandler<T> implements QueryHandler<Parameter<T>> {

	private final MethodCall method;
	private final T value;

	public ParameterQueryHandler(MethodCall method, T value) {
		this.method = method;
		this.value = value;
	}

	@Override
	public Parameter<T> handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methods) {

		if (!methods.isEmpty()) {
			MethodCall pollFirst = methods.pollFirst();
			return new SelectorParameter<T>(new SimpleMethodCallSelector<T>(proxyQueryBuilders.get(pollFirst.getProxy()), pollFirst));
		} else {
			return new ValueParameter<T>(method.getParamName(), value);
		}
	}

}
