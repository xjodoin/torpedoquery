package org.torpedoquery.jpa.internal;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface Join {

	void appendWhereClause(StringBuilder builder, AtomicInteger incrementor);

	String getJoin(String alias, AtomicInteger incrementor);

	List<ValueParameter> getParams();

	void appendOrderBy(StringBuilder builder, AtomicInteger incrementor);

	void appendGroupBy(StringBuilder builder, AtomicInteger incrementor);

}
