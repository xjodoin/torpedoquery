package org.torpedoquery.jpa.internal;

import java.lang.reflect.Method;

public class SimpleMethodCall implements MethodCall {
	private final Proxy proxy;
	private final Method method;

	public SimpleMethodCall(Proxy proxy, Method method) {
		this.proxy = proxy;
		this.method = method;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netappsid.jpaquery.internal.MethodCallTmp#getProxy()
	 */
	@Override
	public Proxy getProxy() {
		return proxy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netappsid.jpaquery.internal.MethodCallTmp#getMethod()
	 */
	@Override
	public Method getMethod() {
		return method;
	}

	@Override
	public String toString() {
		return method.toString();
	}

	@Override
	public String getFullPath() {
		return FieldUtils.getFieldName(method);
	}

	@Override
	public String getParamName() {
		return FieldUtils.getFieldName(method);
	}

}