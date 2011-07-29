package com.netappsid.jpaquery.internal;

public class InnerJoinHandler<T> extends JoinHandler<T> {
	public InnerJoinHandler(FJPAMethodHandler methodHandler, ProxyFactoryFactory proxyFactoryFactory) {
		super(methodHandler, proxyFactoryFactory);
	}

	public InnerJoinHandler(FJPAMethodHandler fjpaMethodHandler, ProxyFactoryFactory proxyFactoryFactory, Class<T> realType) {
		super(fjpaMethodHandler, proxyFactoryFactory, realType);
	}

	@Override
	protected Join createJoin(QueryBuilder queryBuilder, String fieldName) {
		return new InnerJoin(queryBuilder, fieldName);
	}
}
