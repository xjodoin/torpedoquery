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
package org.torpedoquery.core;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.Query;
import org.torpedoquery.jpa.internal.Join;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.conditions.ConditionBuilder;
import org.torpedoquery.jpa.internal.query.GroupBy;
import org.torpedoquery.jpa.internal.query.ValueParameter;

public interface QueryBuilder<T> extends Query<T> {

	public String getQuery(AtomicInteger incrementor);

	public String appendOrderBy(StringBuilder builder, AtomicInteger incrementor);

	public String appendGroupBy(StringBuilder builder, AtomicInteger incrementor);

	public StringBuilder appendWhereClause(StringBuilder builder,
			AtomicInteger incrementor);

	public void appendSelect(StringBuilder builder, AtomicInteger incrementor);

	public String getAlias(AtomicInteger incrementor);

	public void addSelector(Selector selector);

	public void addJoin(Join innerJoin);

	public boolean hasSubJoin();

	public String getJoins(AtomicInteger incrementor);

	public void setWhereClause(ConditionBuilder<?> whereClause);

	public List<ValueParameter> getValueParameters();

	public void addOrder(Selector selector);

	public void setGroupBy(GroupBy groupBy);

	public void setWithClause(ConditionBuilder<?> withClause);

	public boolean hasWithClause();

	public String getWithClause(AtomicInteger incrementor);
	
}