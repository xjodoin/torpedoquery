package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javassist.util.proxy.MethodHandler;

import com.netappsid.jpaquery.FJPAQuery;

public class FJPAMethodHandler implements MethodHandler, Query {
    private QueryBuilder queryBuilder;
    private List<Method> methods = new ArrayList<Method>();

    public FJPAMethodHandler(Class<?> toQuery, AtomicInteger increment) {
	queryBuilder = new QueryBuilder(toQuery, increment);
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
	if (thisMethod.getDeclaringClass().equals(Query.class)) {
	    return thisMethod.invoke(this, args);
	}
	
	methods.add(thisMethod);
	FJPAQuery.setQuery((Query) self);
	return null;
    }

    @Override
    public String getQuery() {
	return queryBuilder.getQuery();
    }
    
    @Override
    public Map<String, Object> getParameters() {
        return queryBuilder.getParams();
    }
    
    @Override
    public <T> T handle(QueryHandler<T> handler) {
	final T result = handler.handleCall(queryBuilder, Collections.unmodifiableList(methods));
	
	methods.clear();
	return result;
    }

    public QueryBuilder getQueryBuilder() {
	return queryBuilder;
    }
}
