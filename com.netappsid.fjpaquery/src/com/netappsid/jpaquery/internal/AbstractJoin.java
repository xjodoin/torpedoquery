package com.netappsid.jpaquery.internal;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractJoin implements Join {

	private final QueryBuilder join;
	private final String fieldName;

	public AbstractJoin(QueryBuilder join, String fieldName) {
		this.join = join;
		this.fieldName = fieldName;
	}

	@Override
	public String getJoin(String parentAlias, AtomicInteger incrementor) {

		return " " + getJoinType() + " join " + parentAlias + "." + fieldName + " " + join.getAlias(incrementor)
				+ (join.hasSubJoin() ? join.getJoins(incrementor) : "");
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
	public List<Parameter> getParams() {
		return join.getParameters();
	}

	public abstract String getJoinType();

}