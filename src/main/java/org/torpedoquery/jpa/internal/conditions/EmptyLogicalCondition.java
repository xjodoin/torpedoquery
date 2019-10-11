package org.torpedoquery.jpa.internal.conditions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.ComparableFunction;
import org.torpedoquery.jpa.Function;
import org.torpedoquery.jpa.OnGoingCollectionCondition;
import org.torpedoquery.jpa.OnGoingComparableCondition;
import org.torpedoquery.jpa.OnGoingLogicalCondition;
import org.torpedoquery.jpa.OnGoingStringCondition;
import org.torpedoquery.jpa.Torpedo;
import org.torpedoquery.jpa.ValueOnGoingCondition;
import org.torpedoquery.jpa.internal.Condition;
import org.torpedoquery.jpa.internal.ConditionVisitor;
import org.torpedoquery.jpa.internal.Parameter;

/**
 * <p>EmptyLogicalCondition class.</p>
 *
 * @author xjodoin
 * @version $Id: $Id
 */
public class EmptyLogicalCondition implements OnGoingLogicalCondition, Condition {

	private Condition delegate;
	
	/** {@inheritDoc} */
	@Override
	public <T> ValueOnGoingCondition<T> and(T property) {
		ValueOnGoingCondition<T> condition = Torpedo.condition(property);
		delegate = (Condition) condition;
		return condition;
	}

	/** {@inheritDoc} */
	@Override
	public <V, T extends Comparable<V>> OnGoingComparableCondition<V> and(T property) {
		OnGoingComparableCondition<V> condition = Torpedo.condition(property);
		delegate = (Condition) condition;
		return condition;
	}

	/** {@inheritDoc} */
	@Override
	public <T> ValueOnGoingCondition<T> or(T property) {
		ValueOnGoingCondition<T> condition = Torpedo.condition(property);
		delegate = (Condition) condition;
		return condition;
	}

	/** {@inheritDoc} */
	@Override
	public <V, T extends Comparable<V>> OnGoingComparableCondition<V> or(T property) {
		OnGoingComparableCondition<V> condition = Torpedo.condition(property);
		delegate = (Condition) condition;
		return condition;
	}

	/** {@inheritDoc} */
	@Override
	public <V, T extends Comparable<V>> OnGoingComparableCondition<V> and(ComparableFunction<T> property) {
		OnGoingComparableCondition<V> condition = Torpedo.condition(property);
		delegate = (Condition) condition;
		return condition;
	}

	/** {@inheritDoc} */
	@Override
	public <V, T extends Comparable<V>> OnGoingComparableCondition<V> or(ComparableFunction<T> property) {
		OnGoingComparableCondition<V> condition = Torpedo.condition(property);
		delegate = (Condition) condition;
		return condition;
	}

	/** {@inheritDoc} */
	@Override
	public <T> OnGoingCollectionCondition<T> and(Collection<T> object) {
		OnGoingCollectionCondition<T> condition = Torpedo.condition(object);
		delegate = (Condition) condition;
		return condition;
	}

	/** {@inheritDoc} */
	@Override
	public <T> OnGoingCollectionCondition<T> or(Collection<T> object) {
		OnGoingCollectionCondition<T> condition = Torpedo.condition(object);
		delegate = (Condition) condition;
		return condition;
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingStringCondition<String> and(String property) {
		OnGoingStringCondition<String> condition = Torpedo.condition(property);
		delegate = (Condition) condition;
		return condition;
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingStringCondition<String> and(Function<String> function) {
		OnGoingStringCondition<String> condition = Torpedo.condition(function);
		delegate = (Condition) condition;
		return condition;
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingStringCondition<String> or(String property) {
		OnGoingStringCondition<String> condition = Torpedo.condition(property);
		delegate = (Condition) condition;
		return condition;
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingStringCondition<String> or(Function<String> function) {
		OnGoingStringCondition<String> condition = Torpedo.condition(function);
		delegate = (Condition) condition;
		return condition;
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition and(OnGoingLogicalCondition param) {
		OnGoingLogicalCondition condition = Torpedo.condition(param);
		delegate = (Condition) condition;
		return condition;
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition or(OnGoingLogicalCondition param) {
		OnGoingLogicalCondition condition = Torpedo.condition(param);
		delegate = (Condition) condition;
		return condition;
	}

	/** {@inheritDoc} */
	@Override
	public List<Parameter> getParameters() {
		if(delegate != null) {
			return delegate.getParameters();
		}
		else {
			return Collections.emptyList();
		}
		
	}
	
	/** {@inheritDoc} */
	@Override
	public <T> QueryBuilder<T> getBuilder() {
		if(delegate != null) {
			return delegate.getBuilder();
		}
		else {
			return null;
		}
	}
	
	@Override
	public <T> T accept(ConditionVisitor<T> visitior) {
		return visitior.visit(this);
	}

}
