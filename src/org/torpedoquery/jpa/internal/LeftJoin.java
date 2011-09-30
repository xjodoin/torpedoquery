package org.torpedoquery.jpa.internal;


public class LeftJoin extends AbstractJoin {

	public LeftJoin(QueryBuilder join, String fieldName) {
		super(join, fieldName);
	}

	@Override
	public String getJoinType() {
		return "left";
	}

}
