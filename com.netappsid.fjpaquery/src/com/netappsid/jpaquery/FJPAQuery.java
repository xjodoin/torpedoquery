package com.netappsid.jpaquery;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javassist.util.proxy.ProxyFactory;

import com.netappsid.jpaquery.internal.ArrayCallHandler;
import com.netappsid.jpaquery.internal.ArrayCallHandler.ValueHandler;
import com.netappsid.jpaquery.internal.AscFunctionHandler;
import com.netappsid.jpaquery.internal.AvgFunctionHandler;
import com.netappsid.jpaquery.internal.CoalesceFunction;
import com.netappsid.jpaquery.internal.CountFunctionHandler;
import com.netappsid.jpaquery.internal.DescFunctionHandler;
import com.netappsid.jpaquery.internal.DistinctFunctionHandler;
import com.netappsid.jpaquery.internal.FJPAMethodHandler;
import com.netappsid.jpaquery.internal.InnerJoinHandler;
import com.netappsid.jpaquery.internal.LeftJoinHandler;
import com.netappsid.jpaquery.internal.MaxFunctionHandler;
import com.netappsid.jpaquery.internal.MinFunctionHandler;
import com.netappsid.jpaquery.internal.MultiClassLoaderProvider;
import com.netappsid.jpaquery.internal.Proxy;
import com.netappsid.jpaquery.internal.QueryBuilder;
import com.netappsid.jpaquery.internal.RightJoinHandler;
import com.netappsid.jpaquery.internal.Selector;
import com.netappsid.jpaquery.internal.SumFunctionHandler;
import com.netappsid.jpaquery.internal.WhereClauseCollectionHandler;
import com.netappsid.jpaquery.internal.WhereClauseHandler;

public class FJPAQuery {

	private static MultiClassLoaderProvider osgiAwareClassLoaderProvider;

	static {
		osgiAwareClassLoaderProvider = new MultiClassLoaderProvider();
		ProxyFactory.classLoaderProvider = osgiAwareClassLoaderProvider;
	}

	private static ThreadLocal<Proxy> query = new ThreadLocal<Proxy>();

