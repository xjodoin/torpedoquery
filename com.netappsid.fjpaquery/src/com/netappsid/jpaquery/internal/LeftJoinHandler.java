package com.netappsid.jpaquery.internal;


public class LeftJoinHandler<T> extends JoinHandler<T> {

	public LeftJoinHandler(FJPAMethodHandler methodHandler) {
		super(methodHandler);
	}

	@Override
	protected Join createJoin(QueryBuilder queryBuilder, String fieldName) {
		return new LeftJoin(queryBuilder, fieldName);
	}

}
