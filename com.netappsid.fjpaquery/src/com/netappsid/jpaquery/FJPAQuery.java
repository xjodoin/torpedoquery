package com.netappsid.jpaquery;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javassist.util.proxy.ProxyFactory;

import com.netappsid.jpaquery.internal.FJPAMethodHandler;
import com.netappsid.jpaquery.internal.InnerJoinHandler;
import com.netappsid.jpaquery.internal.ParamsOutputHandler;
import com.netappsid.jpaquery.internal.Query;
import com.netappsid.jpaquery.internal.SelectHandler;
import com.netappsid.jpaquery.internal.WhereClauseHandler;

public class FJPAQuery {

	private static Query query;

	public static <T> T from(Class<T> toQuery) {

		try {
			final ProxyFactory proxyFactory = new ProxyFactory();
			proxyFactory.setSuperclass(toQuery);
			proxyFactory.setInterfaces(new Class[] { Query.class });

			return (T) proxyFactory.create(null, null, new FJPAMethodHandler(
					toQuery,new AtomicInteger()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void select(Object value) {
		query.handle(new SelectHandler());
	}
	
	public static <T> T innerJoin(T toJoin)
	{
		return query.handle(new InnerJoinHandler<T>());
	}
	
	public static <T> OnGoingWhereClause<T> where(T object)
	{
		return query.handle(new WhereClauseHandler<T>());
	}

	public static String query(Object proxy) {
		if (proxy instanceof Query) {
			Query from = (Query) proxy;
			return from.getQuery();
		}
		return null;
	}
	
	public static Map<String,Object> params(Object proxy)
	{
		return query.handle(new ParamsOutputHandler());
	}

	public static void setQuery(Query query) {
		FJPAQuery.query = query;
	}
}
