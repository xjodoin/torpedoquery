package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;

public class LinkedMethodCall implements MethodCall {

	private final MethodCall previous;
	private final MethodCall current;

	public LinkedMethodCall(MethodCall previous, MethodCall current) {
		this.previous = previous;
		this.current = current;
	}

	@Override
	public Proxy getProxy() {
		return previous.getProxy();
	}

	@Override
	public Method getMethod() {
		return current.getMethod();
	}

	@Override
	public String getFullPath() {
		return previous.getFullPath() + "." + current.getFullPath();
	}

	@Override
	public String getParamName() {
		return current.getParamName();
	}

}
