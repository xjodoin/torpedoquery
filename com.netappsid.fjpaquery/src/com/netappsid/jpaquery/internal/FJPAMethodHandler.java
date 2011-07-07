package com.netappsid.jpaquery.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javassist.util.proxy.MethodHandler;

import com.netappsid.jpaquery.FJPAQuery;

public class FJPAMethodHandler implements MethodHandler, InternalQuery {
	private final Map<Object, QueryBuilder> proxyQueryBuilders = new IdentityHashMap<Object, QueryBuilder>();
	private final Deque<MethodCall> methods = new LinkedList<MethodCall>();

	public QueryBuilder addQueryBuilder(Object proxy, Class toQuery) {
		final QueryBuilder queryBuilder = new QueryBuilder(toQuery);

		proxyQueryBuilders.put(proxy, queryBuilder);
		return queryBuilder;
	}

	@Override
	public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
		if (thisMethod.getDeclaringClass().equals(InternalQuery.class)) {
			try {
				return thisMethod.invoke(this, args);
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}

		methods.addFirst(new MethodCall((InternalQuery) self, thisMethod));
		FJPAQuery.setQuery((InternalQuery) self);

		final Class returnType = thisMethod.getReturnType();

		if (returnType.isPrimitive()) {
			return returnType.isAssignableFrom(boolean.class) ? false : 0;
		} else {
			return null;
		}
	}

	@Override
	public String getQuery(Object proxy) {
		return getQuery(proxy, new AtomicInteger());
	}

	@Override
	public String getQuery(Object proxy, AtomicInteger incrementor) {
		return proxyQueryBuilders.get(proxy).getQuery(incrementor);
	}

	@Override
	public Map<String, Object> getParametersAsMap(Object proxy) {
		return proxyQueryBuilders.get(proxy).getParametersAsMap();
	}

	@Override
	public List<Parameter> getParameters(Object proxy) {
		return proxyQueryBuilders.get(proxy).getParameters();
	}

	@Override
	public <T> T handle(QueryHandler<T> handler) {
		final T result = handler.handleCall(proxyQueryBuilders, methods);
		return result;
	}

	public QueryBuilder getQueryBuilder(Object proxy) {
		return proxyQueryBuilders.get(proxy);
	}
}
