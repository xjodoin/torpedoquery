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

import java.util.concurrent.atomic.AtomicInteger;

public class SimpleMethodCallSelector<T> implements Selector<T> {

	private final MethodCall method;
	private final QueryBuilder<?> queryBuilder;

	public SimpleMethodCallSelector(QueryBuilder<?> queryBuilder, MethodCall method) {
		this.queryBuilder = queryBuilder;
		this.method = method;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return queryBuilder.getAlias(incrementor) + "." + method.getFullPath();
	}

	@Override
	public Parameter<T> generateParameter(T value) {
		return TorpedoMagic.getTorpedoMethodHandler().handle(new ParameterQueryHandler(method,value));
	}

}
