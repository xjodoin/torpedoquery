package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javassist.util.proxy.MethodHandler;

import com.netappsid.jpaquery.FJPAQuery;

public class FJPAMethodHandler implements MethodHandler, Query {
    private Map<Object, QueryBuilder> proxyQueryBuilders = new IdentityHashMap<Object, QueryBuilder>();
    private List<MethodCall> methods = new ArrayList<MethodCall>();

    public QueryBuilder addQueryBuilder(Object proxy, Class toQuery, AtomicInteger increment) {
	final QueryBuilder queryBuilder = new QueryBuilder(toQuery, increment);

	proxyQueryBuilders.put(proxy, queryBuilder);
	return queryBuilder;
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
	if (thisMethod.getDeclaringClass().equals(Query.class)) {
	    return thisMethod.invoke(this, args);
	}

	methods.add(new MethodCall(self, thisMethod));
	FJPAQuery.setQuery((Query) self);
	return null;
    }

    @Override
    public String getQuery(Object proxy) {
	return proxyQueryBuilders.get(proxy).getQuery();
    }

    @Override
    public Map<String, Object> getParameters(Object proxy) {
	return proxyQueryBuilders.get(proxy).getParams();
    }

    @Override
    public <T> T handle(QueryHandler<T> handler) {
	final T result = handler.handleCall(proxyQueryBuilders, Collections.unmodifiableList(methods));

	methods.clear();
	return result;
    }

    public QueryBuilder getQueryBuilder(Object proxy) {
	return proxyQueryBuilders.get(proxy);
    }
}
