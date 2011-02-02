package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javassist.util.proxy.ProxyFactory;


public class InnerJoinHandler<T> implements QueryHandler<T> {
    private final FJPAMethodHandler methodHandler;
    
    public InnerJoinHandler(FJPAMethodHandler methodHandler) {
	this.methodHandler = methodHandler;
    }
    
    @Override
    public T handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, List<MethodCall> methodCalls) {
	final QueryBuilder queryImpl = proxyQueryBuilders.get(methodCalls.get(0).proxy);
	final Method thisMethod = methodCalls.get(0).method;
	final Class<?> returnType = thisMethod.getReturnType();

	try {
	    final ProxyFactory proxyFactory = new ProxyFactory();
	    proxyFactory.setSuperclass(returnType);
	    proxyFactory.setInterfaces(new Class[] { Query.class });

	    final Query join = (Query) proxyFactory.create(null, null, methodHandler);
	    final QueryBuilder queryBuilder = methodHandler.addQueryBuilder(join, returnType, queryImpl.getIncrement());
	    
	    queryImpl.addJoin(new InnerJoin(queryBuilder, queryImpl.getFieldName(thisMethod)));

	    return (T) join;

	} catch (Exception e) {
	    e.printStackTrace();
	}

	return null;
    }
}
