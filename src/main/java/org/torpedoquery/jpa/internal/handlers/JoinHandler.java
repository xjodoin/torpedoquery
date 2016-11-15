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
package org.torpedoquery.jpa.internal.handlers;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Deque;
import java.util.Map;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.Join;
import org.torpedoquery.jpa.internal.MethodCall;
import org.torpedoquery.jpa.internal.TorpedoMagic;
import org.torpedoquery.jpa.internal.TorpedoProxy;
import org.torpedoquery.jpa.internal.query.DefaultQueryBuilder;
import org.torpedoquery.jpa.internal.utils.FieldUtils;
import org.torpedoquery.jpa.internal.utils.ProxyFactoryFactory;
import org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler;

import com.google.common.base.Throwables;
public abstract class JoinHandler<T> implements QueryHandler<T> {

	private final TorpedoMethodHandler methodHandler;
	private Class<T> realType;

	/**
	 * <p>
	 * Constructor for JoinHandler.
	 * </p>
	 *
	 * @param methodHandler
	 *            a
	 *            {@link org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler}
	 *            object.
	 */
	public JoinHandler(TorpedoMethodHandler methodHandler) {
		this.methodHandler = methodHandler;
	}

	/**
	 * <p>
	 * Constructor for JoinHandler.
	 * </p>
	 *
	 * @param fjpaMethodHandler
	 *            a
	 *            {@link org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler}
	 *            object.
	 * @param realType
	 *            a {@link java.lang.Class} object.
	 */
	public JoinHandler(TorpedoMethodHandler fjpaMethodHandler, Class<T> realType) {
		methodHandler = fjpaMethodHandler;
		this.realType = realType;
	}

	/** {@inheritDoc} */
	@Override
	public T handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methodCalls) {

		MethodCall pollFirst = methodCalls.pollFirst();

		final QueryBuilder queryImpl = proxyQueryBuilders.get(pollFirst.getProxy());
		final Method thisMethod = pollFirst.getMethod().getJavaMethod();
		Class<?> returnType = thisMethod.getReturnType();

		if (Collection.class.isAssignableFrom(returnType)) {
			returnType = (Class<?>) ((ParameterizedType) thisMethod.getGenericReturnType()).getActualTypeArguments()[0];
		} else if (Map.class.isAssignableFrom(returnType)) {
			returnType = (Class<?>) ((ParameterizedType) thisMethod.getGenericReturnType()).getActualTypeArguments()[1];
		}

		try {

			Class<? extends Object> goodType = getGoodType(returnType);

			T join = TorpedoMagic.getProxyfactoryfactory().createProxy(methodHandler, goodType, TorpedoProxy.class);

			final QueryBuilder queryBuilder = methodHandler.addQueryBuilder(join, new DefaultQueryBuilder(goodType));

			queryImpl.addJoin(createJoin(queryBuilder, FieldUtils.getFieldName(pollFirst.getMethod())));

			return join;

		} catch (Exception e) {
			throw Throwables.propagate(e);
		}

	}

	private Class<? extends Object> getGoodType(Class<?> returnType) {
		return realType != null ? realType : returnType;
	}

	/**
	 * <p>
	 * createJoin.
	 * </p>
	 *
	 * @param queryBuilder
	 *            a {@link org.torpedoquery.core.QueryBuilder} object.
	 * @param fieldName
	 *            a {@link java.lang.String} object.
	 * @return a {@link org.torpedoquery.jpa.internal.Join} object.
	 */
	protected abstract Join createJoin(QueryBuilder queryBuilder, String fieldName);

}
