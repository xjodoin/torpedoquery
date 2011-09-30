package org.torpedoquery.jpa.internal;

import java.util.Deque;
import java.util.Iterator;
import java.util.Map;

import org.torpedoquery.jpa.Function;

public class ArrayCallHandler implements QueryHandler<Void> {

	public static interface ValueHandler {
		void handle(Proxy proxy, QueryBuilder queryBuilder, Selector selector);
	}

	private final Object[] values;
	private final ValueHandler handler;

	public ArrayCallHandler(ValueHandler handler, Object[] values) {
		this.handler = handler;
		this.values = values;
	}

	@Override
	public Void handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methodCalls) {

		Iterator<MethodCall> iterator = methodCalls.descendingIterator();

		Proxy proxy = null;

		for (int i = 0; i < values.length; i++) {

			Object param = values[i];
			QueryBuilder queryBuilder;
			Selector selector;

			if (param instanceof Function) {
				Function function = (Function) values[i];
				selector = function;
				proxy = (Proxy) function.getProxy();
				queryBuilder = proxyQueryBuilders.get(proxy);

			} else if (param instanceof Proxy) {
				proxy = (Proxy) param;
				queryBuilder = proxyQueryBuilders.get(proxy);
				selector = new ObjectSelector(queryBuilder);
			} else {

				MethodCall methodCall = iterator.next();
				iterator.remove();
				proxy = methodCall.getProxy();
				queryBuilder = proxyQueryBuilders.get(proxy);
				selector = new SimpleMethodCallSelector(queryBuilder, methodCall);
			}

			handler.handle(proxy, queryBuilder, selector);
		}
		return null;

	}
}
