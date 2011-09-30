package org.torpedoquery.jpa.internal;

public class RightJoinHandler<T> extends JoinHandler<T> {
	public RightJoinHandler(TorpedoMethodHandler methodHandler, ProxyFactoryFactory proxyFactoryFactory) {
		super(methodHandler, proxyFactoryFactory);
	}

	public RightJoinHandler(TorpedoMethodHandler fjpaMethodHandler, ProxyFactoryFactory proxyFactoryFactory, Class<T> realType) {
		super(fjpaMethodHandler, proxyFactoryFactory, realType);
	}

	@Override
	protected Join createJoin(QueryBuilder queryBuilder, String fieldName) {
		return new RightJoin(queryBuilder, fieldName);
	}
}
