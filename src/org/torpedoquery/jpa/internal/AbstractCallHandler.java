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

import java.util.Iterator;
import java.util.Map;

import org.torpedoquery.jpa.Function;

public abstract class AbstractCallHandler {

	public void handleValue(ValueHandler valueHandler, Map<Object, QueryBuilder<?>> proxyQueryBuilders, Iterator<MethodCall> iterator, Object param) {
		Proxy proxy;
		QueryBuilder queryBuilder;
		Selector selector;

		if (param instanceof Function) {
			Function function = (Function) param;
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

		valueHandler.handle(proxy, queryBuilder, selector);
	}
	
}
