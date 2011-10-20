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

public class ParameterQueryHandler<T> implements QueryHandler<Parameter<T>> {

	private final MethodCall method;
	private final T value;

	public ParameterQueryHandler(MethodCall method, T value) {
		this.method = method;
		this.value = value;
	}

	@Override
	public Parameter<T> handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methods) {

		if (!methods.isEmpty()) {
			MethodCall pollFirst = methods.pollFirst();
			return new SelectorParameter<T>(new SimpleMethodCallSelector<T>(proxyQueryBuilders.get(pollFirst.getProxy()), pollFirst));
		} else {
			return new ValueParameter<T>(method.getParamName(), value);
		}
	}

}
