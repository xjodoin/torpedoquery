package org.torpedoquery.jpa.internal.query;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.Query;
import org.torpedoquery.jpa.internal.Join;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.conditions.ConditionBuilder;

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