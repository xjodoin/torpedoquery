/**
 * Copyright (C) 2011 Xavier Jodoin (xavier@jodoin.me)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.torpedoquery.jpa;

import static org.torpedoquery.jpa.internal.TorpedoMagic.getTorpedoMethodHandler;
import static org.torpedoquery.jpa.internal.TorpedoMagic.setQuery;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.TorpedoMagic;
import org.torpedoquery.jpa.internal.TorpedoProxy;
import org.torpedoquery.jpa.internal.handlers.ArrayCallHandler;
import org.torpedoquery.jpa.internal.handlers.GroupingConditionHandler;
import org.torpedoquery.jpa.internal.handlers.InnerJoinHandler;
import org.torpedoquery.jpa.internal.handlers.LeftJoinHandler;
import org.torpedoquery.jpa.internal.handlers.RightJoinHandler;
import org.torpedoquery.jpa.internal.handlers.ValueHandler;
import org.torpedoquery.jpa.internal.handlers.WhereClauseHandler;
import org.torpedoquery.jpa.internal.joins.InnerJoinBuilder;
import org.torpedoquery.jpa.internal.joins.LeftJoinBuilder;
import org.torpedoquery.jpa.internal.joins.RightJoinBuilder;
import org.torpedoquery.jpa.internal.query.DefaultQueryBuilder;
import org.torpedoquery.jpa.internal.query.GroupBy;
import org.torpedoquery.jpa.internal.utils.DoNothingQueryConfigurator;
import org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler;
import org.torpedoquery.jpa.internal.utils.WhereQueryConfigurator;
import org.torpedoquery.jpa.internal.utils.WithQueryConfigurator;

import com.google.common.base.Throwables;

/**
 * Torpedo Query goal is to simplify how you create and maintain your HQL and
 * JPA-QL query.
 *
 *
 * First add this import static org.torpedoquery.jpa.Torpedo.*;
 *
 * 1. Create simple select
 *
 * final Entity entity = from(Entity.class);
 * org.torpedoquery.jpa.Query&lt;Entity&gt; select = select(entity);
 *
 * 2. Create scalar queries
 *
 * final Entity entity = from(Entity.class);
 * org.torpedoquery.jpa.Query&lt;String&gt; select = select(entity.getCode());
 *
 * 3. How to execute your query
 *
 * final Entity entity = from(Entity.class);
 * org.torpedoquery.jpa.Query&lt;Entity&gt; select = select(entity);
 * List&lt;Entity&gt; entityList = select.list(entityManager);
 *
 * 4. Create simple condition
 *
 * final Entity entity = from(Entity.class);
 * where(entity.getCode()).eq("mycode");
 * org.torpedoquery.jpa.Query&lt;Entity&gt; select = select(entity);
 *
 * 5. Create join on your entities
 *
 * final Entity entity = from(Entity.class); final SubEntity subEntity =
 * innerJoin(entity.getSubEntities());
 * org.torpedoquery.jpa.Query&lt;String[]&gt; select = select(entity.getCode(),
 * subEntity.getName());
 *
 * 6. Group your conditions
 *
 * Entity from = from(Entity.class); OnGoingLogicalCondition condition =
 * condition(from.getCode()).eq("test").or(from.getCode()).eq("test2");
 * where(from.getName()).eq("test").and(condition); Query&lt;Entity&gt; select =
 * select(from);
 *
 * @author xjodoin
 * @version $Id: $Id
 */
public class Torpedo extends TorpedoFunction {

