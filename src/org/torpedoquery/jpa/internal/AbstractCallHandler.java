package org.torpedoquery.jpa.internal;

import java.util.Iterator;
import java.util.Map;

import org.torpedoquery.jpa.Function;

public abstract class AbstractCallHandler {

	public void handleValue(ValueHandler valueHandler, Map<Object, QueryBuilder<?>> proxyQueryBuilders, Iterator<MethodCall> iterator, Object param) {
		Proxy proxy;
		QueryBuilder queryBuilder;
		Selector selector;

		if (param instanceof Function) {
			Function function = (Function) param;
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

		valueHandler.handle(proxy, queryBuilder, selector);
	}
	
}
