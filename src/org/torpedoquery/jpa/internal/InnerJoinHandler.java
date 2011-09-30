package org.torpedoquery.jpa.internal;

public class InnerJoinHandler<T> extends JoinHandler<T> {
	public InnerJoinHandler(TorpedoMethodHandler methodHandler, ProxyFactoryFactory proxyFactoryFactory) {
		super(methodHandler, proxyFactoryFactory);
	}

	public InnerJoinHandler(TorpedoMethodHandler fjpaMethodHandler, ProxyFactoryFactory proxyFactoryFactory, Class<T> realType) {
		super(fjpaMethodHandler, proxyFactoryFactory, realType);
	}

	@Override
	protected Join createJoin(QueryBuilder queryBuilder, String fieldName) {
		return new InnerJoin(queryBuilder, fieldName);
	}
}
