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
import org.torpedoquery.jpa.ComparableFunction;
import org.torpedoquery.jpa.internal.MethodCall;
import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.query.SelectorParameter;
public class ComparableConstantFunctionHandler<T> implements ComparableFunction<T>, QueryHandler<ComparableFunction<T>> {

	private final T constant;

	/**
	 * <p>Constructor for ComparableConstantFunctionHandler.</p>
	 *
	 * @param constant a T object.
	 */
	public ComparableConstantFunctionHandler(T constant) {
		this.constant = constant;
	}

	/** {@inheritDoc} */
	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return constant.toString();
	}

	/** {@inheritDoc} */
	@Override
	public ComparableFunction<T> handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methods) {
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public Object getProxy() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public Parameter<T> generateParameter(T value) {
		return new SelectorParameter<>(this);
	}
}
