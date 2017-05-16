package org.torpedoquery.jpa.internal.conditions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.ComparableFunction;
import org.torpedoquery.jpa.Function;
import org.torpedoquery.jpa.OnGoingCollectionCondition;
import org.torpedoquery.jpa.OnGoingComparableCondition;
import org.torpedoquery.jpa.OnGoingLogicalCondition;
import org.torpedoquery.jpa.OnGoingStringCondition;
import org.torpedoquery.jpa.ValueOnGoingCondition;
import org.torpedoquery.jpa.internal.Condition;
import org.torpedoquery.jpa.internal.Parameter;

public class EmptyLogicalCondition implements OnGoingLogicalCondition, Condition {

	@Override
	public <T> ValueOnGoingCondition<T> and(T property) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <V, T extends Comparable<V>> OnGoingComparableCondition<V> and(T property) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> ValueOnGoingCondition<T> or(T property) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <V, T extends Comparable<V>> OnGoingComparableCondition<V> or(T property) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> OnGoingComparableCondition<T> and(ComparableFunction<T> property) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> OnGoingComparableCondition<T> or(ComparableFunction<T> property) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> OnGoingCollectionCondition<T> and(Collection<T> object) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> OnGoingCollectionCondition<T> or(Collection<T> object) {
		throw new UnsupportedOperationException();
	}

	@Override
	public OnGoingStringCondition<String> and(String property) {
		throw new UnsupportedOperationException();
	}

	@Override
	public OnGoingStringCondition<String> and(Function<String> function) {
		throw new UnsupportedOperationException();
	}

	@Override
	public OnGoingStringCondition<String> or(String property) {
		throw new UnsupportedOperationException();
	}

	@Override
	public OnGoingStringCondition<String> or(Function<String> function) {
		throw new UnsupportedOperationException();
	}

	@Override
	public OnGoingLogicalCondition and(OnGoingLogicalCondition condition) {
		throw new UnsupportedOperationException();
	}

	@Override
	public OnGoingLogicalCondition or(OnGoingLogicalCondition condition) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return "";
	}

	@Override
	public List<Parameter> getParameters() {
		return Collections.emptyList();
	}

}
