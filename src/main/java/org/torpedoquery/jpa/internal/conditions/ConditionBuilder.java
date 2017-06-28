
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
package org.torpedoquery.jpa.internal.conditions;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.core.QueryBuilder;
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
import org.torpedoquery.jpa.internal.conditions.LikeCondition.Type;
import org.torpedoquery.jpa.internal.selectors.NotSelector;
import org.torpedoquery.jpa.internal.selectors.SizeSelector;
public class ConditionBuilder<T> implements OnGoingComparableCondition<T>, OnGoingStringCondition<T>,
		OnGoingLikeCondition, OnGoingCollectionCondition<T>, Condition, Serializable {
	private Selector selector;
	private final LogicalCondition logicalCondition;
	private Condition condition;
	private boolean notLike;

	/**
	 * <p>
	 * Constructor for ConditionBuilder.
	 * </p>
	 *
	 * @param builder
	 *            a {@link org.torpedoquery.core.QueryBuilder} object.
	 * @param selector
	 *            a {@link org.torpedoquery.jpa.internal.Selector} object.
	 */
	public ConditionBuilder(QueryBuilder<T> builder, Selector<?> selector) {
		this.logicalCondition = new LogicalCondition(builder, this);
		this.selector = selector;
	}

	/**
	 * <p>
	 * Constructor for ConditionBuilder.
	 * </p>
	 *
	 * @param logicalCondition
	 *            a
	 *            {@link org.torpedoquery.jpa.internal.conditions.LogicalCondition}
	 *            object.
	 * @param selector
	 *            a {@link org.torpedoquery.jpa.internal.Selector} object.
	 */
	public ConditionBuilder(LogicalCondition logicalCondition, Selector<?> selector) {
		this.logicalCondition = logicalCondition;
		this.selector = selector;
	}

	/**
	 * <p>
	 * Getter for the field <code>logicalCondition</code>.
	 * </p>
	 *
	 * @return a
	 *         {@link org.torpedoquery.jpa.internal.conditions.LogicalCondition}
	 *         object.
	 */
	public LogicalCondition getLogicalCondition() {
		return logicalCondition;
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition eq(T value) {
		Condition conditionLocal = new EqualCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition eq(Class<? extends T> value) {
		Condition conditionLocal = new EqualPolymorphicCondition<T>(selector, value);
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition neq(T value) {
		Condition conditionLocal = new NotEqualCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition neq(Class<? extends T> value) {
		Condition conditionLocal = new NotEqualPolymorphicCondition<T>(selector, value);
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition lt(T value) {
		Condition conditionLocal = new LtCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition lte(T value) {
		Condition conditionLocal = new LteCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition gt(T value) {
		Condition conditionLocal = new GtCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition gte(T value) {
		Condition conditionLocal = new GteCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition isNull() {
		Condition conditionLocal = new IsNullCondition(selector);
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition isNotNull() {
		Condition conditionLocal = new IsNotNullCondition(selector);
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition in(T... values) {
		return in(Arrays.asList(values));
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition in(Collection<T> values) {
		Condition conditionLocal = new InCondition<T>(selector, selector.generateParameter(values));
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition in(Query<T> query) {
		Condition conditionLocal = new InSubQueryCondition<T>(selector, (QueryBuilder) query);
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition notIn(T... values) {
		return notIn(Arrays.asList(values));
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition notIn(Collection<T> values) {
		return getOnGoingLogicalCondition(new NotInCondition<T>(selector, selector.generateParameter(values)));
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition notIn(Query<T> subQuery) {
		return getOnGoingLogicalCondition(new NotInSubQueryCondition<T>(selector, (QueryBuilder) subQuery));
	}

	/** {@inheritDoc} */
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

	/** {@inheritDoc} */
	@Override
	public List<Parameter> getParameters() {
		if (condition != null) {
			return condition.getParameters();
		} else {
			return Collections.emptyList();
		}
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLikeCondition like() {
		return this;
	}
	
	@Override
	public OnGoingLogicalCondition like(String likeValue) {
		return getOnGoingLogicalCondition(createLike(LikeCondition.Type.UNKNOW, likeValue));
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLikeCondition notLike() {
		notLike = true;
		return this;
	}
	
	@Override
	public OnGoingLogicalCondition notLike(String notLikeValue) {
		notLike = true;
		return getOnGoingLogicalCondition(createLike(LikeCondition.Type.UNKNOW, notLikeValue));
	}

	private LikeCondition createLike(Type type, String toMatch) {
		if (notLike) {
			return new NotLikeCondition(type, selector, toMatch);
		} else {
			return new LikeCondition(type, selector, toMatch);
		}
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition any(String toMatch) {
		return getOnGoingLogicalCondition(createLike(LikeCondition.Type.ANY, toMatch));
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition startsWith(String toMatch) {
		return getOnGoingLogicalCondition(createLike(LikeCondition.Type.STARTSWITH, toMatch));
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition endsWith(String toMatch) {
		return getOnGoingLogicalCondition(createLike(LikeCondition.Type.ENDSWITH, toMatch));
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition isEmpty() {
		return getOnGoingLogicalCondition(new IsEmptyCondition(selector));
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition isNotEmpty() {
		return getOnGoingLogicalCondition(new IsNotEmptyCondition(selector));
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition memberOf(T member) {
		return getOnGoingLogicalCondition(new MemberOfCondition<>(selector, selector.generateParameter(member)));
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingComparableCondition<Integer> size() {
		selector = new SizeSelector(selector);
		return (OnGoingComparableCondition<Integer>) this;
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition lt(ComparableFunction<T> value) {
		Condition conditionLocal = new LtCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition lte(ComparableFunction<T> value) {
		Condition conditionLocal = new LteCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition gt(ComparableFunction<T> value) {
		Condition conditionLocal = new GtCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition gte(ComparableFunction<T> value) {
		Condition conditionLocal = new GteCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition eq(Function<T> value) {
		Condition conditionLocal = new EqualCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition neq(Function<T> value) {
		Condition conditionLocal = new NotEqualCondition<T>(selector, selector.generateParameter(value));
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition between(T from, T to) {
		Condition conditionLocal = new BetweenCondition<T>(selector,
				Arrays.asList(selector.generateParameter(from), selector.generateParameter(to)));
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition notBetween(T from, T to) {
		Condition conditionLocal = new BetweenCondition<T>(new NotSelector(selector),
				Arrays.asList(selector.generateParameter(from), selector.generateParameter(to)));
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition between(ComparableFunction<T> from, ComparableFunction<T> to) {
		Condition conditionLocal = new BetweenCondition<T>(selector,
				Arrays.asList(selector.generateParameter(from), selector.generateParameter(to)));
		return getOnGoingLogicalCondition(conditionLocal);
	}

	/** {@inheritDoc} */
	@Override
	public OnGoingLogicalCondition notBetween(ComparableFunction<T> from, ComparableFunction<T> to) {
		Condition conditionLocal = new BetweenCondition<T>(new NotSelector(selector),
				Arrays.asList(selector.generateParameter(from), selector.generateParameter(to)));
		return getOnGoingLogicalCondition(conditionLocal);
	}
	
	/** {@inheritDoc} */
	@Override
	public <T> QueryBuilder<T> getBuilder() {
		return logicalCondition.getBuilder();
	}
	
}
