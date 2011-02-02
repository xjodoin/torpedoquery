package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.util.List;

public class SelectHandler implements QueryHandler<Void> {
    @Override
    public Void handleCall(QueryBuilder queryImpl, List<Method> methods) {
	for (Method method : methods) {
	    queryImpl.addSelector(method);
	}
	return null;
    }
}
