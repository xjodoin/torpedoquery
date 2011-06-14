package com.netappsid.jpaquery;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import javassist.util.proxy.ProxyFactory;

import javax.persistence.EntityManager;

import com.netappsid.jpaquery.internal.AvgFunctionHandler;
import com.netappsid.jpaquery.internal.CountFunctionHandler;
import com.netappsid.jpaquery.internal.FJPAMethodHandler;
import com.netappsid.jpaquery.internal.InnerJoinHandler;
import com.netappsid.jpaquery.internal.MaxFunctionHandler;
import com.netappsid.jpaquery.internal.MinFunctionHandler;
import com.netappsid.jpaquery.internal.Query;
import com.netappsid.jpaquery.internal.SelectHandler;
import com.netappsid.jpaquery.internal.SumFunctionHandler;
import com.netappsid.jpaquery.internal.WhereClauseHandler;

public class FJPAQuery {
	private static ThreadLocal<FJPAMethodHandler> methodHandler = new ThreadLocal<FJPAMethodHandler>() {
		@Override
		protected FJPAMethodHandler initialValue() {
			return new FJPAMethodHandler();
		}
	};
	private static ThreadLocal<Query> query = new ThreadLocal<Query>();

	public static <T> T from(Class<T> toQuery) {

		try {
			final ProxyFactory proxyFactory = new ProxyFactory();
			proxyFactory.setSuperclass(toQuery);
			proxyFactory.setInterfaces(new Class[] { Query.class });

			FJPAMethodHandler fjpaMethodHandler = getFJPAMethodHandler();
			final T proxy = (T) proxyFactory.create(null, null, fjpaMethodHandler);

			fjpaMethodHandler.addQueryBuilder(proxy, toQuery, new AtomicInteger());
			return proxy;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void select(Object... values) {
		getQuery().handle(new SelectHandler(values));
	}

	public static <T> T innerJoin(T toJoin) {
		return getQuery().handle(new InnerJoinHandler<T>(getFJPAMethodHandler()));
	}

	public static <T> T leftJoin(T toJoin) {
		return getQuery().handle(new LeftJoinHandler<T>(getFJPAMethodHandler()));
	}

	public static <T> T innerJoin(Collection<T> toJoin) {
		return getQuery().handle(new InnerJoinHandler<T>(getFJPAMethodHandler()));
	}

	public static <T> T leftJoin(Collection<T> toJoin) {
		return getQuery().handle(new LeftJoinHandler<T>(getFJPAMethodHandler()));
	}

	public static <T> OnGoingCondition<T> where(T object) {
		return getQuery().handle(new WhereClauseHandler<T>());
	}

	// JPA Functions
	public static Function count(Object object) {
		if (object instanceof Query) {
			setQuery((Query) object);
		}
		return getQuery().handle(new CountFunctionHandler(object));
	}
	
	public static Function sum(Number number)
	{
		return getQuery().handle(new SumFunctionHandler());
	}
	
	public static Function min(Number number)
	{
		return getQuery().handle(new MinFunctionHandler());
	}
	
	public static Function max(Number number)
	{
		return getQuery().handle(new MaxFunctionHandler());
	}
	
	public static Function avg(Number number)
	{
		return getQuery().handle(new AvgFunctionHandler());
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

	public static <T> T singleResult(EntityManager entityManager, Object from) {
		return (T) createJPAQuery(entityManager, from).getSingleResult();
	}

	public static <T> List<T> resultList(EntityManager entityManager, Object from) {
		return createJPAQuery(entityManager, from).getResultList();
	}

	public static void setQuery(Query query) {
		FJPAQuery.query.set(query);
	}

	private static FJPAMethodHandler getFJPAMethodHandler() {
		return methodHandler.get();
	}

	private static Query getQuery() {
		return query.get();
	}

	private static javax.persistence.Query createJPAQuery(EntityManager entityManager, Object from) {
		final javax.persistence.Query query = entityManager.createQuery(query(from));
		final Map<String, Object> parameters = params(from);

		for (Entry<String, Object> parameter : parameters.entrySet()) {
			query.setParameter(parameter.getKey(), parameter.getValue());
		}

		return query;
	}
}
