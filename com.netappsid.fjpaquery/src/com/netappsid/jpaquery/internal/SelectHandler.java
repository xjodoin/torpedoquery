package com.netappsid.jpaquery.internal;

import java.util.List;
import java.util.Map;


public class SelectHandler implements QueryHandler<Void> {
    @Override
    public Void handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, List<MethodCall> methodCalls) {
	for (MethodCall methodCall : methodCalls) {
	    proxyQueryBuilders.get(methodCall.proxy).addSelector(methodCall.method);
	}
	return null;
    }
}