	/**
	 *
	 * MyObject queryBuilder = from(MyObject.class);
	 *
	 * @return a query builder proxy
	 * @param toQuery
	 *            a {@link java.lang.Class} object.
	 * @param <T>
	 *            a T object.
	 */
	public static <T> T from(Class<T> toQuery) {
		try {

			DefaultQueryBuilder queryBuilder = new DefaultQueryBuilder(toQuery);
			TorpedoMethodHandler fjpaMethodHandler = new TorpedoMethodHandler(queryBuilder);

			T from = TorpedoMagic.getProxyfactoryfactory().createProxy(fjpaMethodHandler, TorpedoProxy.class, toQuery);

			fjpaMethodHandler.addQueryBuilder(from, queryBuilder);

			setQuery((TorpedoProxy) from);

			return from;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 *
	 * In HQL you can specify field is only in subclass
	 *
	 * Entity from = from(Entity.class); ExtendEntity extend = extend(from,
	 * ExtendEntity.class); where(extend.getSpecificField()).eq("test");
	 *
	 * @param toExtend
	 *            a T object.
	 * @param subclass
	 *            a {@link java.lang.Class} object.
	 * @param <T>
	 *            a T object.
	 * @return a E object.
	 */
	public static <T, E extends T> E extend(T toExtend, Class<E> subclass) {
		try {

			TorpedoMethodHandler fjpaMethodHandler = getTorpedoMethodHandler();
			E proxy = TorpedoMagic.getProxyfactoryfactory().createProxy(fjpaMethodHandler, TorpedoProxy.class, subclass);

			QueryBuilder queryBuilder = fjpaMethodHandler.getQueryBuilder(toExtend);
			fjpaMethodHandler.addQueryBuilder(proxy, queryBuilder);

			return proxy;

		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * <p>
	 * select.
	 * </p>
	 *
	 * @see #select(Object)
	 * @param value
	 *            a {@link org.torpedoquery.jpa.Function} object.
	 * @param <T>
	 *            a T object.
	 * @return a {@link org.torpedoquery.jpa.Query} object.
	 */
	public static <T> Query<T> select(Function<T> value) {
		return (Query<T>) Torpedo.select(new Object[] { value });
	}

	/**
	 *
	 * in TorpedoQuery the select method finalize your query.
	 *
	 * @return Return your unexecute Query object
	 * @param value
	 *            a T object.
	 * @param <T>
	 *            a T object.
	 */
	public static <T> Query<T> select(T value) {
		return (Query<T>) Torpedo.select(new Object[] { value });
	}

	/**
	 * <p>
	 * select.
	 * </p>
	 *
	 * @see #select(Object)
	 * @param values
	 *            a {@link java.lang.Object} object.
	 * @return a {@link org.torpedoquery.jpa.Query} object.
	 */
	public static Query<Object[]> select(Object... values) {
		TorpedoMethodHandler methodHandler = getTorpedoMethodHandler();

		for (int i = 0; i < values.length; i++) {

			Object param = values[i];

			if (param instanceof Function && !(param instanceof Query)) {
				Function function = (Function) param;
				TorpedoProxy proxy = (TorpedoProxy) function.getProxy();
				methodHandler = proxy.getTorpedoMethodHandler();
			} else if (param instanceof TorpedoProxy) {
				TorpedoProxy proxy = (TorpedoProxy) param;
				methodHandler = proxy.getTorpedoMethodHandler();
			}

		}

		final QueryBuilder root = methodHandler.getRoot();
		root.clearSelectors();

		methodHandler.handle(new ArrayCallHandler(new ValueHandler<Void>() {

			@Override
			public Void handle(TorpedoProxy query, QueryBuilder queryBuilder, Selector selector) {
				root.addSelector(selector);
				return null;
			}
		}, values));

		return root;

	}

	/**
	 * Create HQL inner join
	 *
	 * @return a query builder proxy
	 * @param toJoin
	 *            a T object.
	 * @param <T>
	 *            a T object.
	 */
	public static <T> T innerJoin(T toJoin) {
		return getTorpedoMethodHandler().handle(new InnerJoinHandler<T>(getTorpedoMethodHandler()));
	}

	/**
	 * Create HQL inner join
	 *
	 * @return a query builder proxy
	 * @param toJoin
	 *            a T object.
	 * @param realType
	 *            a {@link java.lang.Class} object.
	 * @param <T>
	 *            a T object.
	 */
	public static <T, E extends T> E innerJoin(T toJoin, Class<E> realType) {
		return getTorpedoMethodHandler().handle(new InnerJoinHandler<E>(getTorpedoMethodHandler(), realType));
	}

	/**
	 * <p>
	 * innerJoin.
	 * </p>
	 *
	 * @see #innerJoin(Object)
	 * @param toJoin
	 *            a {@link java.util.Collection} object.
	 * @param <T>
	 *            a T object.
	 * @return a T object.
	 */
	public static <T> T innerJoin(Collection<T> toJoin) {
		return getTorpedoMethodHandler()
				.handle(new InnerJoinHandler<T>(getTorpedoMethodHandler()));
	}

	/**
	 * <p>
	 * innerJoin.
	 * </p>
	 *
	 * @see #innerJoin(Object, Class)
	 * @param toJoin
	 *            a {@link java.util.Collection} object.
	 * @param realType
	 *            a {@link java.lang.Class} object.
	 * @param <T>
	 *            a T object.
	 * @return a E object.
	 */
	public static <T, E extends T> E innerJoin(Collection<T> toJoin, Class<E> realType) {
		return getTorpedoMethodHandler()
				.handle(new InnerJoinHandler<E>(getTorpedoMethodHandler(), realType));
	}

	/**
	 * <p>
	 * innerJoin.
	 * </p>
	 *
	 * @see #innerJoin(Object)
	 * @param toJoin
	 *            a {@link java.util.Map} object.
	 * @param <T>
	 *            a T object.
	 * @return a T object.
	 */
	public static <T> T innerJoin(Map<?, T> toJoin) {
		return getTorpedoMethodHandler()
				.handle(new InnerJoinHandler<T>(getTorpedoMethodHandler()));
	}

	/**
	 * <p>
	 * innerJoin.
	 * </p>
	 *
	 * @see #innerJoin(Object, Class)
	 * @param toJoin
	 *            a {@link java.util.Map} object.
	 * @param realType
	 *            a {@link java.lang.Class} object.
	 * @param <T>
	 *            a T object.
	 * @return a E object.
	 */
	public static <T, E extends T> E innerJoin(Map<?, T> toJoin, Class<E> realType) {
		return getTorpedoMethodHandler()
				.handle(new InnerJoinHandler<E>(getTorpedoMethodHandler(), realType));
	}
	
	/**
	 * <p>
	 * innerJoin.
	 * </p>
	 *
	 * @param entity class to join on 
	 * @return a query builder T object.
	 */
	public static <T> JoinBuilder<T> innerJoin(Class<T> toJoin) {
		return new InnerJoinBuilder<>(toJoin, getTorpedoMethodHandler());
	}
	
	/**
	 * <p>
	 * leftJoin.
	 * </p>
	 *
	 * @param entity class to join on 
	 * @return a query builder T object.
	 */
	public static <T> JoinBuilder<T> leftJoin(Class<T> toJoin) {
		return new LeftJoinBuilder<>(toJoin, getTorpedoMethodHandler());
	}
	
	/**
	 * <p>
	 * rightJoin.
	 * </p>
	 *
	 * @param entity class to join on 
	 * @return a query builder T object.
	 */
	public static <T> JoinBuilder<T> rightJoin(Class<T> toJoin) {
		return new RightJoinBuilder<>(toJoin, getTorpedoMethodHandler());
	}

	/**
	 * Create HQL left join
	 *
	 * @return a query builder proxy
	 * @param toJoin
	 *            a T object.
	 * @param <T>
	 *            a T object.
	 */
	public static <T> T leftJoin(T toJoin) {
		return getTorpedoMethodHandler().handle(new LeftJoinHandler<T>(getTorpedoMethodHandler()));
	}

	/**
	 * Create HQL left join
	 *
	 * @return a query builder proxy
	 * @param toJoin
	 *            a T object.
	 * @param realType
	 *            a {@link java.lang.Class} object.
	 * @param <T>
	 *            a T object.
	 */
	public static <T, E extends T> E leftJoin(T toJoin, Class<E> realType) {
		return getTorpedoMethodHandler()
				.handle(new LeftJoinHandler<E>(getTorpedoMethodHandler(), realType));
	}

	/**
	 * <p>
	 * leftJoin.
	 * </p>
	 *
	 * @see #leftJoin(Object)
	 * @param toJoin
	 *            a {@link java.util.Collection} object.
	 * @param <T>
	 *            a T object.
	 * @return a T object.
	 */
	public static <T> T leftJoin(Collection<T> toJoin) {
		return getTorpedoMethodHandler().handle(new LeftJoinHandler<T>(getTorpedoMethodHandler()));
	}

	/**
	 * <p>
	 * leftJoin.
	 * </p>
	 *
	 * @see #leftJoin(Object, Class)
	 * @param toJoin
	 *            a {@link java.util.Collection} object.
	 * @param realType
	 *            a {@link java.lang.Class} object.
	 * @param <T>
	 *            a T object.
	 * @return a E object.
	 */
	public static <T, E extends T> E leftJoin(Collection<T> toJoin, Class<E> realType) {
		return getTorpedoMethodHandler()
				.handle(new LeftJoinHandler<E>(getTorpedoMethodHandler(), realType));
	}

	/**
	 * <p>
	 * leftJoin.
	 * </p>
	 *
	 * @see #leftJoin(Object)
	 * @param toJoin
	 *            a {@link java.util.Map} object.
	 * @param <T>
	 *            a T object.
	 * @return a T object.
	 */
	public static <T> T leftJoin(Map<?, T> toJoin) {
		return getTorpedoMethodHandler().handle(new LeftJoinHandler<T>(getTorpedoMethodHandler()));
	}

	/**
	 * <p>
	 * leftJoin.
	 * </p>
	 *
	 * @see #leftJoin(Object, Class)
	 * @param toJoin
	 *            a {@link java.util.Map} object.
	 * @param realType
	 *            a {@link java.lang.Class} object.
	 * @param <T>
	 *            a T object.
	 * @return a E object.
	 */
	public static <T, E extends T> E leftJoin(Map<?, T> toJoin, Class<E> realType) {
		return getTorpedoMethodHandler()
				.handle(new LeftJoinHandler<E>(getTorpedoMethodHandler(), realType));
	}

	/**
	 * Create HQL right join
	 *
	 * @return a query builder proxy
	 * @param toJoin
	 *            a T object.
	 * @param <T>
	 *            a T object.
	 */
	public static <T> T rightJoin(T toJoin) {
		return getTorpedoMethodHandler()
				.handle(new RightJoinHandler<T>(getTorpedoMethodHandler()));
	}

	/**
	 * Create HQL right join
	 *
	 * @return a query builder proxy
	 * @param toJoin
	 *            a T object.
	 * @param realType
	 *            a {@link java.lang.Class} object.
	 * @param <T>
	 *            a T object.
	 */
	public static <T, E extends T> E rightJoin(T toJoin, Class<E> realType) {
		return getTorpedoMethodHandler()
				.handle(new RightJoinHandler<E>(getTorpedoMethodHandler(), realType));
	}

	/**
	 * <p>
	 * rightJoin.
	 * </p>
	 *
	 * @see #rightJoin(Object)
	 * @param toJoin
	 *            a {@link java.util.Collection} object.
	 * @param <T>
	 *            a T object.
	 * @return a T object.
	 */
	public static <T> T rightJoin(Collection<T> toJoin) {
		return getTorpedoMethodHandler()
				.handle(new RightJoinHandler<T>(getTorpedoMethodHandler()));
	}

	/**
	 * <p>
	 * rightJoin.
	 * </p>
	 *
	 * @see #rightJoin(Object, Class)
	 * @param toJoin
	 *            a {@link java.util.Collection} object.
	 * @param realType
	 *            a {@link java.lang.Class} object.
	 * @param <T>
	 *            a T object.
	 * @return a E object.
	 */
	public static <T, E extends T> E rightJoin(Collection<T> toJoin, Class<E> realType) {
		return getTorpedoMethodHandler()
				.handle(new RightJoinHandler<E>(getTorpedoMethodHandler(), realType));
	}

	/**
	 * <p>
	 * rightJoin.
	 * </p>
	 *
	 * @see #rightJoin(Object)
	 * @param toJoin
	 *            a {@link java.util.Map} object.
	 * @param <T>
	 *            a T object.
	 * @return a T object.
	 */
	public static <T> T rightJoin(Map<?, T> toJoin) {
		return getTorpedoMethodHandler()
				.handle(new RightJoinHandler<T>(getTorpedoMethodHandler()));
	}

	/**
	 * <p>
	 * rightJoin.
	 * </p>
	 *
	 * @see #rightJoin(Object, Class)
	 * @param toJoin
	 *            a {@link java.util.Map} object.
	 * @param realType
	 *            a {@link java.lang.Class} object.
	 * @param <T>
	 *            a T object.
	 * @return a E object.
	 */
	public static <T, E extends T> E rightJoin(Map<?, T> toJoin, Class<E> realType) {
		return getTorpedoMethodHandler()
				.handle(new RightJoinHandler<E>(getTorpedoMethodHandler(), realType));
	}

	/**
	 * Apply where condition to the query relative to the parameter.
	 *
	 * @return a condition builder
	 * @param object
	 *            a T object.
	 * @param <T>
	 *            a T object.
	 */
	public static <T> ValueOnGoingCondition<T> where(T object) {
		return getTorpedoMethodHandler().handle(new WhereClauseHandler<T, ValueOnGoingCondition<T>>(object));
	}

	/**
	 * Apply where condition to the query relative to the parameter.
	 *
	 * @param condition
	 *            group create by @see {@link #condition(Object)}
	 * @return a condition builder
	 * @param <T>
	 *            a T object.
	 */
	public static <T> OnGoingLogicalCondition where(OnGoingLogicalCondition condition) {
		return getTorpedoMethodHandler()
				.handle(new GroupingConditionHandler<T>(new WhereQueryConfigurator<T>(), condition));
	}

	/**
	 * <p>
	 * where.
	 * </p>
	 *
	 * @see #where(Object)
	 * @param object
	 *            a T object.
	 * @param <V>
	 *            a V object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingComparableCondition} object.
	 */
	public static <V, T extends Comparable<V>> OnGoingComparableCondition<V> where(T object) {
		return getTorpedoMethodHandler().handle(new WhereClauseHandler<V, OnGoingComparableCondition<V>>(object));
	}

	/**
	 * <p>
	 * where.
	 * </p>
	 *
	 * @see #where(Object)
	 * @param object
	 *            a {@link java.lang.String} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingStringCondition} object.
	 */
	public static OnGoingStringCondition<String> where(String object) {
		return getTorpedoMethodHandler().handle(new WhereClauseHandler<String, OnGoingStringCondition<String>>(object));
	}

	/**
	 * <p>
	 * where.
	 * </p>
	 *
	 * @param function
	 *            a {@link org.torpedoquery.jpa.Function} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingStringCondition} object.
	 */
	public static OnGoingStringCondition<String> where(Function<String> function) {
		return getTorpedoMethodHandler()
				.handle(new WhereClauseHandler<String, OnGoingStringCondition<String>>(function));
	}

	/**
	 * <p>
	 * where.
	 * </p>
	 *
	 * @see #where(Object)
	 * @param object
	 *            a {@link java.util.Collection} object.
	 * @param <T>
	 *            a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingCollectionCondition} object.
	 */
	public static <T> OnGoingCollectionCondition<T> where(Collection<T> object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<T, OnGoingCollectionCondition<T>>(object, new WhereQueryConfigurator<T>()));
	}

	/**
	 * <p>
	 * where.
	 * </p>
	 *
	 * @see #where(Object)
	 * @param object
	 *            a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 * @param <T>
	 *            a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingComparableCondition} object.
	 */
	public static <T> OnGoingComparableCondition<T> where(ComparableFunction<T> object) {
		return getTorpedoMethodHandler().handle(new WhereClauseHandler<T, OnGoingComparableCondition<T>>(object));
	}

	/**
	 * Create hql restriction directly onto hql join
	 *
	 * ex: inner join myObject.getMyJoinObject() joinObject with
	 * joinObject.myField is not null
	 *
	 * @return a condition builder
	 * @param object
	 *            a T object.
	 * @param <T>
	 *            a T object.
	 */
	public static <T> ValueOnGoingCondition<T> with(T object) {
		return getTorpedoMethodHandler()
				.handle(new WhereClauseHandler<T, ValueOnGoingCondition<T>>(object, new WithQueryConfigurator<T>()));
	}

	/**
	 * <p>
	 * with.
	 * </p>
	 *
	 * @see #with(Object)
	 * @param object
	 *            a T object.
	 * @param <V>
	 *            a V object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingComparableCondition} object.
	 */
	public static <V, T extends Comparable<V>> OnGoingComparableCondition<V> with(T object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<V, OnGoingComparableCondition<V>>(object, new WithQueryConfigurator<V>()));
	}

	/**
	 * <p>
	 * with.
	 * </p>
	 *
	 * @see #with(Object)
	 * @param object
	 *            a {@link java.lang.String} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingStringCondition} object.
	 */
	public static OnGoingStringCondition<String> with(String object) {
		return getTorpedoMethodHandler().handle(new WhereClauseHandler<String, OnGoingStringCondition<String>>(object,
				new WithQueryConfigurator<String>()));
	}

	/**
	 * <p>
	 * with.
	 * </p>
	 *
	 * @see #with(Object)
	 * @param object
	 *            a {@link java.util.Collection} object.
	 * @param <T>
	 *            a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingCollectionCondition} object.
	 */
	public static <T> OnGoingCollectionCondition<T> with(Collection<T> object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<T, OnGoingCollectionCondition<T>>(object, new WithQueryConfigurator<T>()));
	}

	/**
	 * <p>
	 * with.
	 * </p>
	 *
	 * @see #with(Object)
	 * @param condition
	 *            a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 * @param <T>
	 *            a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	public static <T> OnGoingLogicalCondition with(OnGoingLogicalCondition condition) {
		return getTorpedoMethodHandler()
				.handle(new GroupingConditionHandler<T>(new WithQueryConfigurator<T>(), condition));
	}

	/**
	 * Create a group condition ex: (myObject.myField is not null and
	 * myObject.intField &gt; 2)
	 *
	 * this condition is not bind to any query builder until you pass it to
	 * the @see {@link #where(Object)} or to a ValueOnGoingCondition create by a
	 * where
	 *
	 * @return a condition builder
	 * @param object
	 *            a T object.
	 * @param <T>
	 *            a T object.
	 */
	public static <T> ValueOnGoingCondition<T> condition(T object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<T, ValueOnGoingCondition<T>>(object, new DoNothingQueryConfigurator<T>()));
	}

