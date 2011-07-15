package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.FJPAQuery;
import com.netappsid.jpaquery.Function;

public class DistinctFunctionHandler<T> implements Function<T>, QueryHandler<Function<T>> {

	private Object proxy;
	private Method method;

	public DistinctFunctionHandler(Object proxy) {
		this.proxy = proxy;
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder, AtomicInteger incrementor) {
		if (method != null) {
			SimpleMethodCallSelector simpleMethodCallSelector = new SimpleMethodCallSelector(method);
			return "distinct " + simpleMethodCallSelector.createQueryFragment(queryBuilder, incrementor);
		} else {
			return "distinct " + FJPAQuery.getFJPAMethodHandler().getQueryBuilder(proxy).getAlias(incrementor);
		}
	}

	@Override
	public Function<T> handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, Deque<MethodCall> methods) {

		if (!methods.isEmpty()) {
			MethodCall methodCall = methods.pollFirst();
			method = methodCall.getMethod();
			proxy = methodCall.getProxy();
		}

		return this;
	}

	@Override
	public Object getProxy() {
		return proxy;
	}

	@Override
	public String getName() {
		return "";
	}

}
