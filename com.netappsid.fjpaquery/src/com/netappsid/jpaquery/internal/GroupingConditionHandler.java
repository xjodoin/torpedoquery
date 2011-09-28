package com.netappsid.jpaquery.internal;

import java.util.Deque;
import java.util.Map;

import com.netappsid.jpaquery.OnGoingLogicalCondition;

public class GroupingConditionHandler<T> implements QueryHandler<OnGoingLogicalCondition> {

	private final LogicalCondition condition;
	private final QueryConfigurator<T> configurator;

	public GroupingConditionHandler(QueryConfigurator<T> configurator, OnGoingLogicalCondition condition) {
		this.configurator = configurator;
		this.condition = (LogicalCondition) condition;
	}

	@Override
	public OnGoingLogicalCondition handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, Deque<MethodCall> methods) {
		GroupingCondition groupingCondition = new GroupingCondition(condition);

		QueryBuilder<T> builder = (QueryBuilder<T>) condition.getBuilder();
		LogicalCondition logicalCondition = new LogicalCondition(builder, groupingCondition);

		configurator.configure(builder, new ConditionBuilder<T>(builder, logicalCondition, null));

		return logicalCondition;
	}
}