	/**
	 * <p>
	 * condition.
	 * </p>
	 *
	 * @see #condition(Object)
	 * @param object
	 *            a T object.
	 * @param <V>
	 *            a V object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingComparableCondition} object.
	 */
	public static <V, T extends Comparable<V>> OnGoingComparableCondition<V> condition(T object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<V, OnGoingComparableCondition<V>>(object, new DoNothingQueryConfigurator<V>()));
	}

	/**
	 * <p>
	 * condition.
	 * </p>
	 *
	 * @see #condition(Object)
	 * @param object
	 *            a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 * @param <V>
	 *            a V object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingComparableCondition} object.
	 */
	public static <V, T extends Comparable<V>> OnGoingComparableCondition<V> condition(ComparableFunction<T> object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<V, OnGoingComparableCondition<V>>(object, new DoNothingQueryConfigurator<V>()));
	}

	/**
	 * <p>
	 * condition.
	 * </p>
	 *
	 * @see #condition(Object)
	 * @param object
	 *            a {@link java.lang.String} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingStringCondition} object.
	 */
	public static OnGoingStringCondition<String> condition(String object) {
		return getTorpedoMethodHandler().handle(new WhereClauseHandler<String, OnGoingStringCondition<String>>(object,
				new DoNothingQueryConfigurator<String>()));
	}

	/**
	 * <p>
	 * condition.
	 * </p>
	 *
	 * @see #condition(Object)
	 * @param object
	 *            a {@link org.torpedoquery.jpa.Function} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingStringCondition} object.
	 */
	public static OnGoingStringCondition<String> condition(Function<String> object) {
		return getTorpedoMethodHandler().handle(new WhereClauseHandler<String, OnGoingStringCondition<String>>(object,
				new DoNothingQueryConfigurator<String>()));
	}

	/**
	 * <p>
	 * condition.
	 * </p>
	 *
	 * @see #condition(Object)
	 * @param object
	 *            a {@link java.util.Collection} object.
	 * @param <T>
	 *            a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingCollectionCondition} object.
	 */
	public static <T> OnGoingCollectionCondition<T> condition(Collection<T> object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<T, OnGoingCollectionCondition<T>>(object, new DoNothingQueryConfigurator<T>()));
	}

	/**
	 * <p>
	 * condition.
	 * </p>
	 *
	 * @see #condition(Object)
	 * @param condition
	 *            a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 * @param <T>
	 *            a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	public static <T> OnGoingLogicalCondition condition(OnGoingLogicalCondition condition) {
		return getTorpedoMethodHandler()
				.handle(new GroupingConditionHandler<T>(new DoNothingQueryConfigurator<T>(), condition));
	}

	/**
	 * <p>
	 * and.
	 * </p>
	 *
	 * @param conditions
	 *            a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	public static OnGoingLogicalCondition and(OnGoingLogicalCondition... conditions) {
		return and(Arrays.asList(conditions));
	}

	/**
	 * Group expressions together in a single conjunction (A and B and C...)
	 *
	 * @param conditions
	 *            a {@link java.lang.Iterable} object.
	 * @return OnGoingLogicalCondition
	 */
	public static OnGoingLogicalCondition and(Iterable<OnGoingLogicalCondition> conditions) {
		OnGoingLogicalCondition andCondition = null;

		for (OnGoingLogicalCondition condition : conditions) {
			if (andCondition == null) {
				andCondition = condition(condition);
			} else {
				andCondition.and(condition);
			}
		}

		return andCondition;
	}

	/**
	 * Group expressions together in a single disjunction (A or B or C...)
	 *
	 * @param conditions
	 *            a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 * @return OnGoingLogicalCondition
	 */
	public static OnGoingLogicalCondition or(OnGoingLogicalCondition... conditions) {
		return or(Arrays.asList(conditions));
	}

	/**
	 * <p>
	 * or.
	 * </p>
	 *
	 * @param conditions
	 *            a {@link java.lang.Iterable} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	public static OnGoingLogicalCondition or(Iterable<OnGoingLogicalCondition> conditions) {
		OnGoingLogicalCondition orCondition = null;

		for (OnGoingLogicalCondition condition : conditions) {
			if (orCondition == null) {
				orCondition = condition(condition);
			} else {
				orCondition.or(condition);
			}
		}

		return orCondition;
	}

	/**
	 * Add group by to the relative query builder
	 *
	 * @return group by condition builder
	 * @param values
	 *            a {@link java.lang.Object} object.
	 */
	public static OnGoingGroupByCondition groupBy(Object... values) {

		TorpedoMethodHandler fjpaMethodHandler = getTorpedoMethodHandler();
		final QueryBuilder root = fjpaMethodHandler.getRoot();
		final GroupBy groupBy = new GroupBy();

		fjpaMethodHandler.handle(new ArrayCallHandler(new ValueHandler<Void>() {
			@Override
			public Void handle(TorpedoProxy proxy, QueryBuilder queryBuilder, Selector selector) {
				groupBy.addGroup(selector);
				return null;
			}
		}, values));

		root.setGroupBy(groupBy);
		return groupBy;
	}

	/**
	 * Add order by to the relative query
	 *
	 * @param values
	 *            a {@link java.lang.Object} object.
	 */
	public static void orderBy(Object... values) {
		getTorpedoMethodHandler().handle(new ArrayCallHandler(new ValueHandler<Void>() {
			@Override
			public Void handle(TorpedoProxy proxy, QueryBuilder queryBuilder, Selector selector) {
				queryBuilder.addOrder(selector);
				return null;
			}
		}, values));

	}

	/**
	 * <p>
	 * param.
	 * </p>
	 *
	 * @param param
	 *            a T object.
	 * @param <T>
	 *            a T object.
	 * @return a T object.
	 */
	public static <T> T param(T param) {
		getTorpedoMethodHandler().addParam(param);
		return param;
	}

}
