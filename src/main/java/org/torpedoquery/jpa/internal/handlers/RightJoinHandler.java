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

import org.torpedoquery.jpa.internal.Join;
import org.torpedoquery.jpa.internal.joins.RightJoin;
import org.torpedoquery.jpa.internal.query.QueryBuilder;
import org.torpedoquery.jpa.internal.utils.ProxyFactoryFactory;
import org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler;

public class RightJoinHandler<T> extends JoinHandler<T> {
	public RightJoinHandler(TorpedoMethodHandler methodHandler, ProxyFactoryFactory proxyFactoryFactory) {
		super(methodHandler, proxyFactoryFactory);
	}

	public RightJoinHandler(TorpedoMethodHandler fjpaMethodHandler, ProxyFactoryFactory proxyFactoryFactory, Class<T> realType) {
		super(fjpaMethodHandler, proxyFactoryFactory, realType);
	}

	@Override
	protected Join createJoin(QueryBuilder queryBuilder, String fieldName) {
		return new RightJoin(queryBuilder, fieldName);
	}
}
