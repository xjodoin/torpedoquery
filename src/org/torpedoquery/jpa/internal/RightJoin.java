package com.netappsid.jpaquery.internal;

public class RightJoin extends AbstractJoin {
	public RightJoin(QueryBuilder join, String fieldName) {
		super(join, fieldName);
	}

	@Override
	public String getJoinType() {
		return "right";
	}
}