	public static <T> T from(Class<T> toQuery) {

		try {
			final ProxyFactory proxyFactory = new ProxyFactory();

			proxyFactory.setSuperclass(toQuery);
			proxyFactory.setInterfaces(new Class[] { Proxy.class });

			QueryBuilder queryBuilder = new QueryBuilder(toQuery);
			FJPAMethodHandler fjpaMethodHandler = new FJPAMethodHandler(queryBuilder);
			final T proxy = (T) proxyFactory.create(null, null, fjpaMethodHandler);

			fjpaMethodHandler.addQueryBuilder(proxy, queryBuilder);

			setQuery((Proxy) proxy);
			return proxy;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static <T> Query<T> select(Function<T> value) {
		return (Query<T>) FJPAQuery.select(new Object[] { value });
	}

	public static <T> Query<T> select(T value) {
		return (Query<T>) FJPAQuery.select(new Object[] { value });
	}

	public static <T> Query<T[]> select(Function<T>... values) {
		return select((T[]) values);
	}

	public static <T> Query<T[]> select(T... values) {
		FJPAMethodHandler fjpaMethodHandler = getFJPAMethodHandler();
		final QueryBuilder root = fjpaMethodHandler.getRoot();
		fjpaMethodHandler.handle(new ArrayCallHandler(new ValueHandler() {

			@Override
			public void handle(Proxy query, QueryBuilder queryBuilder, Selector selector) {
				root.addSelector(selector);
			}
		}, values));

		return root;

	}

	public static <T> T innerJoin(T toJoin) {
		return getFJPAMethodHandler().handle(new InnerJoinHandler<T>(getFJPAMethodHandler()));
	}
	
	public static <T, E extends T> E innerJoin(T toJoin, Class<E> realType) {
		return getFJPAMethodHandler().handle(new InnerJoinHandler<E>(getFJPAMethodHandler(), realType));
	}

	public static <T, E extends T> E innerJoin(Collection<T> toJoin, Class<E> realType) {
		return getFJPAMethodHandler().handle(new InnerJoinHandler<E>(getFJPAMethodHandler(), realType));
	}

	public static <T> T innerJoin(Collection<T> toJoin) {
		return getFJPAMethodHandler().handle(new InnerJoinHandler<T>(getFJPAMethodHandler()));
	}

	public static <T> T leftJoin(T toJoin) {
		return getFJPAMethodHandler().handle(new LeftJoinHandler<T>(getFJPAMethodHandler()));
	}

	public static <T> T leftJoin(Collection<T> toJoin) {
		return getFJPAMethodHandler().handle(new LeftJoinHandler<T>(getFJPAMethodHandler()));
	}

	public static <T> T rightJoin(T toJoin) {
		return getFJPAMethodHandler().handle(new RightJoinHandler<T>(getFJPAMethodHandler()));
	}

	public static <T> T rightJoin(Collection<T> toJoin) {
		return getFJPAMethodHandler().handle(new RightJoinHandler<T>(getFJPAMethodHandler()));
	}

	public static <T> OnGoingCondition<T> where(T object) {
		return getFJPAMethodHandler().handle(new WhereClauseHandler<T, OnGoingCondition<T>>());
	}

	public static <T extends Number> OnGoingNumberCondition<T> where(T object) {
		return getFJPAMethodHandler().handle(new WhereClauseHandler<T, OnGoingNumberCondition<T>>());
	}

	public static OnGoingStringCondition<String> where(String object) {
		return getFJPAMethodHandler().handle(new WhereClauseHandler<String, OnGoingStringCondition<String>>());
	}

	public static <T> OnGoingCollectionCondition<T> where(Collection<T> object) {
		return getFJPAMethodHandler().handle(new WhereClauseCollectionHandler<T>());
	}

	public static <T> OnGoingCondition<T> condition(T object) {
		return getFJPAMethodHandler().handle(new WhereClauseHandler<T, OnGoingCondition<T>>(false));
	}

	public static <T extends Number> OnGoingNumberCondition<T> condition(T object) {
		return getFJPAMethodHandler().handle(new WhereClauseHandler<T, OnGoingNumberCondition<T>>(false));
	}

	public static OnGoingStringCondition<String> condition(String object) {
		return getFJPAMethodHandler().handle(new WhereClauseHandler<String, OnGoingStringCondition<String>>(false));
	}

	public static <T> OnGoingCollectionCondition<T> condition(Collection<T> object) {
		return getFJPAMethodHandler().handle(new WhereClauseCollectionHandler<T>(false));
	}

	public static void groupBy(Object... values) {
		getFJPAMethodHandler().handle(new ArrayCallHandler(new ValueHandler() {
			@Override
			public void handle(Proxy proxy, QueryBuilder queryBuilder, Selector selector) {
				queryBuilder.addGroupBy(selector);
			}
		}, values));
	}

	// JPA Functions
	public static Function count(Object object) {
		if (object instanceof Proxy) {
			setQuery((Proxy) object);
		}
		return getFJPAMethodHandler().handle(new CountFunctionHandler(object));
	}

	public static Function sum(Number number) {
		return getFJPAMethodHandler().handle(new SumFunctionHandler());
	}

	public static Function min(Number number) {
		return getFJPAMethodHandler().handle(new MinFunctionHandler());
	}

	public static Function max(Number number) {
		return getFJPAMethodHandler().handle(new MaxFunctionHandler());
	}

	public static Function avg(Number number) {
		return getFJPAMethodHandler().handle(new AvgFunctionHandler());
	}

	public static Function coalesce(Object... values) {
		final CoalesceFunction coalesceFunction = new CoalesceFunction();
		getFJPAMethodHandler().handle(new ArrayCallHandler(new ValueHandler() {
			@Override
			public void handle(Proxy proxy, QueryBuilder queryBuilder, Selector selector) {
				coalesceFunction.setQuery(proxy);
				coalesceFunction.addSelector(selector);
			}
		}, values));

		return coalesceFunction;
	}

	public static <T> Function<T> distinct(T object) {
		if (object instanceof Proxy) {
			setQuery((Proxy) object);
		}
		return getFJPAMethodHandler().handle(new DistinctFunctionHandler<T>(object));
	}

	public static void orderBy(Object... values) {
		getFJPAMethodHandler().handle(new ArrayCallHandler(new ValueHandler() {
			@Override
			public void handle(Proxy proxy, QueryBuilder queryBuilder, Selector selector) {
				queryBuilder.addOrder(selector);
			}
		}, values));

	}

	// orderBy function

	public static Function asc(Object object) {
		return getFJPAMethodHandler().handle(new AscFunctionHandler());
	}

	public static Function desc(Object object) {
		return getFJPAMethodHandler().handle(new DescFunctionHandler());
	}

	public static String query(Object proxy) {
		if (proxy instanceof Proxy) {
			Proxy from = (Proxy) proxy;
			return from.getFJPAMethodHandler().<QueryBuilder> getRoot().getQuery(new AtomicInteger());
		}
		return null;
	}

	public static Map<String, Object> params(Object proxy) {
		if (proxy instanceof Proxy) {
			Proxy from = (Proxy) proxy;
			return from.getFJPAMethodHandler().<QueryBuilder> getRoot().getParametersAsMap();
		}
		return null;
	}

	public static void setQuery(Proxy query) {
		FJPAQuery.query.set(query);
	}

	// TODO devrait se retrouver dans l'api interne

	public static FJPAMethodHandler getFJPAMethodHandler() {
		Proxy internalQuery = query.get();
		return internalQuery.getFJPAMethodHandler();
	}

}
