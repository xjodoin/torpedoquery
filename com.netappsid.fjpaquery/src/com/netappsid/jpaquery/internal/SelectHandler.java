package com.netappsid.jpaquery.internal;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.netappsid.jpaquery.Function;
import com.netappsid.jpaquery.Query;

public class SelectHandler<T> implements QueryHandler<Query<T>> {

	private final Object[] values;

	public SelectHandler(Object[] values) {
		this.values = values;
	}

	@Override
	public Query<T> handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, List<MethodCall> methodCalls) {

		Iterator<MethodCall> iterator = methodCalls.iterator();

		InternalQuery proxy = null;

		for (int i = 0; i < values.length; i++) {

			Object param = values[i];

			if (param instanceof Function) {

				Function function = (Function) values[i];
				proxy = (InternalQuery) function.getProxy();
				proxyQueryBuilders.get(proxy).addSelector(function);

			} else {

				MethodCall methodCall = iterator.next();
				proxy = methodCall.getProxy();
				proxyQueryBuilders.get(proxy).addSelector(new SimpleMethodCallSelector(methodCall.getMethod()));
			}
		}

		return proxy;
	}
}
