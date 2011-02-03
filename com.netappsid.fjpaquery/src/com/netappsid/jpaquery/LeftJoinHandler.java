package com.netappsid.jpaquery;

import com.netappsid.jpaquery.internal.FJPAMethodHandler;
import com.netappsid.jpaquery.internal.Join;
import com.netappsid.jpaquery.internal.JoinHandler;
import com.netappsid.jpaquery.internal.QueryBuilder;

public class LeftJoinHandler<T> extends JoinHandler<T> {

	public LeftJoinHandler(FJPAMethodHandler methodHandler) {
		super(methodHandler);
	}

	@Override
	protected Join createJoin(QueryBuilder queryBuilder, String fieldName) {
		return new LeftJoin(queryBuilder, fieldName);
	}

}
