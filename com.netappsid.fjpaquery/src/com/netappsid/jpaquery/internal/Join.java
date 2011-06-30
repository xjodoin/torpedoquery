package com.netappsid.jpaquery.internal;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface Join {

	void appendWhereClause(StringBuilder builder, AtomicInteger incrementor);

	void appendSelect(StringBuilder builder, AtomicInteger incrementor);

	String getJoin(String alias, AtomicInteger incrementor);

	Map<String, Object> getParams();

}
