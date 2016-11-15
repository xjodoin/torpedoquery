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
import java.util.Iterator;
import java.util.Map;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.MethodCall;
import org.torpedoquery.jpa.internal.TorpedoProxy;
public class ArrayCallHandler extends AbstractCallHandler implements QueryHandler<Void> {

	private final Object[] values;
	private final ValueHandler handler;

	/**
	 * <p>Constructor for ArrayCallHandler.</p>
	 *
	 * @param handler a {@link org.torpedoquery.jpa.internal.handlers.ValueHandler} object.
	 * @param values an array of {@link java.lang.Object} objects.
	 */
	public ArrayCallHandler(ValueHandler handler, Object[] values) {
		this.handler = handler;
		this.values = values;
	}

	/** {@inheritDoc} */
	@Override
	public Void handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methodCalls) {

		Iterator<MethodCall> iterator = methodCalls.descendingIterator();

		TorpedoProxy proxy = null;

		for (int i = 0; i < values.length; i++) {

			Object param = values[i];
			handleValue(handler,proxyQueryBuilders, iterator, param);
		}
		return null;

	}

}
