package com.netappsid.jpaquery.internal;

import java.util.Map;

public class InnerJoin implements Join {
    private final QueryBuilder join;
    private final String fieldName;

    public InnerJoin(QueryBuilder join, String fieldName) {
	this.join = join;
	this.fieldName = fieldName;
    }

    public String getJoin(String parentAlias) {

	return " inner join " + parentAlias + "." + fieldName + " " + join.getAlias() + (join.hasSubJoin() ? join.getJoins() : "");
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
