package com.netappsid.jpaquery.internal;

public class InnerJoinHandler<T> extends JoinHandler<T> {
	public InnerJoinHandler(FJPAMethodHandler methodHandler) {
		super(methodHandler);
	}

	@Override
	protected Join createJoin(QueryBuilder queryBuilder, String fieldName) {
		return new InnerJoin(queryBuilder, fieldName);
	}
}
