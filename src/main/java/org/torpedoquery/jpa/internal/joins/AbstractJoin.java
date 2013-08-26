/**
 *   Copyright Xavier Jodoin xjodoin@torpedoquery.org
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
package org.torpedoquery.jpa.internal.joins;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.Join;
import org.torpedoquery.jpa.internal.query.ValueParameter;

public abstract class AbstractJoin implements Join {

	private final QueryBuilder join;
	private final String fieldName;

	public AbstractJoin(QueryBuilder join, String fieldName) {
		this.join = join;
		this.fieldName = fieldName;
	}

	@Override
	public String getJoin(String parentAlias, AtomicInteger incrementor) {

		return (" " + getJoinType() + " join " + parentAlias + "." + fieldName + " " + join.getAlias(incrementor))
				+ (join.hasWithClause() ? join.getWithClause(incrementor) : "") + (join.hasSubJoin() ? join.getJoins(incrementor) : "");
	}

	@Override
	public void appendWhereClause(StringBuilder builder, AtomicInteger incrementor) {
		join.appendWhereClause(builder, incrementor);
	}

	@Override
	public void appendOrderBy(StringBuilder builder, AtomicInteger incrementor) {
		join.appendOrderBy(builder, incrementor);
	}

	@Override
	public void appendGroupBy(StringBuilder builder, AtomicInteger incrementor) {
		join.appendGroupBy(builder, incrementor);
	}

	@Override
	public List<ValueParameter> getParams() {
		return join.getValueParameters();
	}

	public abstract String getJoinType();

}