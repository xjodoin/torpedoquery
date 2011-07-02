package com.netappsid.jpaquery;

import com.netappsid.jpaquery.internal.AbstractJoin;
import com.netappsid.jpaquery.internal.QueryBuilder;

public class LeftJoin extends AbstractJoin {

	public LeftJoin(QueryBuilder join, String fieldName) {
		super(join, fieldName);
	}

	@Override
	public String getJoinType() {
		return "left";
	}

}
