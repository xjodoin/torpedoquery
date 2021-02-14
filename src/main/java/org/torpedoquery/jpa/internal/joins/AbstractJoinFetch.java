
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
package org.torpedoquery.jpa.internal.joins;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.Join;
import org.torpedoquery.jpa.internal.conditions.LogicalCondition;
import org.torpedoquery.jpa.internal.query.ValueParameter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractJoinFetch implements Join {

	private final QueryBuilder join;
	private String fieldName;
	private LogicalCondition joinCondition;

	/**
	 * <p>
	 * Constructor for AbstractJoinFetch.
	 * </p>
	 *
	 * @param join
	 *            a {@link QueryBuilder} object.
	 * @param fieldName
	 *            a {@link String} object.
	 */
	public AbstractJoinFetch(QueryBuilder<?> join, String fieldName) {
		this.join = join;
		this.fieldName = fieldName;
	}

	/**
	 * <p>Constructor for AbstractJoinFetch.</p>
	 *
	 * @param join a {@link QueryBuilder} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 */
	public AbstractJoinFetch(QueryBuilder<?> join, LogicalCondition joinCondition) {
		this.join = join;
		this.joinCondition = joinCondition;
	}

	/** {@inheritDoc} */
	@Override
	public String getJoin(String parentAlias, AtomicInteger incrementer) {
		if (joinCondition != null) {
			return " " + getJoinType() + " join fetch " +join.getEntityName() + " " + join.getAlias(incrementer) + " on "
					+ joinCondition.createQueryFragment(incrementer);
		} else {
			return (" " + getJoinType() + " join fetch " + parentAlias + "." + fieldName + " " + join.getAlias(incrementer))
					+ (join.hasWithClause() ? join.getWithClause(incrementer) : "")
					+ (join.hasSubJoin() ? join.getJoins(incrementer) : "");
		}
	}

	/** {@inheritDoc} */
	@Override
	public void appendWhereClause(StringBuilder builder, AtomicInteger incrementer) {
		join.appendWhereClause(builder, incrementer);
	}

	/** {@inheritDoc} */
	@Override
	public void appendGroupBy(StringBuilder builder, AtomicInteger incrementer) {
		join.appendGroupBy(builder, incrementer);
	}

	/** {@inheritDoc} */
	@Override
	public List<ValueParameter<?>> getParams() {
		List<ValueParameter<?>> valueParameters = join.getValueParameters();
		if(joinCondition != null) {
			valueParameters.addAll(joinCondition.getValueParameters());
		}
		return valueParameters;
	}

	/**
	 * <p>
	 * getJoinType.
	 * </p>
	 *
	 * @return a {@link String} object.
	 */
	public abstract String getJoinType();

}
