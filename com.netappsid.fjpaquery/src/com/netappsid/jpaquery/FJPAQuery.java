package com.netappsid.jpaquery;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javassist.util.proxy.ProxyFactory;

import com.netappsid.jpaquery.internal.FJPAMethodHandler;
import com.netappsid.jpaquery.internal.InnerJoinHandler;
import com.netappsid.jpaquery.internal.Query;
import com.netappsid.jpaquery.internal.SelectHandler;
import com.netappsid.jpaquery.internal.WhereClauseHandler;

public class FJPAQuery {
    private static final FJPAMethodHandler methodHandler = new FJPAMethodHandler();
    private static ThreadLocal<Query> query = new ThreadLocal<Query>();

    public static <T> T from(Class<T> toQuery) {

	try {
	    final ProxyFactory proxyFactory = new ProxyFactory();
	    proxyFactory.setSuperclass(toQuery);
	    proxyFactory.setInterfaces(new Class[] { Query.class });

	    final T proxy = (T) proxyFactory.create(null, null, methodHandler);

	    methodHandler.addQueryBuilder(proxy, toQuery, new AtomicInteger());
	    return proxy;

	} catch (Exception e) {
	    e.printStackTrace();
	}

	return null;
    }

    public static void select(Object... values) {
	getQuery().handle(new SelectHandler());
    }

    public static <T> T innerJoin(T toJoin) {
	return getQuery().handle(new InnerJoinHandler<T>(methodHandler));
    }

    public static <T> OnGoingWhereClause<T> where(T object) {
	return getQuery().handle(new WhereClauseHandler<T>());
    }

    public static String query(Object proxy) {
	if (proxy instanceof Query) {
	    Query from = (Query) proxy;
	    return from.getQuery(proxy);
	}
	return null;
    }

    public static Map<String, Object> params(Object proxy) {
	if (proxy instanceof Query) {
	    Query from = (Query) proxy;
	    return from.getParameters(proxy);
	}
	return null;
    }

    public static Query getQuery() {
	return query.get();
    }

    public static void setQuery(Query query) {
	FJPAQuery.query.set(query);
    }
}
