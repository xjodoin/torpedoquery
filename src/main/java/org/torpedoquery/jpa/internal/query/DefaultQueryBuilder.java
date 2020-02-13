
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
package org.torpedoquery.jpa.internal.query;

import static org.torpedoquery.jpa.internal.conditions.ConditionHelper.getConditionClause;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.Entity;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.OnGoingLogicalCondition;
import org.torpedoquery.jpa.internal.Condition;
import org.torpedoquery.jpa.internal.Join;
import org.torpedoquery.jpa.internal.conditions.ConditionBuilder;
public class DefaultQueryBuilder<T> implements QueryBuilder<T> {
	private final Class<?> toQuery;
	private final List<Join> joins = new ArrayList<>();
	private ConditionBuilder<T> whereClause;
	private ConditionBuilder<T> withClause;

	private String alias;
	private OrderBy orderBy;
	private GroupBy groupBy;

	/**
	 * <p>
	 * Constructor for DefaultQueryBuilder.
	 * </p>
	 *
	 * @param toQuery a {@link java.lang.Class} object.
	 */
	public DefaultQueryBuilder(Class<?> toQuery) {
		this.toQuery = toQuery;
	}

	/** {@inheritDoc} */
	@Override
	public String getEntityName() {

		Entity e = toQuery.getAnnotation(Entity.class);
		if (e != null && e.name() != null && !e.name().trim().isEmpty())
			return e.name();
		else
			return toQuery.getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.torpedoquery.jpa.internal.query.QueryBuilder#appendOrderBy(java.lang.
	 * StringBuilder, java.util.concurrent.atomic.AtomicInteger)
	 */
	/** {@inheritDoc} */
	@Override
	public String appendOrderBy(StringBuilder builder, AtomicInteger incrementor) {

		if (orderBy != null) {
			orderBy.createQueryFragment(builder, this, incrementor);
		}

		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.torpedoquery.jpa.internal.query.QueryBuilder#appendGroupBy(java.lang.
	 * StringBuilder, java.util.concurrent.atomic.AtomicInteger)
	 */
	/** {@inheritDoc} */
	@Override
	public String appendGroupBy(StringBuilder builder, AtomicInteger incrementor) {

		if (groupBy != null) {
			groupBy.createQueryFragment(builder, incrementor);
		}

		for (Join join : joins) {
			join.appendGroupBy(builder, incrementor);
		}

		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.torpedoquery.jpa.internal.query.QueryBuilder#appendWhereClause(java.
	 * lang.StringBuilder, java.util.concurrent.atomic.AtomicInteger)
	 */
	/** {@inheritDoc} */
	@Override
	public StringBuilder appendWhereClause(StringBuilder builder, AtomicInteger incrementor) {

		Condition whereClauseCondition = getConditionClause(whereClause);

		if (whereClauseCondition != null) {
			if (builder.length() == 0) {
				builder.append(" where ").append(whereClauseCondition.createQueryFragment(incrementor)).append(' ');
			} else {
				builder.append("and ").append(whereClauseCondition.createQueryFragment(incrementor)).append(' ');
			}
		}

		for (Join join : joins) {
			join.appendWhereClause(builder, incrementor);
		}

		return builder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.torpedoquery.jpa.internal.query.QueryBuilder#getAlias(java.util.
	 * concurrent.atomic.AtomicInteger)
	 */
	/** {@inheritDoc} */
	@Override
	public String getAlias(AtomicInteger incrementor) {
		if (alias == null) {
			final char[] charArray = getEntityName().toCharArray();

			charArray[0] = Character.toLowerCase(charArray[0]);
			alias = new String(charArray) + "_" + incrementor.getAndIncrement();
		}
		return alias;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.torpedoquery.jpa.internal.query.QueryBuilder#addJoin(org.torpedoquery
	 * .jpa.internal.Join)
	 */
	/** {@inheritDoc} */
	@Override
	public void addJoin(Join innerJoin) {
		joins.add(innerJoin);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.torpedoquery.jpa.internal.query.QueryBuilder#hasSubJoin()
	 */
	/** {@inheritDoc} */
	@Override
	public boolean hasSubJoin() {
		return !joins.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.torpedoquery.jpa.internal.query.QueryBuilder#getJoins(java.util.
	 * concurrent.atomic.AtomicInteger)
	 */
	/** {@inheritDoc} */
	@Override
	public String getJoins(AtomicInteger incrementor) {

		StringBuilder builder = new StringBuilder();

		for (Join join : joins) {
			builder.append(join.getJoin(getAlias(incrementor), incrementor));
		}

		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.torpedoquery.jpa.internal.query.QueryBuilder#setWhereClause(org.
	 * torpedoquery.jpa.internal.conditions.ConditionBuilder)
	 */
	/** {@inheritDoc} */
	@Override
	public void setWhereClause(ConditionBuilder<T> whereClause) {

		if (this.whereClause != null) {
			throw new IllegalArgumentException("You cannot have more than one WhereClause by query");
		}

		this.whereClause = whereClause;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.torpedoquery.jpa.internal.query.QueryBuilder#getValueParameters()
	 */
	/** {@inheritDoc} */
	@Override
	public List<ValueParameter<?>> getValueParameters() {
		List<ValueParameter<?>> valueParameters = new ArrayList<>();

		Condition whereClauseCondition = getConditionClause(whereClause);

		feedValueParameters(valueParameters, whereClauseCondition);

		Condition withConditionClause = getConditionClause(withClause);

		feedValueParameters(valueParameters, withConditionClause);

		for (Join join : joins) {
			List<ValueParameter<?>> params = join.getParams();
			valueParameters.addAll(params);
		}

		if (groupBy != null) {
			Condition groupByCondition = groupBy.getCondition();
			feedValueParameters(valueParameters, groupByCondition);
		}

		return valueParameters;
	}

	private static void feedValueParameters(List<ValueParameter<?>> valueParameters, Condition clauseCondition) {
		if (clauseCondition != null) {
			valueParameters.addAll(clauseCondition.getValueParameters());
		}
	}

	/** {@inheritDoc} */
	@Override
	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.torpedoquery.jpa.internal.query.QueryBuilder#setGroupBy(org.
	 * torpedoquery.jpa.internal.query.GroupBy)
	 */
	/** {@inheritDoc} */
	@Override
	public void setGroupBy(GroupBy groupBy) {
		this.groupBy = groupBy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.torpedoquery.jpa.internal.query.QueryBuilder#setWithClause(org.
	 * torpedoquery.jpa.internal.conditions.ConditionBuilder)
	 */
	/** {@inheritDoc} */
	@Override
	public void setWithClause(ConditionBuilder<T> withClause) {
		this.withClause = withClause;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.torpedoquery.jpa.internal.query.QueryBuilder#hasWithClause()
	 */
	/** {@inheritDoc} */
	@Override
	public boolean hasWithClause() {
		return withClause != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.torpedoquery.jpa.internal.query.QueryBuilder#getWithClause(java.util.
	 * concurrent.atomic.AtomicInteger)
	 */
	/** {@inheritDoc} */
	@Override
	public String getWithClause(AtomicInteger incrementor) {

		StringBuilder builder = new StringBuilder();
		Condition with = getConditionClause(withClause);

		if (with != null) {
			builder.append(" with ").append(with.createQueryFragment(incrementor)).append(' ');
		}

		return builder.toString();
	}

	/** {@inheritDoc} */
	@Override
	public Optional<OnGoingLogicalCondition> condition() {
		return Optional.ofNullable(Optional.ofNullable(whereClause).orElse(withClause))
				.map(ConditionBuilder::getLogicalCondition);
	}

}
