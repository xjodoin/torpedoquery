package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;


public class SelectHandler implements QueryHandler<Void> {

	@Override
	public Void handleCall(QueryBuilder queryImpl, Method thisMethod) {
		queryImpl.addSelector(thisMethod);
		return null;
	}

}
