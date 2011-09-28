package com.netappsid.jpaquery.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import com.netappsid.jpaquery.FJPAQuery;
import com.netappsid.jpaquery.Query;

public class FJPAMethodHandler implements MethodHandler, Proxy {
	private final Map<Object, QueryBuilder> proxyQueryBuilders = new IdentityHashMap<Object, QueryBuilder>();
	private final Deque<MethodCall> methods = new LinkedList<MethodCall>();
	private final QueryBuilder root;
	private final ProxyFactoryFactory proxyfactoryfactory;

	public FJPAMethodHandler(QueryBuilder root, ProxyFactoryFactory proxyfactoryfactory) {
		this.root = root;
		this.proxyfactoryfactory = proxyfactoryfactory;
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

		methods.addFirst(new SimpleMethodCall((Proxy) self, thisMethod));
		FJPAQuery.setQuery((Proxy) self);

		final Class returnType = thisMethod.getReturnType();

		if (returnType.isPrimitive()) {
			return returnType.isAssignableFrom(boolean.class) ? false : 0;
		} else if (!Modifier.isFinal(returnType.getModifiers())) {
			final Object proxy = createLinkedProxy(returnType);
			return proxy;
		} else {
			return null;
		}
	}

	private Object createLinkedProxy(final Class returnType) throws NoSuchMethodException, InstantiationException, IllegalAccessException,
			InvocationTargetException {
		ProxyFactory proxyFactory = proxyfactoryfactory.getProxyFactory();
		if (returnType.isInterface()) {
			proxyFactory.setInterfaces(new Class[] { returnType });
		} else {
			proxyFactory.setSuperclass(returnType);
		}

		final Object proxy = proxyFactory.create(null, null, new MethodHandler() {

			@Override
			public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
				MethodCall pollFirst = methods.pollFirst();
				LinkedMethodCall linkedMethodCall = new LinkedMethodCall(pollFirst, new SimpleMethodCall(pollFirst.getProxy(), thisMethod));
				methods.addFirst(linkedMethodCall);
				Class<?> returnType2 = thisMethod.getReturnType();

				if (returnType2.isPrimitive()) {
					return returnType2.isAssignableFrom(boolean.class) ? false : 0;
				} else if (!Modifier.isFinal(returnType2.getModifiers())) {
					final Object proxy = createLinkedProxy(returnType2);
					return proxy;
				} else {
					return null;
				}
			}
		});
		return proxy;
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
