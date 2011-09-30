package org.torpedoquery.jpa.internal;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.OnGoingCollectionCondition;
import org.torpedoquery.jpa.OnGoingComparableCondition;
import org.torpedoquery.jpa.OnGoingLogicalCondition;
import org.torpedoquery.jpa.OnGoingStringCondition;
import org.torpedoquery.jpa.ValueOnGoingCondition;

public class LogicalCondition implements OnGoingLogicalCondition, Condition {

	private Condition condition;
	private final QueryBuilder<?> builder;

	public LogicalCondition(QueryBuilder<?> builder, Condition condition) {
		this.builder = builder;
		this.condition = condition;
	}

	@Override
	public <T1> ValueOnGoingCondition<T1> and(T1 property) {

		ValueOnGoingCondition<T1> right = ConditionHelper.<T1, ValueOnGoingCondition<T1>> createCondition(this);
		condition = new AndCondition(condition, (Condition) right);
		return right;
	}

	@Override
	public <T1> ValueOnGoingCondition<T1> or(T1 property) {
		ValueOnGoingCondition<T1> right = ConditionHelper.<T1, ValueOnGoingCondition<T1>> createCondition(this);
		condition = new OrCondition(condition, (Condition) right);
		return right;
	}

	@Override
	public <V, T extends Comparable<V>> OnGoingComparableCondition<V> and(T property) {
		OnGoingComparableCondition<V> right = ConditionHelper.<V, OnGoingComparableCondition<V>> createCondition(this);
		condition = new AndCondition(condition, (Condition) right);
		return right;
	}

	@Override
	public <V, T extends Comparable<V>> OnGoingComparableCondition<V> or(T property) {
		OnGoingComparableCondition<V> right = ConditionHelper.<V, OnGoingComparableCondition<V>> createCondition(this);
		condition = new OrCondition(condition, (Condition) right);
		return right;
	}

	@Override
	public List<Parameter> getParameters() {
		return condition.getParameters();
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return condition.createQueryFragment(incrementor);
	}

	@Override
	public OnGoingLogicalCondition and(OnGoingLogicalCondition condition) {
		this.condition = new AndCondition(this.condition, new GroupingCondition((Condition) condition));
		return this;
	}

	@Override
	public OnGoingLogicalCondition or(OnGoingLogicalCondition condition) {
		this.condition = new OrCondition(this.condition, new GroupingCondition((Condition) condition));
		return this;
	}

	@Override
	public OnGoingStringCondition<String> and(String property) {
		OnGoingStringCondition<String> right = ConditionHelper.<String, OnGoingStringCondition<String>> createCondition(this);
		condition = new AndCondition(condition, (Condition) right);
		return right;
	}

	@Override
	public OnGoingStringCondition<String> or(String property) {
		OnGoingStringCondition<String> right = ConditionHelper.<String, OnGoingStringCondition<String>> createCondition(this);
		condition = new OrCondition(condition, (Condition) right);
		return right;
	}

	@Override
	public <T1> OnGoingCollectionCondition<T1> and(Collection<T1> object) {
		OnGoingCollectionCondition<T1> right = ConditionHelper.<T1, OnGoingCollectionCondition<T1>> createCondition(this);
		condition = new AndCondition(condition, (Condition) right);
		return right;
	}

	@Override
	public <T1> OnGoingCollectionCondition<T1> or(Collection<T1> object) {
		OnGoingCollectionCondition<T1> right = ConditionHelper.<T1, OnGoingCollectionCondition<T1>> createCondition(this);
		condition = new OrCondition(condition, (Condition) right);
		return right;
	}

	public QueryBuilder<?> getBuilder() {
		return builder;
	}

}
