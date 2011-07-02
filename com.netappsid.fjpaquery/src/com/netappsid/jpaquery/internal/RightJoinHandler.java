package com.netappsid.jpaquery.internal;

public class RightJoinHandler<T> extends JoinHandler<T> {
	public RightJoinHandler(FJPAMethodHandler methodHandler) {
		super(methodHandler);
	}

	@Override
	protected Join createJoin(QueryBuilder queryBuilder, String fieldName) {
		return new RightJoin(queryBuilder, fieldName);
	}
}
