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
 */
package org.torpedoquery.core;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.Query;
import org.torpedoquery.jpa.internal.Join;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.conditions.ConditionBuilder;
import org.torpedoquery.jpa.internal.query.GroupBy;
import org.torpedoquery.jpa.internal.query.ValueParameter;
public interface QueryBuilder<T> extends Query<T>, Cloneable, Serializable {

	/**
	 * <p>
	 * getQuery.
	 * </p>
	 *
	 * @param incrementor
	 *            a {@link java.util.concurrent.atomic.AtomicInteger} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getQuery(AtomicInteger incrementor);

	/**
	 * <p>
	 * appendOrderBy.
	 * </p>
	 *
	 * @param builder
	 *            a {@link java.lang.StringBuilder} object.
	 * @param incrementor
	 *            a {@link java.util.concurrent.atomic.AtomicInteger} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String appendOrderBy(StringBuilder builder, AtomicInteger incrementor);

	/**
	 * <p>
	 * appendGroupBy.
	 * </p>
	 *
	 * @param builder
	 *            a {@link java.lang.StringBuilder} object.
	 * @param incrementor
	 *            a {@link java.util.concurrent.atomic.AtomicInteger} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String appendGroupBy(StringBuilder builder, AtomicInteger incrementor);

	/**
	 * <p>
	 * appendWhereClause.
	 * </p>
	 *
	 * @param builder
	 *            a {@link java.lang.StringBuilder} object.
	 * @param incrementor
	 *            a {@link java.util.concurrent.atomic.AtomicInteger} object.
	 * @return a {@link java.lang.StringBuilder} object.
	 */
	public StringBuilder appendWhereClause(StringBuilder builder, AtomicInteger incrementor);

	/**
	 * <p>
	 * appendSelect.
	 * </p>
	 *
	 * @param builder
	 *            a {@link java.lang.StringBuilder} object.
	 * @param incrementor
	 *            a {@link java.util.concurrent.atomic.AtomicInteger} object.
	 */
	public void appendSelect(StringBuilder builder, AtomicInteger incrementor);

	/**
	 * <p>
	 * getAlias.
	 * </p>
	 *
	 * @param incrementor
	 *            a {@link java.util.concurrent.atomic.AtomicInteger} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getAlias(AtomicInteger incrementor);

	/**
	 * <p>
	 * addSelector.
	 * </p>
	 *
	 * @param selector
	 *            a {@link org.torpedoquery.jpa.internal.Selector} object.
	 */
	public void addSelector(Selector selector);

	/**
	 * <p>
	 * addJoin.
	 * </p>
	 *
	 * @param join
	 *            a {@link org.torpedoquery.jpa.internal.Join} object.
	 */
	public void addJoin(Join join);

	/**
	 * <p>
	 * hasSubJoin.
	 * </p>
	 *
	 * @return a boolean.
	 */
	public boolean hasSubJoin();

	/**
	 * <p>
	 * getJoins.
	 * </p>
	 *
	 * @param incrementor
	 *            a {@link java.util.concurrent.atomic.AtomicInteger} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getJoins(AtomicInteger incrementor);

	/**
	 * <p>
	 * setWhereClause.
	 * </p>
	 *
	 * @param whereClause
	 *            a
	 *            {@link org.torpedoquery.jpa.internal.conditions.ConditionBuilder}
	 *            object.
	 */
	public void setWhereClause(ConditionBuilder<T> whereClause);

	/**
	 * <p>
	 * getValueParameters.
	 * </p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<ValueParameter<?>> getValueParameters();

	/**
	 * <p>
	 * addOrder.
	 * </p>
	 *
	 * @param selector
	 *            a {@link org.torpedoquery.jpa.internal.Selector} object.
	 */
	public void addOrder(Selector selector);

	/**
	 * <p>
	 * setGroupBy.
	 * </p>
	 *
	 * @param groupBy
	 *            a {@link org.torpedoquery.jpa.internal.query.GroupBy} object.
	 */
	public void setGroupBy(GroupBy groupBy);

	/**
	 * <p>
	 * setWithClause.
	 * </p>
	 *
	 * @param withClause
	 *            a
	 *            {@link org.torpedoquery.jpa.internal.conditions.ConditionBuilder}
	 *            object.
	 */
	public void setWithClause(ConditionBuilder<T> withClause);

	/**
	 * <p>
	 * hasWithClause.
	 * </p>
	 *
	 * @return a boolean.
	 */
	public boolean hasWithClause();

	/**
	 * <p>
	 * getWithClause.
	 * </p>
	 *
	 * @param incrementor
	 *            a {@link java.util.concurrent.atomic.AtomicInteger} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getWithClause(AtomicInteger incrementor);

	/**
	 * <p>clearSelectors.</p>
	 */
	void clearSelectors();

	String getEntityName();

}
