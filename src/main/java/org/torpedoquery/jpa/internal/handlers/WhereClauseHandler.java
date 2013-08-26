/**
 *   Copyright Xavier Jodoin xjodoin@torpedoquery.org
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

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.OnGoingCondition;
import org.torpedoquery.jpa.internal.MethodCall;
import org.torpedoquery.jpa.internal.TorpedoProxy;
import org.torpedoquery.jpa.internal.QueryConfigurator;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.conditions.ConditionBuilder;
import org.torpedoquery.jpa.internal.conditions.LogicalCondition;
import org.torpedoquery.jpa.internal.utils.WhereQueryConfigurator;

//TODO duplicate avec WhereClauseCollectionHandler
public class WhereClauseHandler<T, E extends OnGoingCondition<T>> extends
		AbstractCallHandler<E> implements QueryHandler<E>, ValueHandler<E> {

	private final LogicalCondition logicalCondition;
	private final QueryConfigurator<T> configurator;
	private final Object param;

	public WhereClauseHandler(Object param) {
		this(param, null, new WhereQueryConfigurator<T>());
	}

	public WhereClauseHandler(Object param, QueryConfigurator<T> configurator) {
		this(param, null, configurator);
	}

	public WhereClauseHandler(Object param, LogicalCondition logicalCondition,
			QueryConfigurator<T> configurator) {
		this.param = param;
		this.logicalCondition = logicalCondition;
		this.configurator = configurator;
	}

	@Override
	public E handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders,
			Deque<MethodCall> methodCalls) {

		return handleValue(this, proxyQueryBuilders, methodCalls.iterator(),
				param);
	}

	@Override
	public E handle(TorpedoProxy proxy, QueryBuilder queryBuilder, Selector selector) {
		final ConditionBuilder<T> whereClause = logicalCondition != null ? new ConditionBuilder<T>(
				queryBuilder, logicalCondition, selector)
				: new ConditionBuilder<T>(queryBuilder, selector);

		configurator.configure(queryBuilder, whereClause);

		return (E) whereClause;
	}
}
