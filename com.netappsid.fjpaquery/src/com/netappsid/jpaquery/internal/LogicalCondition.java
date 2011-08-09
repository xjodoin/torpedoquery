package com.netappsid.jpaquery.internal;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.OnGoingCollectionCondition;
import com.netappsid.jpaquery.OnGoingComparableCondition;
import com.netappsid.jpaquery.OnGoingCondition;
import com.netappsid.jpaquery.OnGoingLogicalCondition;
import com.netappsid.jpaquery.OnGoingStringCondition;

public class LogicalCondition implements OnGoingLogicalCondition, Condition {

	private Condition condition;

	public LogicalCondition(Condition condition) {
		this.condition = condition;
	}

	@Override
	public <T1> OnGoingCondition<T1> and(T1 property) {

		OnGoingCondition<T1> right = ConditionHelper.<T1, OnGoingCondition<T1>> createCondition(this);
		condition = new AndCondition(condition, (Condition) right);
		return right;
	}

	@Override
	public <T1> OnGoingCondition<T1> or(T1 property) {
		OnGoingCondition<T1> right = ConditionHelper.<T1, OnGoingCondition<T1>> createCondition(this);
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
		OnGoingCollectionCondition<T1> right = ConditionHelper.createCollectionCondition(this);
		condition = new AndCondition(condition, (Condition) right);
		return right;
	}

	@Override
	public <T1> OnGoingCollectionCondition<T1> or(Collection<T1> object) {
		OnGoingCollectionCondition<T1> right = ConditionHelper.createCollectionCondition(this);
		condition = new OrCondition(condition, (Condition) right);
		return right;
	}

}
