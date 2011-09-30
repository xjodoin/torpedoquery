package org.torpedoquery.jpa.internal;

public class InnerJoin extends AbstractJoin {
	public InnerJoin(QueryBuilder join, String fieldName) {
		super(join, fieldName);
	}

	@Override
	public String getJoinType() {
		return "inner";
	}
}
