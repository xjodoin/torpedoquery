package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;

public class MethodCall {
	private final Proxy proxy;
	private final Method method;

	public MethodCall(Proxy proxy, Method method) {
		this.proxy = proxy;
		this.method = method;
	}

	public Proxy getProxy() {
		return proxy;
	}

	public Method getMethod() {
		return method;
	}

	@Override
	public String toString() {
		return method.toString();
	}

}