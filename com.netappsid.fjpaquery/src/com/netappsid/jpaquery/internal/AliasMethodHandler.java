package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import com.netappsid.jpaquery.JPAQuery;

public class AliasMethodHandler implements MethodHandler {
    private final JPAQuery jpaQuery;
    private final Class aliasType;

    public AliasMethodHandler(JPAQuery jpaQuery, Class aliasType) {
	this.jpaQuery = jpaQuery;
	this.aliasType = aliasType;
    }

    public String getAliasName() {
	final String entityName = getEntityName();
	return new String(new char[] { entityName.charAt(0) }).toLowerCase() + entityName.substring(1);
    }

    public String getEntityName() {
	return aliasType.getSimpleName();
    }

    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
	try {

	    final Class fromType = thisMethod.getReturnType();
	    final AliasMethodHandler methodHandler = new AliasMethodHandler(jpaQuery, fromType);
	    final ProxyFactory proxyFactory = new ProxyFactory();

	    proxyFactory.setSuperclass(fromType);

	    return proxyFactory.create(null, null, methodHandler);
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }
}
