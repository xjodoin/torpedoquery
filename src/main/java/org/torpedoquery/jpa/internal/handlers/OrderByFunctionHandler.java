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

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.Function;
import org.torpedoquery.jpa.internal.MethodCall;
import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.selectors.SimpleMethodCallSelector;
public abstract class OrderByFunctionHandler<T> implements QueryHandler<Function<T>>, Function<T> {

	private MethodCall method;
	private Object proxy;
	private QueryBuilder queryBuilder;

	/** {@inheritDoc} */
	@Override
	public String createQueryFragment(AtomicInteger incrementor) {

		SimpleMethodCallSelector simpleMethodCallSelector = new SimpleMethodCallSelector(queryBuilder, method);
		return simpleMethodCallSelector.createQueryFragment(incrementor) + " " + getFunctionName();
	}

	/** {@inheritDoc} */
	@Override
	public Object getProxy() {
		return proxy;
	}

	/** {@inheritDoc} */
	@Override
	public Function<T> handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methods) {

		if (!methods.isEmpty()) {
			method = methods.pollFirst();
			proxy = method.getProxy();
			queryBuilder = proxyQueryBuilders.get(proxy);
		}

		return this;
	}

	/**
	 * <p>getFunctionName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected abstract String getFunctionName();

	/** {@inheritDoc} */
	@Override
	public Parameter generateParameter(Object value) {
		return null;
	}

}
