package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;


import javassist.util.proxy.ProxyFactory;

public abstract class JoinHandler<T> implements QueryHandler<T> {

	private final FJPAMethodHandler methodHandler;

	public JoinHandler(FJPAMethodHandler methodHandler) {
		this.methodHandler = methodHandler;
	}

	@Override
	public T handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, List<MethodCall> methodCalls) {
		final QueryBuilder queryImpl = proxyQueryBuilders.get(methodCalls.get(0).getProxy());
		final Method thisMethod = methodCalls.get(0).getMethod();
		Class<?> returnType = thisMethod.getReturnType();

		if (Collection.class.isAssignableFrom(returnType)) {
			returnType = (Class<?>) ((ParameterizedType) thisMethod.getGenericReturnType()).getActualTypeArguments()[0];
		}

		try {

			final ProxyFactory proxyFactory = new ProxyFactory();
			proxyFactory.setSuperclass(returnType);
			proxyFactory.setInterfaces(new Class[] { InternalQuery.class });

			final InternalQuery join = (InternalQuery) proxyFactory.create(null, null, methodHandler);
			final QueryBuilder queryBuilder = methodHandler.addQueryBuilder(join, returnType, queryImpl.getIncrement());

			queryImpl.addJoin(createJoin(queryBuilder, queryImpl.getFieldName(thisMethod)));

			return (T) join;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	protected abstract Join createJoin(QueryBuilder queryBuilder, String fieldName);

}