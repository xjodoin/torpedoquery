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

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.Join;
import org.torpedoquery.jpa.internal.joins.LeftJoin;
import org.torpedoquery.jpa.internal.utils.ProxyFactoryFactory;
import org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler;
public class LeftJoinHandler<T> extends JoinHandler<T> {

	/**
	 * <p>Constructor for LeftJoinHandler.</p>
	 *
	 * @param methodHandler a {@link org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler} object.
	 */
	public LeftJoinHandler(TorpedoMethodHandler methodHandler) {
		super(methodHandler);
	}

	/**
	 * <p>Constructor for LeftJoinHandler.</p>
	 *
	 * @param fjpaMethodHandler a {@link org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler} object.
	 * @param realType a {@link java.lang.Class} object.
	 */
	public LeftJoinHandler(TorpedoMethodHandler fjpaMethodHandler, Class<T> realType) {
		super(fjpaMethodHandler, realType);
	}

	/** {@inheritDoc} */
	@Override
	protected Join createJoin(QueryBuilder queryBuilder, String fieldName) {
		return new LeftJoin(queryBuilder, fieldName);
	}

}
