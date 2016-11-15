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

import java.util.Iterator;
import java.util.Map;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.Function;
import org.torpedoquery.jpa.Query;
import org.torpedoquery.jpa.internal.MethodCall;
import org.torpedoquery.jpa.internal.TorpedoProxy;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.selectors.ObjectSelector;
import org.torpedoquery.jpa.internal.selectors.SimpleMethodCallSelector;
public abstract class AbstractCallHandler<T> {

	/**
	 * <p>handleValue.</p>
	 *
	 * @param valueHandler a {@link org.torpedoquery.jpa.internal.handlers.ValueHandler} object.
	 * @param proxyQueryBuilders a {@link java.util.Map} object.
	 * @param iterator a {@link java.util.Iterator} object.
	 * @param param a {@link java.lang.Object} object.
	 * @return a T object.
	 */
	public T handleValue(ValueHandler<T> valueHandler,
			Map<Object, QueryBuilder<?>> proxyQueryBuilders,
			Iterator<MethodCall> iterator, Object param) {
		TorpedoProxy proxy;
		QueryBuilder queryBuilder;
		Selector selector;

		if (param instanceof Query) {
			Query query = (Query) param;
			selector = query;
			queryBuilder = (QueryBuilder) param;
			proxy = (TorpedoProxy) query.getProxy();
		} else if (param instanceof Function) {
			Function function = (Function) param;
			selector = function;
			proxy = (TorpedoProxy) function.getProxy();
			queryBuilder = proxyQueryBuilders.get(proxy);

		} else if (param instanceof TorpedoProxy) {
			proxy = (TorpedoProxy) param;
			queryBuilder = proxyQueryBuilders.get(proxy);
			selector = new ObjectSelector(queryBuilder);
		} else {

			MethodCall methodCall = iterator.next();
			iterator.remove();
			proxy = methodCall.getProxy();
			queryBuilder = proxyQueryBuilders.get(proxy);
			selector = new SimpleMethodCallSelector(queryBuilder, methodCall);
		}

		return valueHandler.handle(proxy, queryBuilder, selector);
	}

}
