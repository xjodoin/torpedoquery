package com.netappsid.jpaquery.internal;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InnerJoin implements Join {
	private final QueryBuilder join;
	private final String fieldName;

	public InnerJoin(QueryBuilder join, String fieldName) {
		this.join = join;
		this.fieldName = fieldName;
	}

	@Override
	public String getJoin(String parentAlias, AtomicInteger incrementor) {

		return " inner join " + parentAlias + "." + fieldName + " " + join.getAlias(incrementor) + (join.hasSubJoin() ? join.getJoins(incrementor) : "");
	}

	@Override
	public void appendSelect(StringBuilder builder, AtomicInteger incrementor) {
		join.appendSelect(builder, incrementor);
	}

	@Override
	public void appendWhereClause(StringBuilder builder, AtomicInteger incrementor) {
		join.appendWhereClause(builder, incrementor);
	}

	@Override
	public Map<String, Object> getParams() {
		return join.getParams();
	}
}
