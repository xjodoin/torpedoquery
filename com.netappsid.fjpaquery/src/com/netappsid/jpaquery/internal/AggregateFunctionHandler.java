package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.Function;

public abstract class AggregateFunctionHandler implements QueryHandler<Function>, Function {

	private Method method;
	private Object proxy;

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder, AtomicInteger incrementor) {

		SimpleMethodCallSelector simpleMethodCallSelector = new SimpleMethodCallSelector(method);
		return getFunctionName() + "(" + simpleMethodCallSelector.createQueryFragment(queryBuilder, incrementor) + ")";
	}

	@Override
	public Object getProxy() {
		return proxy;
	}

	@Override
	public Function handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, Deque<MethodCall> methods) {

		if (!methods.isEmpty()) {
			MethodCall methodCall = methods.pollFirst();
			method = methodCall.getMethod();
			proxy = methodCall.getProxy();
		}

		return this;
	}

	protected abstract String getFunctionName();

	@Override
	public String getName() {
		return "";
	}

}
