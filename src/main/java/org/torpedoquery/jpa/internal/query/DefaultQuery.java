
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.OnGoingLogicalCondition;
import org.torpedoquery.jpa.Query;
import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.TorpedoMagic;
public class DefaultQuery<T> implements Query<T> {

	private List<Selector> toSelect;
	private QueryBuilder<T> queryBuilder;
	private String freezeQuery;

	// paging infos
	private int startPosition;
	private int maxResult;
	private LockModeType lockMode;

	/**
	 * <p>
	 * Constructor for DefaultQueryBuilder.
	 * </p>
	 *
	 * @param builder a {@link org.torpedoquery.core.QueryBuilder} object.
	 * @param toSelect a {@link java.util.List} object.
	 */
	public DefaultQuery(QueryBuilder<T> builder, List<Selector> toSelect) {
		this.queryBuilder = builder;
		this.toSelect = new ArrayList<Selector>(toSelect);
	}

	/** {@inheritDoc} */
	public String getQuery(AtomicInteger incrementor) {
		if (freezeQuery == null) {
			String from = " from " + queryBuilder.getEntityName() + " " + queryBuilder.getAlias(incrementor);
			StringBuilder builder = new StringBuilder();

			appendSelect(builder, incrementor);

			builder.append(from)

					.append(queryBuilder.getJoins(incrementor))

					.append(queryBuilder.appendWhereClause(new StringBuilder(), incrementor))

					.append(queryBuilder.appendOrderBy(new StringBuilder(), incrementor))

					.append(queryBuilder.appendGroupBy(new StringBuilder(), incrementor));

			freezeQuery = builder.toString().trim();
		}
		return freezeQuery;
	}

	/** {@inheritDoc} */
	@Override
	public String getQuery() {
		return getQuery(new AtomicInteger());
	}

	/**
	 * <p>appendSelect.</p>
	 *
	 * @param builder a {@link java.lang.StringBuilder} object.
	 * @param incrementor a {@link java.util.concurrent.atomic.AtomicInteger} object.
	 */
	public void appendSelect(StringBuilder builder, AtomicInteger incrementor) {
		for (Selector selector : toSelect) {
			if (builder.length() == 0) {
				builder.append("select ").append(selector.createQueryFragment(incrementor));
			} else {
				builder.append(", ").append(selector.createQueryFragment(incrementor));
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public Map<String, Object> getParameters() {

		//generate query
		getQuery();
		
		Map<String, Object> params = new HashMap<>();
		List<ValueParameter<?>> parameters = queryBuilder.getValueParameters();
		for (ValueParameter parameter : parameters) {
			params.put(parameter.getName(), parameter.getValue());
		}
		return params;
	}

	/** {@inheritDoc} */
	@Override
	public List<ValueParameter<?>> getValueParameters() {
		return queryBuilder.getValueParameters();
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
		return queryBuilder.condition();
	}

	/** {@inheritDoc} */
	@Override
	public Query<T> setLockMode(LockModeType lockMode) {
		this.lockMode = lockMode;
		return this;
	}
}
