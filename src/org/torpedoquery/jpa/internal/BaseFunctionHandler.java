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

public abstract class BaseFunctionHandler<T, F extends Function<T>> implements QueryHandler<F>, Function<T> {

	private MethodCall method;
	private Object proxy;
	private QueryBuilder<T> queryBuilder;

	public BaseFunctionHandler() {
	}

	public BaseFunctionHandler(Object proxy) {
		this.proxy = proxy;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {

		if (method != null) {
			SimpleMethodCallSelector<T> simpleMethodCallSelector = new SimpleMethodCallSelector<T>(queryBuilder, method);
			return getFunctionName() + "(" + simpleMethodCallSelector.createQueryFragment(incrementor) + ")";
		} else {
			return getFunctionName() + "(" + TorpedoMagic.getTorpedoMethodHandler().getQueryBuilder(proxy).getAlias(incrementor) + ")";
		}
	}

	@Override
	public Object getProxy() {
		return proxy;
	}

	@Override
	public F handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methods) {

		if (proxy == null) {
			method = methods.pollFirst();
			proxy = method.getProxy();
			queryBuilder = (QueryBuilder<T>) proxyQueryBuilders.get(proxy);
		}

		return (F) this;
	}

	protected abstract String getFunctionName();

	@Override
	public Parameter<T> generateParameter(T value) {

		if (value instanceof Function) {
			return new SelectorParameter<T>((Selector) value);
		} else {
			return new ValueParameter<T>("function", value);
		}
	}

}
