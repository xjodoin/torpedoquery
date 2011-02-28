package com.netappsid.jpaquery.internal;

import java.util.Map;

public interface Join {

	void appendWhereClause(StringBuilder builder);

	void appendSelect(StringBuilder builder);

	String getJoin(String alias);

	Map<String, Object> getParams();

}
