package com.netappsid.jpaquery.internal;

import java.util.Deque;
import java.util.Iterator;
import java.util.Map;

import com.netappsid.jpaquery.Function;
import com.netappsid.jpaquery.Query;

public class ArrayCallHandler<T> implements QueryHandler<Query<T>> {

	public static interface ValueHandler {
		void handle(InternalQuery proxy, QueryBuilder queryBuilder, Selector selector);
	}

	private final Object[] values;
	private final ValueHandler handler;

	public ArrayCallHandler(ValueHandler handler, Object[] values) {
		this.handler = handler;
		this.values = values;
	}

	@Override
	public Query<T> handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, Deque<MethodCall> methodCalls) {

		Iterator<MethodCall> iterator = methodCalls.descendingIterator();

		InternalQuery proxy = null;

		for (int i = 0; i < values.length; i++) {

			Object param = values[i];
			QueryBuilder queryBuilder;
			Selector selector;

			if (param instanceof Function) {
				Function function = (Function) values[i];
				selector = function;
				proxy = (InternalQuery) function.getProxy();
				queryBuilder = proxyQueryBuilders.get(proxy);

			} else if (param instanceof InternalQuery) {
				proxy = (InternalQuery) param;
				queryBuilder = proxyQueryBuilders.get(proxy);
				selector = new ObjectSelector(proxy);
			} else {

				MethodCall methodCall = iterator.next();
				iterator.remove();
				proxy = methodCall.getProxy();
				queryBuilder = proxyQueryBuilders.get(proxy);
				selector = new SimpleMethodCallSelector(methodCall.getMethod());
			}

			handler.handle(proxy, queryBuilder, selector);
		}

		return proxy;
	}
}
