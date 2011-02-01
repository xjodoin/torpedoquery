package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;

import javassist.util.proxy.MethodHandler;

public class DefaultMethodHandler implements MethodHandler {
    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        return proceed.invoke(self, args);
    }
}
