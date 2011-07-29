package com.netappsid.jpaquery.internal;

public class RightJoinHandler<T> extends JoinHandler<T> {
	public RightJoinHandler(FJPAMethodHandler methodHandler, ProxyFactoryFactory proxyFactoryFactory) {
		super(methodHandler, proxyFactoryFactory);
	}

	@Override
	protected Join createJoin(QueryBuilder queryBuilder, String fieldName) {
		return new RightJoin(queryBuilder, fieldName);
	}
}
