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
package org.torpedoquery.jpa.internal.handlers;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.Function;
import org.torpedoquery.jpa.internal.MethodCall;
import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.Proxy;
import org.torpedoquery.jpa.internal.query.SelectorParameter;
import org.torpedoquery.jpa.internal.selectors.SimpleMethodCallSelector;

public class CountFunctionHandler implements Function<Long>, QueryHandler<Function<Long>> {

	private Proxy proxy;
	private MethodCall method;
	private QueryBuilder<?> queryBuilder;

	public CountFunctionHandler(Proxy proxy) {
		this.proxy = proxy;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		if (method != null) {
			SimpleMethodCallSelector simpleMethodCallSelector = new SimpleMethodCallSelector(queryBuilder, method);
			return "count(" + simpleMethodCallSelector.createQueryFragment(incrementor) + ")";
		} else {
			return "count(*)";
		}
	}

	@Override
	public Function<Long> handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methods) {

		if (proxy == null && !methods.isEmpty()) {
			method = methods.pollFirst();
			proxy = method.getProxy();
			queryBuilder = proxyQueryBuilders.get(proxy);
		}

		return this;
	}

	@Override
	public Object getProxy() {
		return proxy;
	}

	@Override
	public Parameter<Long> generateParameter(Long value) {
		return new SelectorParameter<Long>(this);
	}
}
