package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;

public interface MethodCall {

	public abstract Proxy getProxy();

	public abstract Method getMethod();

	public abstract String getFullPath();

	public abstract String getParamName();

}