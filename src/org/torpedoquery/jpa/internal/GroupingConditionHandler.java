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

import org.torpedoquery.jpa.OnGoingLogicalCondition;

public class GroupingConditionHandler<T> implements QueryHandler<OnGoingLogicalCondition> {

	private final LogicalCondition condition;
	private final QueryConfigurator<T> configurator;

	public GroupingConditionHandler(QueryConfigurator<T> configurator, OnGoingLogicalCondition condition) {
		this.configurator = configurator;
		this.condition = (LogicalCondition) condition;
	}

	@Override
	public OnGoingLogicalCondition handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methods) {
		GroupingCondition groupingCondition = new GroupingCondition(condition);

		QueryBuilder<T> builder = (QueryBuilder<T>) condition.getBuilder();
		LogicalCondition logicalCondition = new LogicalCondition(builder, groupingCondition);

		configurator.configure(builder, new ConditionBuilder<T>(builder, logicalCondition, null));

		return logicalCondition;
	}
}
