package com.netappsid.jpaquery.internal;

public class InnerJoin extends AbstractJoin {
	public InnerJoin(QueryBuilder join, String fieldName) {
		super(join, fieldName);
	}

	@Override
	public String getJoinType() {
		return "inner";
	}
}
