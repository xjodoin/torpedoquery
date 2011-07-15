package com.netappsid.jpaquery.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;

import javassist.util.proxy.MethodHandler;

import com.netappsid.jpaquery.FJPAQuery;
import com.netappsid.jpaquery.Query;

public class FJPAMethodHandler implements MethodHandler, Proxy {
	private final Map<Object, QueryBuilder> proxyQueryBuilders = new IdentityHashMap<Object, QueryBuilder>();
	private final Deque<MethodCall> methods = new LinkedList<MethodCall>();
	private final QueryBuilder root;

	public FJPAMethodHandler(QueryBuilder root) {
		this.root = root;
	}

	public QueryBuilder addQueryBuilder(Object proxy, QueryBuilder queryBuilder) {
		proxyQueryBuilders.put(proxy, queryBuilder);
		return queryBuilder;
	}

	@Override
	public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
		if (thisMethod.getDeclaringClass().equals(Proxy.class)) {
			try {
				return thisMethod.invoke(this, args);
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}

		methods.addFirst(new MethodCall((Proxy) self, thisMethod));
		FJPAQuery.setQuery((Proxy) self);

		final Class returnType = thisMethod.getReturnType();

		if (returnType.isPrimitive()) {
			return returnType.isAssignableFrom(boolean.class) ? false : 0;
		} else {
			return null;
		}
	}

	public <T> T handle(QueryHandler<T> handler) {
		final T result = handler.handleCall(proxyQueryBuilders, methods);
		return result;
	}

	public QueryBuilder getQueryBuilder(Object proxy) {
		return proxyQueryBuilders.get(proxy);
	}

	public <T extends Query> T getRoot() {
		return (T) root;
	}

	@Override
	public FJPAMethodHandler getFJPAMethodHandler() {
		return this;
	}
}
