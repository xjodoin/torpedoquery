package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.FJPAQuery;

import javassist.util.proxy.MethodHandler;

public class FJPAMethodHandler implements MethodHandler,Query{
	

	private QueryBuilder queryBuilder;
	private Method lastMethod;

	public FJPAMethodHandler(Class<?> toQuery,AtomicInteger increment) {
		queryBuilder = new QueryBuilder(toQuery,increment);
	}

	@Override
	public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args)
			throws Throwable 
	{
		if(thisMethod.getDeclaringClass().equals(Query.class))
		{
			return thisMethod.invoke(this, args);
		}
		lastMethod = thisMethod;
		FJPAQuery.setQuery((Query) self);
		return null;
	}
	
	@Override
	public String getQuery() {
		return queryBuilder.getQuery();
	}

	@Override
	public <T> T handle(QueryHandler<T> handler) {
		return handler.handleCall(queryBuilder, lastMethod);
	}

	public QueryBuilder getQueryBuilder() {
		return queryBuilder;
	}

}
