
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
 *
 * @author xjodoin
 * @version $Id: $Id
 */
package org.torpedoquery.jpa.internal.handlers;

import java.util.Deque;
import java.util.Map;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.OnGoingLogicalCondition;
import org.torpedoquery.jpa.internal.MethodCall;
import org.torpedoquery.jpa.internal.QueryConfigurator;
import org.torpedoquery.jpa.internal.conditions.ConditionBuilder;
import org.torpedoquery.jpa.internal.conditions.GroupingCondition;
import org.torpedoquery.jpa.internal.conditions.LogicalCondition;

public class GroupingConditionHandler<T> implements QueryHandler<OnGoingLogicalCondition> {

	private final OnGoingLogicalCondition condition;
	private final QueryConfigurator<T> configurator;

	/**
	 * <p>
	 * Constructor for GroupingConditionHandler.
	 * </p>
	 *
	 * @param configurator
	 *            a {@link org.torpedoquery.jpa.internal.QueryConfigurator}
	 *            object.
	 * @param condition
	 *            a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	public GroupingConditionHandler(QueryConfigurator<T> configurator, OnGoingLogicalCondition condition) {
		this.configurator = configurator;
		this.condition = condition;
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders,
			Deque<MethodCall> methods) {
		if (condition instanceof LogicalCondition) {
			LogicalCondition logicalCondition = (LogicalCondition) condition;
			GroupingCondition groupingCondition = new GroupingCondition(logicalCondition);
			QueryBuilder<T> builder = logicalCondition.getBuilder();
			LogicalCondition newLogicalCondition = new LogicalCondition(builder, groupingCondition);
			configurator.configure(builder, new ConditionBuilder<T>(builder, newLogicalCondition, null));
			return newLogicalCondition;
		} else {
			return condition;
		}

	}
}
