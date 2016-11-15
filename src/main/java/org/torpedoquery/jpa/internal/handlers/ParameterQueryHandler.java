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

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.Function;
import org.torpedoquery.jpa.Query;
import org.torpedoquery.jpa.internal.MethodCall;
import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.query.SelectorParameter;
import org.torpedoquery.jpa.internal.query.SubqueryValueParameters;
import org.torpedoquery.jpa.internal.query.ValueParameter;
import org.torpedoquery.jpa.internal.selectors.SimpleMethodCallSelector;
public class ParameterQueryHandler<T> implements QueryHandler<Parameter<T>> {

	private final T value;
	private final String paramName;

	/**
	 * <p>Constructor for ParameterQueryHandler.</p>
	 *
	 * @param paramName a {@link java.lang.String} object.
	 * @param value a T object.
	 */
	public ParameterQueryHandler(String paramName, T value) {
		this.paramName = paramName;
		this.value = value;
	}

	/** {@inheritDoc} */
	@Override
	public Parameter<T> handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methods) {

		if (!methods.isEmpty()) {
			MethodCall pollFirst = methods.pollFirst();
			return new SelectorParameter<>(new SimpleMethodCallSelector<T>(proxyQueryBuilders.get(pollFirst.getProxy()), pollFirst));
		}
        else if (value instanceof Query) {
            return new SubqueryValueParameters<>((Query) value);
        }
		else if (value instanceof Function) {
			return new SelectorParameter<>((Selector) value);
		}
		else {
			return new ValueParameter<>(paramName, value);
		}
	}

}
