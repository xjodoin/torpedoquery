/**
 * Copyright (C) 2011 Xavier Jodoin (xavier@jodoin.me)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.torpedoquery.jpa.internal.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javassist.util.proxy.MethodHandler;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.MethodCall;
import org.torpedoquery.jpa.internal.TorpedoProxy;
import org.torpedoquery.jpa.internal.TorpedoMagic;
import org.torpedoquery.jpa.internal.handlers.QueryHandler;

import com.google.common.base.Defaults;
public class TorpedoMethodHandler implements MethodHandler, TorpedoProxy {
	private final Map<Object, QueryBuilder<?>> proxyQueryBuilders = new IdentityHashMap<>();
	private final Deque<MethodCall> methods = new LinkedList<>();
	private final QueryBuilder<?> root;
	private final List<Object> params = new ArrayList<>();

	/**
	 * <p>
	 * Constructor for TorpedoMethodHandler.
	 * </p>
	 *
	 * @param root
	 *            a {@link org.torpedoquery.core.QueryBuilder} object.
	 */
	public TorpedoMethodHandler(QueryBuilder<?> root) {
		this.root = root;
	}

	/**
	 * <p>
	 * addQueryBuilder.
	 * </p>
	 *
	 * @param proxy
	 *            a {@link java.lang.Object} object.
	 * @param queryBuilder
	 *            a {@link org.torpedoquery.core.QueryBuilder} object.
	 * @return a {@link org.torpedoquery.core.QueryBuilder} object.
	 */
	public QueryBuilder<?> addQueryBuilder(Object proxy, QueryBuilder<?> queryBuilder) {
		proxyQueryBuilders.put(proxy, queryBuilder);
		return queryBuilder;
	}

	/** {@inheritDoc} */
	@Override
	public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
		if (thisMethod.getDeclaringClass().equals(TorpedoProxy.class)) {
			try {
				return thisMethod.invoke(this, args);
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}

		methods.addFirst(new SimpleMethodCall((TorpedoProxy) self, thisMethod));
		TorpedoMagic.setQuery((TorpedoProxy) self);

		return createReturnValue(thisMethod.getReturnType());
	}

	private <T> T createReturnValue(final Class<T> returnType)
			throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		if (returnType.isPrimitive()) {
			return Defaults.defaultValue(returnType);
		} else if (!Modifier.isFinal(returnType.getModifiers())
				&& !returnType.getPackage().getName().startsWith("java")) {
			return createLinkedProxy(returnType);
		} else {
			return null;
		}
	}

	private <T> T createLinkedProxy(final Class<T> returnType)
			throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		MethodHandler mh = new SerializableMethodHandler() {

			@Override
			public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
				MethodCall pollFirst = methods.pollFirst();
				LinkedMethodCall linkedMethodCall = new LinkedMethodCall(pollFirst,
						new SimpleMethodCall(pollFirst.getProxy(), thisMethod));
				methods.addFirst(linkedMethodCall);
				return createReturnValue(thisMethod.getReturnType());
			}
		};

		return TorpedoMagic.getProxyfactoryfactory().createProxy(mh, returnType);
	}

	/**
	 * <p>
	 * handle.
	 * </p>
	 *
	 * @param handler
	 *            a {@link org.torpedoquery.jpa.internal.handlers.QueryHandler}
	 *            object.
	 * @param <T>
	 *            a T object.
	 * @return a T object.
	 */
	public <T> T handle(QueryHandler<T> handler) {
		return handler.handleCall(proxyQueryBuilders, methods);
	}

	/**
	 * <p>
	 * getQueryBuilder.
	 * </p>
	 *
	 * @param proxy
	 *            a {@link java.lang.Object} object.
	 * @return a {@link org.torpedoquery.core.QueryBuilder} object.
	 */
	public QueryBuilder<?> getQueryBuilder(Object proxy) {
		return proxyQueryBuilders.get(proxy);
	}

	/**
	 * <p>
	 * Getter for the field <code>root</code>.
	 * </p>
	 *
	 * @param <T>
	 *            a T object.
	 * @return a {@link org.torpedoquery.core.QueryBuilder} object.
	 */
	public <T> QueryBuilder<T> getRoot() {
		return (QueryBuilder<T>) root;
	}

	/** {@inheritDoc} */
	@Override
	public TorpedoMethodHandler getTorpedoMethodHandler() {
		return this;
	}

	/**
	 * <p>
	 * Getter for the field <code>methods</code>.
	 * </p>
	 *
	 * @return a {@link java.util.Deque} object.
	 */
	public Deque<MethodCall> getMethods() {
		return methods;
	}

	/**
	 * <p>
	 * addParam.
	 * </p>
	 *
	 * @param param
	 *            a {@link java.lang.Object} object.
	 */
	public void addParam(Object param) {
		params.add(param);
	}

	/**
	 * <p>
	 * params.
	 * </p>
	 *
	 * @return an array of {@link java.lang.Object} objects.
	 */
	public Object[] params() {
		Object[] array = params.toArray();
		params.clear();
		return array;
	}

}
