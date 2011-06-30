package com.netappsid.jpaquery;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.internal.Join;
import com.netappsid.jpaquery.internal.QueryBuilder;

public class LeftJoin implements Join {

	private final QueryBuilder join;
	private final String fieldName;

	public LeftJoin(QueryBuilder queryBuilder, String fieldName) {
		this.join = queryBuilder;
		this.fieldName = fieldName;
	}

	@Override
	public String getJoin(String parentAlias, AtomicInteger incrementor) {

		return " left join " + parentAlias + "." + fieldName + " " + join.getAlias(incrementor) + (join.hasSubJoin() ? join.getJoins(incrementor) : "");
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
