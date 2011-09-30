/**
 *
 *   Copyright 2011 Xavier Jodoin xjodoin@gmail.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.torpedoquery.jpa.internal;

import java.util.Deque;
import java.util.Iterator;
import java.util.Map;

import org.torpedoquery.jpa.Function;

public class ArrayCallHandler implements QueryHandler<Void> {

	public static interface ValueHandler {
		void handle(Proxy proxy, QueryBuilder queryBuilder, Selector selector);
	}

	private final Object[] values;
	private final ValueHandler handler;

	public ArrayCallHandler(ValueHandler handler, Object[] values) {
		this.handler = handler;
		this.values = values;
	}

	@Override
	public Void handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methodCalls) {

		Iterator<MethodCall> iterator = methodCalls.descendingIterator();

		Proxy proxy = null;

		for (int i = 0; i < values.length; i++) {

			Object param = values[i];
			QueryBuilder queryBuilder;
			Selector selector;

			if (param instanceof Function) {
				Function function = (Function) values[i];
				selector = function;
				proxy = (Proxy) function.getProxy();
				queryBuilder = proxyQueryBuilders.get(proxy);

			} else if (param instanceof Proxy) {
				proxy = (Proxy) param;
				queryBuilder = proxyQueryBuilders.get(proxy);
				selector = new ObjectSelector(queryBuilder);
			} else {

				MethodCall methodCall = iterator.next();
				iterator.remove();
				proxy = methodCall.getProxy();
				queryBuilder = proxyQueryBuilders.get(proxy);
				selector = new SimpleMethodCallSelector(queryBuilder, methodCall);
			}

			handler.handle(proxy, queryBuilder, selector);
		}
		return null;

	}
}
