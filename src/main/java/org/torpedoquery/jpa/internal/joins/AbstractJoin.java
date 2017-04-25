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
package org.torpedoquery.jpa.internal.joins;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.Join;
import org.torpedoquery.jpa.internal.conditions.LogicalCondition;
import org.torpedoquery.jpa.internal.query.ValueParameter;

public abstract class AbstractJoin implements Join {

	private final QueryBuilder join;
	private String fieldName;
	private LogicalCondition joinCondition;

	/**
	 * <p>
	 * Constructor for AbstractJoin.
	 * </p>
	 *
	 * @param join
	 *            a {@link org.torpedoquery.core.QueryBuilder} object.
	 * @param fieldName
	 *            a {@link java.lang.String} object.
	 */
	public AbstractJoin(QueryBuilder<?> join, String fieldName) {
		this.join = join;
		this.fieldName = fieldName;
	}

	public AbstractJoin(QueryBuilder<?> join, LogicalCondition joinCondition) {
		this.join = join;
		this.joinCondition = joinCondition;

	}

	/** {@inheritDoc} */
	@Override
	public String getJoin(String parentAlias, AtomicInteger incrementor) {
		if (joinCondition != null) {
			return " " + getJoinType() + " join " +join.getEntityName() + " " + join.getAlias(incrementor) + " on "
					+ joinCondition.createQueryFragment(incrementor);
		} else {
			return (" " + getJoinType() + " join " + parentAlias + "." + fieldName + " " + join.getAlias(incrementor))
					+ (join.hasWithClause() ? join.getWithClause(incrementor) : "")
					+ (join.hasSubJoin() ? join.getJoins(incrementor) : "");
		}

	}

	/** {@inheritDoc} */
	@Override
	public void appendWhereClause(StringBuilder builder, AtomicInteger incrementor) {
		join.appendWhereClause(builder, incrementor);
	}

	/** {@inheritDoc} */
	@Override
	public void appendOrderBy(StringBuilder builder, AtomicInteger incrementor) {
		join.appendOrderBy(builder, incrementor);
	}

	/** {@inheritDoc} */
	@Override
	public void appendGroupBy(StringBuilder builder, AtomicInteger incrementor) {
		join.appendGroupBy(builder, incrementor);
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
	 * @return a {@link java.lang.String} object.
	 */
	public abstract String getJoinType();

}
