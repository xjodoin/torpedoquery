package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.netappsid.jpaquery.Function;

public class CountFunctionHandler implements Function, QueryHandler<Function> {

	private Object proxy;
	private Method method;

	public CountFunctionHandler(Object proxy) {
		this.proxy = proxy;
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder) {
		if (method != null) {
			SimpleMethodCallSelector simpleMethodCallSelector = new SimpleMethodCallSelector(method);
			return "count(" + simpleMethodCallSelector.createQueryFragment(queryBuilder) + ")";
		} else {
			return "count(*)";
		}
	}

	@Override
	public Function handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, List<MethodCall> methods) {

		if (!methods.isEmpty()) {
			MethodCall methodCall = methods.get(0);
			method = methodCall.getMethod();
			proxy = methodCall.getProxy();
		}

		return this;
	}

	@Override
	public Object getProxy() {
		return proxy;
	}

}
