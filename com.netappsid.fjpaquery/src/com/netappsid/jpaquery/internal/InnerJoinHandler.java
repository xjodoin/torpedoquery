package com.netappsid.jpaquery.internal;

public class InnerJoinHandler<T> extends JoinHandler<T> {
	public InnerJoinHandler(FJPAMethodHandler methodHandler) {
		super(methodHandler);
	}

	public InnerJoinHandler(FJPAMethodHandler fjpaMethodHandler, Class<T> realType) {
		super(fjpaMethodHandler,realType);
	}

	@Override
	protected Join createJoin(QueryBuilder queryBuilder, String fieldName) {
		return new InnerJoin(queryBuilder, fieldName);
	}
}
