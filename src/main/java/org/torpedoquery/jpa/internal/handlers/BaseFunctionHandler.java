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
import org.torpedoquery.jpa.Function;
import org.torpedoquery.jpa.internal.MethodCall;
import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.TorpedoProxy;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.TorpedoMagic;
public abstract class BaseFunctionHandler<T, F extends Function<T>> extends AbstractCallHandler<F> implements QueryHandler<F>, ComparableFunction<T>, ValueHandler<F> {

	private Selector selector;
	private QueryBuilder<T> queryBuilder;
	private final Object value;
	private TorpedoProxy proxy;

	/**
	 * <p>Constructor for BaseFunctionHandler.</p>
	 *
	 * @param value a {@link java.lang.Object} object.
	 */
	public BaseFunctionHandler(Object value) {
		this.value = value;
	}

	/** {@inheritDoc} */
	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return String.format(getFunctionFormat(), selector.createQueryFragment(incrementor));
	}

	/** {@inheritDoc} */
	@Override
	public Object getProxy() {
		return proxy;
	}

	/** {@inheritDoc} */
	@Override
	public F handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methods) {
		return handleValue(this, proxyQueryBuilders, methods.iterator(), value);
	}

	/** {@inheritDoc} */
	@Override
	public F handle(TorpedoProxy proxy, QueryBuilder queryBuilder, Selector selector) {
		this.proxy = proxy;
		this.queryBuilder = queryBuilder;
		this.selector = selector;
		return (F) this;
	}

	/**
	 * <p>getFunctionFormat.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected abstract String getFunctionFormat();

	/** {@inheritDoc} */
	@Override
	public Parameter<T> generateParameter(T value) {
		return TorpedoMagic.getTorpedoMethodHandler().handle(new ParameterQueryHandler<T>("function",value));
	}

}
