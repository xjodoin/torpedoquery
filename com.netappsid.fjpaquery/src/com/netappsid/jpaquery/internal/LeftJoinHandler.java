package com.netappsid.jpaquery.internal;

public class LeftJoinHandler<T> extends JoinHandler<T> {

	public LeftJoinHandler(FJPAMethodHandler methodHandler, ProxyFactoryFactory proxyFactoryFactory) {
		super(methodHandler, proxyFactoryFactory);
	}

	@Override
	protected Join createJoin(QueryBuilder queryBuilder, String fieldName) {
		return new LeftJoin(queryBuilder, fieldName);
	}

}
