package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;


public class MethodCall {
	private final InternalQuery proxy;
	private final Method method;

	public MethodCall(InternalQuery proxy, Method method) {
		this.proxy = proxy;
		this.method = method;
	}

	public InternalQuery getProxy() {
		return proxy;
	}

	public Method getMethod() {
		return method;
	}
}