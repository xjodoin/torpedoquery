package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Deque;
import java.util.Map;

import javassist.util.proxy.ProxyFactory;

public abstract class JoinHandler<T> implements QueryHandler<T> {

	private final FJPAMethodHandler methodHandler;
	private Class<T> realType;

	public JoinHandler(FJPAMethodHandler methodHandler) {
		this.methodHandler = methodHandler;
	}

	public JoinHandler(FJPAMethodHandler fjpaMethodHandler, Class<T> realType) {
		methodHandler = fjpaMethodHandler;
		this.realType = realType;
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
			Class<? extends Object> goodType = getGoodType(returnType);
			proxyFactory.setSuperclass(goodType);
			proxyFactory.setInterfaces(new Class[] { Proxy.class });

			final Proxy join = (Proxy) proxyFactory.create(null, null, methodHandler);
			final QueryBuilder queryBuilder = methodHandler.addQueryBuilder(join, new QueryBuilder(goodType));

			queryImpl.addJoin(createJoin(queryBuilder, FieldUtils.getFieldName(thisMethod)));

			return (T) join;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private Class<? extends Object> getGoodType(Class<?> returnType) {
		return realType != null ? realType :returnType;
	}

	protected abstract Join createJoin(QueryBuilder queryBuilder, String fieldName);

}