
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;

import org.apache.commons.lang3.SerializationUtils;
import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.OnGoingLogicalCondition;
import org.torpedoquery.jpa.Query;
import org.torpedoquery.jpa.internal.Condition;
import org.torpedoquery.jpa.internal.Join;
import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.TorpedoMagic;
import org.torpedoquery.jpa.internal.conditions.ConditionBuilder;

public class DefaultQueryBuilder<T> implements QueryBuilder<T> {
	private final Class<?> toQuery;
	private final List<Selector> toSelect = new ArrayList<>();
	private final List<Join> joins = new ArrayList<>();
	private ConditionBuilder<T> whereClause;
	private ConditionBuilder<T> withClause;

	private String alias;
	private OrderBy orderBy;
	private GroupBy groupBy;

	// paging infos
	private int startPosition;
	private int maxResult;
	private LockModeType lockMode;

	/**
	 * <p>
	 * Constructor for DefaultQueryBuilder.
	 * </p>
	 *
	 * @param toQuery
	 *            a {@link java.lang.Class} object.
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
	 * @see org.torpedoquery.jpa.internal.query.QueryBuilder#addSelector(org.
	 * torpedoquery.jpa.internal.Selector)
	 */
	/** {@inheritDoc} */
	@Override
	public void addSelector(Selector selector) {
		toSelect.add(selector);
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

	/** {@inheritDoc} */
	@Override
	public Map<String, Object> getParameters() {

		freezeQuery(new AtomicInteger());

		Map<String, Object> params = new HashMap<>();
		List<ValueParameter<?>> parameters = getValueParameters();
		for (ValueParameter parameter : parameters) {
			params.put(parameter.getName(), parameter.getValue());
		}
		return params;
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

	/** {@inheritDoc} */
	@Override
	public Optional<T> get(EntityManager entityManager) {
		try {
			return Optional.<T>ofNullable((T) createJPAQuery(entityManager).getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<T> list(EntityManager entityManager) {
		return createJPAQuery(entityManager).getResultList();
	}

	/** {@inheritDoc} */
	@Override
	public <E> List<E> map(EntityManager entityManager, Function<T, E> function) {
		List<T> toConvert = list(entityManager);
		List<E> result = new ArrayList<>();

		for (T value : toConvert) {
			result.add(function.apply(value));
		}
		return result;
	}

	private javax.persistence.Query createJPAQuery(EntityManager entityManager) {
		final javax.persistence.Query query = entityManager.createQuery(getQuery(new AtomicInteger()));

		if (startPosition >= 0) {
			query.setFirstResult(startPosition);
		}

		if (maxResult > 0) {
			query.setMaxResults(maxResult);
		}

		if (lockMode != null) {
			query.setLockMode(lockMode);
		}

		final Map<String, Object> parameters = getParameters();

		for (Entry<String, Object> parameter : parameters.entrySet()) {
			query.setParameter(parameter.getKey(), parameter.getValue());
		}

		TorpedoMagic.setQuery(null);

		return query;
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
	public Query<T> setFirstResult(int startPosition) {
		this.startPosition = startPosition;
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public Query<T> setMaxResults(int maxResult) {
		this.maxResult = maxResult;
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public Object getProxy() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return "( " + getQuery(incrementor) + " )";
	}

	/** {@inheritDoc} */
	@Override
	public Parameter<T> generateParameter(T value) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public Optional<OnGoingLogicalCondition> condition() {
		return Optional.ofNullable(Optional.ofNullable(whereClause).orElse(withClause))
				.map(ConditionBuilder::getLogicalCondition);
	}

	/** {@inheritDoc} */
	@Override
	public void clearSelectors() {
		toSelect.clear();
	}

	/**
	 * <p>
	 * freeze.
	 * </p>
	 *
	 * @return a {@link org.torpedoquery.jpa.Query} object.
	 */
	public Query<T> freeze() {
		QueryBuilder clone = SerializationUtils.clone(this);
		return clone;
	}

	/** {@inheritDoc} */
	@Override
	public Query<T> setLockMode(LockModeType lockMode) {
		this.lockMode = lockMode;
		return this;
	}
}
