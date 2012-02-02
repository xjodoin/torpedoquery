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
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.Function;

public abstract class BaseFunctionHandler<T, F extends Function<T>> extends AbstractCallHandler<F> implements QueryHandler<F>, Function<T>, ValueHandler {

	private Selector selector;
	private QueryBuilder<T> queryBuilder;
	private final Object value;
	private Proxy proxy;

	public BaseFunctionHandler(Object value) {
		this.value = value;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {

		return getFunctionName() + "(" + selector.createQueryFragment(incrementor) + ")";
	}

	@Override
	public Object getProxy() {
		return proxy;
	}

	@Override
	public F handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methods) {

		return handleValue(this, proxyQueryBuilders, methods.iterator(), value);
	}

	@Override
	public F handle(Proxy proxy, QueryBuilder queryBuilder, Selector selector) {
		this.proxy = proxy;
		this.queryBuilder = queryBuilder;
		this.selector = selector;
		return (F) this;
	}

	protected abstract String getFunctionName();

	@Override
	public Parameter<T> generateParameter(T value) {
		return TorpedoMagic.getTorpedoMethodHandler().handle(new ParameterQueryHandler<T>("function",value));
	}

}
