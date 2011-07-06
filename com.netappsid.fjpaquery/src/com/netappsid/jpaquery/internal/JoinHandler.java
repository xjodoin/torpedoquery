package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Deque;
import java.util.Map;

import javassist.util.proxy.ProxyFactory;

public abstract class JoinHandler<T> implements QueryHandler<T> {

	private final FJPAMethodHandler methodHandler;

	public JoinHandler(FJPAMethodHandler methodHandler) {
		this.methodHandler = methodHandler;
	}

	@Override
	public T handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, Deque<MethodCall> methodCalls) {

		MethodCall pollFirst = methodCalls.pollFirst();

		final QueryBuilder queryImpl = proxyQueryBuilders.get(pollFirst.getProxy());
		final Method thisMethod = pollFirst.getMethod();
		Class<?> returnType = thisMethod.getReturnType();

		if (Collection.class.isAssignableFrom(returnType)) {
			returnType = (Class<?>) ((ParameterizedType) thisMethod.getGenericReturnType()).getActualTypeArguments()[0];
		}

		try {

			final ProxyFactory proxyFactory = new ProxyFactory();
			proxyFactory.setSuperclass(returnType);
			proxyFactory.setInterfaces(new Class[] { InternalQuery.class });

			final InternalQuery join = (InternalQuery) proxyFactory.create(null, null, methodHandler);
			final QueryBuilder queryBuilder = methodHandler.addQueryBuilder(join, returnType);

			queryImpl.addJoin(createJoin(queryBuilder, FieldUtils.getFieldName(thisMethod)));

			return (T) join;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	protected abstract Join createJoin(QueryBuilder queryBuilder, String fieldName);

}