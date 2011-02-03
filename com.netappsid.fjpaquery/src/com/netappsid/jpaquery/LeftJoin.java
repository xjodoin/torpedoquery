package com.netappsid.jpaquery;

import java.util.Map;

import com.netappsid.jpaquery.internal.Join;
import com.netappsid.jpaquery.internal.QueryBuilder;

public class LeftJoin implements Join {

	private final QueryBuilder join;
	private final String fieldName;

	public LeftJoin(QueryBuilder queryBuilder, String fieldName) {
		this.join = queryBuilder;
		this.fieldName = fieldName;
	}

	public String getJoin(String parentAlias) {

		return " left join " + parentAlias + "." + fieldName + " " + join.getAlias() + (join.hasSubJoin() ? join.getJoins() : "");
	}

	public void appendSelect(StringBuilder builder) {
		join.appendSelect(builder);
	}

	public void appendWhereClause(StringBuilder builder) {
		join.appendWhereClause(builder);
	}

	public Map<String, Object> getParams() {
		return join.getParams();
	}

}
