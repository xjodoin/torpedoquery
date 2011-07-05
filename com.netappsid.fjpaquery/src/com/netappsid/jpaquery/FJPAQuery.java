package com.netappsid.jpaquery;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javassist.util.proxy.ProxyFactory;

import javax.persistence.EntityManager;

import com.netappsid.jpaquery.internal.AvgFunctionHandler;
import com.netappsid.jpaquery.internal.CountFunctionHandler;
import com.netappsid.jpaquery.internal.FJPAMethodHandler;
import com.netappsid.jpaquery.internal.InnerJoinHandler;
import com.netappsid.jpaquery.internal.InternalQuery;
import com.netappsid.jpaquery.internal.LeftJoinHandler;
import com.netappsid.jpaquery.internal.MaxFunctionHandler;
import com.netappsid.jpaquery.internal.MinFunctionHandler;
import com.netappsid.jpaquery.internal.RightJoinHandler;
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
	private static ThreadLocal<InternalQuery> query = new ThreadLocal<InternalQuery>();

	public static <T> T from(Class<T> toQuery) {

		try {
			final ProxyFactory proxyFactory = new ProxyFactory();
			proxyFactory.setSuperclass(toQuery);
			proxyFactory.setInterfaces(new Class[] { InternalQuery.class });

			FJPAMethodHandler fjpaMethodHandler = getFJPAMethodHandler();
			final T proxy = (T) proxyFactory.create(null, null, fjpaMethodHandler);

			fjpaMethodHandler.addQueryBuilder(proxy, toQuery);
			return proxy;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static <T> Query<T> select(T value) {
		return select(new Object[] { value });
	}

	public static <T> Query<T> select(Object... values) {
		return getQuery().handle(new SelectHandler(values));
	}

	public static <T> T innerJoin(T toJoin) {
		return getQuery().handle(new InnerJoinHandler<T>(getFJPAMethodHandler()));
	}

	public static <T> T innerJoin(Collection<T> toJoin) {
		return getQuery().handle(new InnerJoinHandler<T>(getFJPAMethodHandler()));
	}

	public static <T> T leftJoin(T toJoin) {
		return getQuery().handle(new LeftJoinHandler<T>(getFJPAMethodHandler()));
	}

	public static <T> T leftJoin(Collection<T> toJoin) {
		return getQuery().handle(new LeftJoinHandler<T>(getFJPAMethodHandler()));
	}

	public static <T> T rightJoin(T toJoin) {
		return getQuery().handle(new RightJoinHandler<T>(getFJPAMethodHandler()));
	}

	public static <T> T rightJoin(Collection<T> toJoin) {
		return getQuery().handle(new RightJoinHandler<T>(getFJPAMethodHandler()));
	}

	public static <T> OnGoingCondition<T> where(T object) {
		return getQuery().handle(new WhereClauseHandler<T>());
	}

	// JPA Functions
	public static Function count(Object object) {
		if (object instanceof InternalQuery) {
			setQuery((InternalQuery) object);
		}
		return getQuery().handle(new CountFunctionHandler(object));
	}

	public static Function sum(Number number) {
		return getQuery().handle(new SumFunctionHandler());
	}

	public static Function min(Number number) {
		return getQuery().handle(new MinFunctionHandler());
	}

	public static Function max(Number number) {
		return getQuery().handle(new MaxFunctionHandler());
	}

	public static Function avg(Number number) {
		return getQuery().handle(new AvgFunctionHandler());
	}

	public static String query(Object proxy) {
		if (proxy instanceof InternalQuery) {
			InternalQuery from = (InternalQuery) proxy;
			return from.getQuery(proxy);
		}
		return null;
	}

	public static Map<String, Object> params(Object proxy) {
		if (proxy instanceof InternalQuery) {
			InternalQuery from = (InternalQuery) proxy;
			return from.getParametersAsMap(proxy);
		}
		return null;
	}

	public static <T> T singleResult(EntityManager entityManager, Object from) {
		return (T) createJPAQuery(entityManager, from).getSingleResult();
	}

	public static <T> List<T> resultList(EntityManager entityManager, Object from) {
		return createJPAQuery(entityManager, from).getResultList();
	}

	public static void setQuery(InternalQuery query) {
		FJPAQuery.query.set(query);
	}

	// TODO devrait se retrouver dans l'api interne
	public static FJPAMethodHandler getFJPAMethodHandler() {
		return methodHandler.get();
	}

	private static InternalQuery getQuery() {
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
