package com.netappsid.jpaquery.internal;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.netappsid.jpaquery.Function;

public class SelectHandler implements QueryHandler<Void> {

	private final Object[] values;

	public SelectHandler(Object[] values) {
		this.values = values;
	}

	@Override
	public Void handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, List<MethodCall> methodCalls) {

		Iterator<MethodCall> iterator = methodCalls.iterator();

		for (int i = 0; i < values.length; i++) {

			Object param = values[i];

			if (param instanceof Function) {

				Function function = (Function) values[i];
				Object proxy = function.getProxy();
				proxyQueryBuilders.get(proxy).addSelector(function);

			} else {

				MethodCall methodCall = iterator.next();
				proxyQueryBuilders.get(methodCall.proxy).addSelector(new SimpleMethodCallSelector(methodCall.method));
			}
		}

		return null;
	}
}
