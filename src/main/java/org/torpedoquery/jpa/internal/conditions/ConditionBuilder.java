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
package org.torpedoquery.jpa.internal.conditions;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.ComparableFunction;
import org.torpedoquery.jpa.Function;
import org.torpedoquery.jpa.OnGoingCollectionCondition;
import org.torpedoquery.jpa.OnGoingComparableCondition;
import org.torpedoquery.jpa.OnGoingLikeCondition;
import org.torpedoquery.jpa.OnGoingLogicalCondition;
import org.torpedoquery.jpa.OnGoingStringCondition;
import org.torpedoquery.jpa.Query;
import org.torpedoquery.jpa.internal.Condition;
import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.query.QueryBuilder;
import org.torpedoquery.jpa.internal.selectors.NotSelector;
import org.torpedoquery.jpa.internal.selectors.SizeSelector;

public class ConditionBuilder<T> implements OnGoingComparableCondition<T>, OnGoingStringCondition<T>, OnGoingLikeCondition,
		OnGoingCollectionCondition<T>, Condition {
	private Selector selector;
	private final LogicalCondition logicalCondition;
	private Condition condition;
	private final QueryBuilder<T> builder;

	public ConditionBuilder(QueryBuilder<T> builder, Selector<?> selector) {
		this.builder = builder;
		this.logicalCondition = new LogicalCondition(builder, this);
		this.selector = selector;
	}

	public ConditionBuilder(QueryBuilder<T> builder, LogicalCondition logicalCondition, Selector<?> selector) {
		this.builder = builder;
		this.logicalCondition = logicalCondition;
		this.selector = selector;
	}

	public LogicalCondition getLogicalCondition() {
		return logicalCondition;
	}

	@Override
	public OnGoingLogicalCondition eq(T value) {
		Condition condition = new EqualCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition eq(Class<? extends T> value) {
		Condition condition = new EqualPolymorphicCondition<T>(selector, value);
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition neq(T value) {
		Condition condition = new NotEqualCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition neq(Class<? extends T> value) {
		Condition condition = new NotEqualPolymorphicCondition<T>(selector, value);
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition lt(T value) {
		Condition condition = new LtCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition lte(T value) {
		Condition condition = new LteCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition gt(T value) {
		Condition condition = new GtCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition gte(T value) {
		Condition condition = new GteCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition isNull() {
		Condition condition = new IsNullCondition(selector);
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition isNotNull() {
		Condition condition = new IsNotNullCondition(selector);
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition in(T... values) {
		return in(Arrays.asList(values));
	}

	@Override
	public OnGoingLogicalCondition in(Collection<T> values) {
		Condition condition = new InCondition<T>(selector, selector.generateParameter(values));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition in(Query<T> query) {
		Condition condition = new InSubQueryCondition<T>(selector, (QueryBuilder) query);
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition notIn(T... values) {
		return getOnGoingLogicalCondition(new NotInCondition<T>(selector, selector.generateParameter(values)));
	}

	@Override
	public OnGoingLogicalCondition notIn(Collection<T> values) {
		return getOnGoingLogicalCondition(new NotInCondition<T>(selector, selector.generateParameter(values)));
	}

	@Override
	public OnGoingLogicalCondition notIn(Query<T> subQuery) {
		return getOnGoingLogicalCondition(new NotInSubQueryCondition<T>(selector, (QueryBuilder) subQuery));
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		if (condition != null) {
			return condition.createQueryFragment(incrementor);
		} else {
			return "";
		}

	}

	private OnGoingLogicalCondition getOnGoingLogicalCondition(Condition condition) {
		this.condition = condition;
		return logicalCondition;
	}

	@Override
	public List<Parameter> getParameters() {
		if (condition != null) {
			return condition.getParameters();
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public OnGoingLikeCondition like() {
		return this;
	}

	@Override
	public OnGoingLogicalCondition any(String toMatch) {
		return getOnGoingLogicalCondition(new LikeCondition(LikeCondition.Type.ANY, selector, toMatch));
	}

	@Override
	public OnGoingLogicalCondition startsWith(String toMatch) {
		return getOnGoingLogicalCondition(new LikeCondition(LikeCondition.Type.STARTSWITH, selector, toMatch));
	}

	@Override
	public OnGoingLogicalCondition endsWith(String toMatch) {
		return getOnGoingLogicalCondition(new LikeCondition(LikeCondition.Type.ENDSWITH, selector, toMatch));
	}

	@Override
	public OnGoingLogicalCondition isEmpty() {
		return getOnGoingLogicalCondition(new IsEmptyCondition(selector));
	}

	@Override
	public OnGoingLogicalCondition isNotEmpty() {
		return getOnGoingLogicalCondition(new IsNotEmptyCondition(selector));
	}

	@Override
	public OnGoingComparableCondition<Integer> size() {
		selector = new SizeSelector(selector);
		return (OnGoingComparableCondition<Integer>) this;
	}

	@Override
	public OnGoingLogicalCondition lt(ComparableFunction<T> value) {
		Condition condition = new LtCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition lte(ComparableFunction<T> value) {
		Condition condition = new LteCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition gt(ComparableFunction<T> value) {
		Condition condition = new GtCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition gte(ComparableFunction<T> value) {
		Condition condition = new GteCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition eq(Function<T> value) {
		Condition condition = new EqualCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition neq(Function<T> value) {
		Condition condition = new NotEqualCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition between(T from, T to) {
		Condition condition = new BetweenCondition<T>(selector, Arrays.asList(selector.generateParameter(from), selector.generateParameter(to)));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition notBetween(T from, T to) {
		Condition condition = new BetweenCondition<T>(new NotSelector(selector), Arrays.asList(selector.generateParameter(from),
				selector.generateParameter(to)));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition between(ComparableFunction<T> from,
			ComparableFunction<T> to) {
		Condition condition = new BetweenCondition<T>(selector, Arrays.asList(selector.generateParameter(from), selector.generateParameter(to)));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition notBetween(ComparableFunction<T> from,
			ComparableFunction<T> to) {
		Condition condition = new BetweenCondition<T>(new NotSelector(selector), Arrays.asList(selector.generateParameter(from),
				selector.generateParameter(to)));
		return getOnGoingLogicalCondition(condition);
	}
}
