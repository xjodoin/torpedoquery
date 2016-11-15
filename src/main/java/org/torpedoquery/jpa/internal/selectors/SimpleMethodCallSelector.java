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
package org.torpedoquery.jpa.internal.selectors;

import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.MethodCall;
import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.TorpedoMagic;
import org.torpedoquery.jpa.internal.handlers.ParameterQueryHandler;
public class SimpleMethodCallSelector<T> implements Selector<T> {

	private final MethodCall method;
	private final QueryBuilder<?> queryBuilder;

	/**
	 * <p>Constructor for SimpleMethodCallSelector.</p>
	 *
	 * @param queryBuilder a {@link org.torpedoquery.core.QueryBuilder} object.
	 * @param method a {@link org.torpedoquery.jpa.internal.MethodCall} object.
	 */
	public SimpleMethodCallSelector(QueryBuilder<?> queryBuilder, MethodCall method) {
		this.queryBuilder = queryBuilder;
		this.method = method;
	}

	/** {@inheritDoc} */
	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return queryBuilder.getAlias(incrementor) + "." + method.getFullPath();
	}

	/** {@inheritDoc} */
	@Override
	public Parameter<T> generateParameter(T value) {
		return TorpedoMagic.getTorpedoMethodHandler().handle(new ParameterQueryHandler<T>(method.getParamName(),value));
	}

}
