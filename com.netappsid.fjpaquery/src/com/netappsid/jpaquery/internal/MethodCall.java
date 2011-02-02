package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;

public class MethodCall {
    public final Object proxy;
    public final Method method;

    public MethodCall(Object proxy, Method method) {
        this.proxy = proxy;
        this.method = method;
    }
}