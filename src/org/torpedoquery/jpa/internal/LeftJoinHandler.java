package org.torpedoquery.jpa.internal;

public class LeftJoinHandler<T> extends JoinHandler<T> {

	public LeftJoinHandler(TorpedoMethodHandler methodHandler, ProxyFactoryFactory proxyFactoryFactory) {
		super(methodHandler, proxyFactoryFactory);
	}

	public LeftJoinHandler(TorpedoMethodHandler fjpaMethodHandler, ProxyFactoryFactory proxyFactoryFactory, Class<T> realType) {
		super(fjpaMethodHandler, proxyFactoryFactory, realType);
	}

	@Override
	protected Join createJoin(QueryBuilder queryBuilder, String fieldName) {
		return new LeftJoin(queryBuilder, fieldName);
	}

}
