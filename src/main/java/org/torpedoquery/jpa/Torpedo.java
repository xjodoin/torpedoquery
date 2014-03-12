/**
 *   Copyright Xavier Jodoin xjodoin@torpedoquery.org
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.torpedoquery.jpa;

import static org.torpedoquery.jpa.internal.TorpedoMagic.getTorpedoMethodHandler;
import static org.torpedoquery.jpa.internal.TorpedoMagic.setQuery;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.TorpedoProxy;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.conditions.LogicalCondition;
import org.torpedoquery.jpa.internal.handlers.ArrayCallHandler;
import org.torpedoquery.jpa.internal.handlers.GroupingConditionHandler;
import org.torpedoquery.jpa.internal.handlers.InnerJoinHandler;
import org.torpedoquery.jpa.internal.handlers.LeftJoinHandler;
import org.torpedoquery.jpa.internal.handlers.RightJoinHandler;
import org.torpedoquery.jpa.internal.handlers.ValueHandler;
import org.torpedoquery.jpa.internal.handlers.WhereClauseHandler;
import org.torpedoquery.jpa.internal.query.DefaultQueryBuilder;
import org.torpedoquery.jpa.internal.query.GroupBy;
import org.torpedoquery.jpa.internal.utils.DoNothingQueryConfigurator;
import org.torpedoquery.jpa.internal.utils.MultiClassLoaderProvider;
import org.torpedoquery.jpa.internal.utils.ProxyFactoryFactory;
import org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler;
import org.torpedoquery.jpa.internal.utils.WhereQueryConfigurator;
import org.torpedoquery.jpa.internal.utils.WithQueryConfigurator;

import com.google.common.base.Throwables;

/**
 * Torpedo Query goal is to simplify how you create and maintain your HQL query.
 * (http://docs.jboss.org/hibernate/core/3.3/reference/en/html/queryhql.html)
 * 
 * (All following examples are extract from Torpedo's Tests cases)
 * 
 * First add this import static org.torpedoquery.jpa.Torpedo.*;
 * 
 * 1. Create simple select
 * 
 * final Entity entity = from(Entity.class); org.torpedoquery.jpa.Query<Entity>
 * select = select(entity);
 * 
 * 2. Create scalar queries
 * 
 * final Entity entity = from(Entity.class); org.torpedoquery.jpa.Query<String>
 * select = select(entity.getCode());
 * 
 * 3. How to execute your query
 * 
 * final Entity entity = from(Entity.class); org.torpedoquery.jpa.Query<Entity>
 * select = select(entity); List<Entity> entityList =
 * select.list(entityManager);
 * 
 * 4. Create simple condition
 * 
 * final Entity entity = from(Entity.class);
 * where(entity.getCode()).eq("mycode"); org.torpedoquery.jpa.Query<Entity>
 * select = select(entity);
 * 
 * 5. Create join on your entities
 * 
 * final Entity entity = from(Entity.class); final SubEntity subEntity =
 * innerJoin(entity.getSubEntities()); org.torpedoquery.jpa.Query<String[]>
 * select = select(entity.getCode(), subEntity.getName());
 * 
 * 6. Group your conditions
 * 
 * Entity from = from(Entity.class); OnGoingLogicalCondition condition =
 * condition(from.getCode()).eq("test").or(from.getCode()).eq("test2");
 * where(from.getName()).eq("test").and(condition); Query<Entity> select =
 * select(from);
 * 
 * 
 */
public class Torpedo extends TorpedoFunction {

	private static final ProxyFactoryFactory proxyFactoryFactory = new ProxyFactoryFactory(
			new MultiClassLoaderProvider());

	/**
	 * 
	 * MyObject queryBuilder = from(MyObject.class);
	 * 
	 * @param your
	 *            entity class you want to create your query
	 * @return a query builder proxy
	 * 
	 */
	public static <T> T from(Class<T> toQuery) {
		try {

			DefaultQueryBuilder queryBuilder = new DefaultQueryBuilder(toQuery);
			TorpedoMethodHandler fjpaMethodHandler = new TorpedoMethodHandler(
					queryBuilder, proxyFactoryFactory);

			T from = proxyFactoryFactory.createProxy(fjpaMethodHandler,
					TorpedoProxy.class, toQuery);

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
	 * @param subclass
	 * @return
	 */
	public static <T, E extends T> E extend(T toExtend, Class<E> subclass) {
		try {

			TorpedoMethodHandler fjpaMethodHandler = getTorpedoMethodHandler();
			E proxy = proxyFactoryFactory.createProxy(fjpaMethodHandler,
					TorpedoProxy.class, subclass);

			QueryBuilder queryBuilder = fjpaMethodHandler
					.getQueryBuilder(toExtend);
			fjpaMethodHandler.addQueryBuilder(proxy, queryBuilder);

			return proxy;

		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * @see #select(Object)
	 */
	public static <T> Query<T> select(Function<T> value) {
		return (Query<T>) Torpedo.select(new Object[] { value });
	}

	/**
	 * 
	 * in TorpedoQuery the select method finalize your query.
	 * 
	 * @param values
	 *            ex: myObject.getMyField()
	 * @return Return your unexecute Query object
	 * 
	 */
	public static <T> Query<T> select(T value) {
		return (Query<T>) Torpedo.select(new Object[] { value });
	}

	/**
	 * @see #select(Object)
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

		final QueryBuilder<Object[]> root = methodHandler.getRoot();

		methodHandler.handle(new ArrayCallHandler(new ValueHandler<Void>() {

			@Override
			public Void handle(TorpedoProxy query, QueryBuilder queryBuilder,
					Selector selector) {
				root.addSelector(selector);
				return null;
			}
		}, values));

		return root;

	}

	/**
	 * Create HQL inner join
	 * 
	 * @param field
	 *            to join on
	 * @return a query builder proxy
	 */
	public static <T> T innerJoin(T toJoin) {
		return getTorpedoMethodHandler().handle(
				new InnerJoinHandler<T>(getTorpedoMethodHandler(),
						proxyFactoryFactory));
	}

	/**
	 * Create HQL inner join
	 * 
	 * @param field
	 *            to join on
	 * @param define
	 *            the implementation class you want to work with
	 * @return a query builder proxy
	 */
	public static <T, E extends T> E innerJoin(T toJoin, Class<E> realType) {
		return getTorpedoMethodHandler().handle(
				new InnerJoinHandler<E>(getTorpedoMethodHandler(),
						proxyFactoryFactory, realType));
	}

	/**
	 * @see #innerJoin(Object)
	 */
	public static <T> T innerJoin(Collection<T> toJoin) {
		return getTorpedoMethodHandler().handle(
				new InnerJoinHandler<T>(getTorpedoMethodHandler(),
						proxyFactoryFactory));
	}

	/**
	 * @see #innerJoin(Object, Class)
	 */
	public static <T, E extends T> E innerJoin(Collection<T> toJoin,
			Class<E> realType) {
		return getTorpedoMethodHandler().handle(
				new InnerJoinHandler<E>(getTorpedoMethodHandler(),
						proxyFactoryFactory, realType));
	}

	/**
	 * @see #innerJoin(Object)
	 */
	public static <T> T innerJoin(Map<?, T> toJoin) {
		return getTorpedoMethodHandler().handle(
				new InnerJoinHandler<T>(getTorpedoMethodHandler(),
						proxyFactoryFactory));
	}

	/**
	 * @see #innerJoin(Object, Class)
	 */
	public static <T, E extends T> E innerJoin(Map<?, T> toJoin,
			Class<E> realType) {
		return getTorpedoMethodHandler().handle(
				new InnerJoinHandler<E>(getTorpedoMethodHandler(),
						proxyFactoryFactory, realType));
	}

	/**
	 * Create HQL left join
	 * 
	 * @param field
	 *            to join on
	 * @return a query builder proxy
	 */
	public static <T> T leftJoin(T toJoin) {
		return getTorpedoMethodHandler().handle(
				new LeftJoinHandler<T>(getTorpedoMethodHandler(),
						proxyFactoryFactory));
	}

	/**
	 * Create HQL left join
	 * 
	 * @param field
	 *            to join on
	 * @param define
	 *            the implementation class you want to work with
	 * @return a query builder proxy
	 */
	public static <T, E extends T> E leftJoin(T toJoin, Class<E> realType) {
		return getTorpedoMethodHandler().handle(
				new LeftJoinHandler<E>(getTorpedoMethodHandler(),
						proxyFactoryFactory, realType));
	}

	/**
	 * @see #leftJoin(Object)
	 */
	public static <T> T leftJoin(Collection<T> toJoin) {
		return getTorpedoMethodHandler().handle(
				new LeftJoinHandler<T>(getTorpedoMethodHandler(),
						proxyFactoryFactory));
	}

	/**
	 * @see #leftJoin(Object, Class)
	 */
	public static <T, E extends T> E leftJoin(Collection<T> toJoin,
			Class<E> realType) {
		return getTorpedoMethodHandler().handle(
				new LeftJoinHandler<E>(getTorpedoMethodHandler(),
						proxyFactoryFactory, realType));
	}

	/**
	 * @see #leftJoin(Object)
	 */
	public static <T> T leftJoin(Map<?, T> toJoin) {
		return getTorpedoMethodHandler().handle(
				new LeftJoinHandler<T>(getTorpedoMethodHandler(),
						proxyFactoryFactory));
	}

	/**
	 * @see #leftJoin(Object, Class)
	 */
	public static <T, E extends T> E leftJoin(Map<?, T> toJoin,
			Class<E> realType) {
		return getTorpedoMethodHandler().handle(
				new LeftJoinHandler<E>(getTorpedoMethodHandler(),
						proxyFactoryFactory, realType));
	}

	/**
	 * Create HQL right join
	 * 
	 * @param field
	 *            to join on
	 * @return a query builder proxy
	 */
	public static <T> T rightJoin(T toJoin) {
		return getTorpedoMethodHandler().handle(
				new RightJoinHandler<T>(getTorpedoMethodHandler(),
						proxyFactoryFactory));
	}

	/**
	 * Create HQL right join
	 * 
	 * @param field
	 *            to join on
	 * @param define
	 *            the implementation class you want to work with
	 * @return a query builder proxy
	 */
	public static <T, E extends T> E rightJoin(T toJoin, Class<E> realType) {
		return getTorpedoMethodHandler().handle(
				new RightJoinHandler<E>(getTorpedoMethodHandler(),
						proxyFactoryFactory, realType));
	}

	/**
	 * @see #rightJoin(Object)
	 */
	public static <T> T rightJoin(Collection<T> toJoin) {
		return getTorpedoMethodHandler().handle(
				new RightJoinHandler<T>(getTorpedoMethodHandler(),
						proxyFactoryFactory));
	}

	/**
	 * @see #rightJoin(Object, Class)
	 */
	public static <T, E extends T> E rightJoin(Collection<T> toJoin,
			Class<E> realType) {
		return getTorpedoMethodHandler().handle(
				new RightJoinHandler<E>(getTorpedoMethodHandler(),
						proxyFactoryFactory, realType));
	}

	/**
	 * @see #rightJoin(Object)
	 */
	public static <T> T rightJoin(Map<?, T> toJoin) {
		return getTorpedoMethodHandler().handle(
				new RightJoinHandler<T>(getTorpedoMethodHandler(),
						proxyFactoryFactory));
	}

	/**
	 * @see #rightJoin(Object, Class)
	 */
	public static <T, E extends T> E rightJoin(Map<?, T> toJoin,
			Class<E> realType) {
		return getTorpedoMethodHandler().handle(
				new RightJoinHandler<E>(getTorpedoMethodHandler(),
						proxyFactoryFactory, realType));
	}

	/**
	 * Apply where condition to the query relative to the parameter.
	 * 
	 * @param this could be a the return of the query builder method call or the
	 *        query builder itself ex: myObject.getMyField() or myObject
	 * @return a condition builder
	 */
	public static <T> ValueOnGoingCondition<T> where(T object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<T, ValueOnGoingCondition<T>>(object));
	}

	/**
	 * Apply where condition to the query relative to the parameter.
	 * 
	 * @param condition
	 *            group create by @see {@link #condition(Object)}
	 * @return a condition builder
	 */
	public static <T> OnGoingLogicalCondition where(
			OnGoingLogicalCondition condition) {
		return getTorpedoMethodHandler().handle(
				new GroupingConditionHandler<T>(
						new WhereQueryConfigurator<T>(), condition));
	}

	/**
	 * @see #where(Object)
	 */
	public static <V, T extends Comparable<V>> OnGoingComparableCondition<V> where(
			T object) {
		return getTorpedoMethodHandler()
				.handle(new WhereClauseHandler<V, OnGoingComparableCondition<V>>(
						object));
	}

	/**
	 * @see #where(Object)
	 */
	public static OnGoingStringCondition<String> where(String object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<String, OnGoingStringCondition<String>>(
						object));
	}

	public static OnGoingStringCondition<String> where(Function<String> function) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<String, OnGoingStringCondition<String>>(
						function));
	}

	/**
	 * @see #where(Object)
	 */
	public static <T> OnGoingCollectionCondition<T> where(Collection<T> object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<T, OnGoingCollectionCondition<T>>(
						object, new WhereQueryConfigurator<T>()));
	}

	/**
	 * @see #where(Object)
	 */
	public static <T> OnGoingComparableCondition<T> where(
			ComparableFunction<T> object) {
		return getTorpedoMethodHandler()
				.handle(new WhereClauseHandler<T, OnGoingComparableCondition<T>>(
						object));
	}

	/**
	 * Create hql restriction directly onto hql join
	 * 
	 * ex: inner join myObject.getMyJoinObject() joinObject with
	 * joinObject.myField is not null
	 * 
	 * @param this could be a the return of the query builder method call or the
	 *        query builder itself ex: myObject.getMyField() or myObject
	 * @return a condition builder
	 */
	public static <T> ValueOnGoingCondition<T> with(T object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<T, ValueOnGoingCondition<T>>(object,
						new WithQueryConfigurator<T>()));
	}

	/**
	 * @see #with(Object)
	 */
	public static <V, T extends Comparable<V>> OnGoingComparableCondition<V> with(
			T object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<V, OnGoingComparableCondition<V>>(
						object, new WithQueryConfigurator<V>()));
	}

	/**
	 * @see #with(Object)
	 */
	public static OnGoingStringCondition<String> with(String object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<String, OnGoingStringCondition<String>>(
						object, new WithQueryConfigurator<String>()));
	}

	/**
	 * @see #with(Object)
	 */
	public static <T> OnGoingCollectionCondition<T> with(Collection<T> object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<T, OnGoingCollectionCondition<T>>(
						object, new WithQueryConfigurator<T>()));
	}

	/**
	 * @see #with(Object)
	 */
	public static <T> OnGoingLogicalCondition with(
			OnGoingLogicalCondition condition) {
		return getTorpedoMethodHandler().handle(
				new GroupingConditionHandler<T>(new WithQueryConfigurator<T>(),
						condition));
	}

	/**
	 * Create a group condition ex: (myObject.myField is not null and
	 * myObject.intField > 2)
	 * 
	 * this condition is not bind to any query builder until you pass it to the @see
	 * {@link #where(Object)} or to a ValueOnGoingCondition create by a where
	 * 
	 * @param this could be a the return of the query builder method call or the
	 *        query builder itself ex: myObject.getMyField() or myObject
	 * @return a condition builder
	 */
	public static <T> ValueOnGoingCondition<T> condition(T object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<T, ValueOnGoingCondition<T>>(object,
						new DoNothingQueryConfigurator<T>()));
	}

	/**
	 * @see #condition(Object)
	 */
	public static <V, T extends Comparable<V>> OnGoingComparableCondition<V> condition(
			T object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<V, OnGoingComparableCondition<V>>(
						object, new DoNothingQueryConfigurator<V>()));
	}

	/**
	 * @see #condition(Object)
	 */
	public static <V, T extends Comparable<V>> OnGoingComparableCondition<V> condition(
			ComparableFunction<T> object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<V, OnGoingComparableCondition<V>>(
						object, new DoNothingQueryConfigurator<V>()));
	}

	/**
	 * @see #condition(Object)
	 */
	public static OnGoingStringCondition<String> condition(String object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<String, OnGoingStringCondition<String>>(
						object, new DoNothingQueryConfigurator<String>()));
	}

	/**
	 * @see #condition(Object)
	 */
	public static OnGoingStringCondition<String> condition(
			Function<String> object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<String, OnGoingStringCondition<String>>(
						object, new DoNothingQueryConfigurator<String>()));
	}

	/**
	 * @see #condition(Object)
	 */
	public static <T> OnGoingCollectionCondition<T> condition(
			Collection<T> object) {
		return getTorpedoMethodHandler().handle(
				new WhereClauseHandler<T, OnGoingCollectionCondition<T>>(
						object, new DoNothingQueryConfigurator<T>()));
	}

	/**
	 * @see #condition(Object)
	 */
	public static <T> OnGoingLogicalCondition condition(
			OnGoingLogicalCondition condition) {
		return getTorpedoMethodHandler().handle(
				new GroupingConditionHandler<T>(
						new DoNothingQueryConfigurator<T>(), condition));
	}

	public static OnGoingLogicalCondition and(
			OnGoingLogicalCondition... conditions) {
		return and(Arrays.asList(conditions));
	}

	/**
	 * Group expressions together in a single conjunction (A and B and C...)
	 * 
	 * @param conditions
	 * @return OnGoingLogicalCondition
	 */
	public static OnGoingLogicalCondition and(
			Iterable<OnGoingLogicalCondition> conditions) {
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
	 * @return OnGoingLogicalCondition
	 */
	public static OnGoingLogicalCondition or(
			OnGoingLogicalCondition... conditions) {
		return or(Arrays.asList(conditions));
	}

	public static OnGoingLogicalCondition or(
			Iterable<OnGoingLogicalCondition> conditions) {
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
	 * @param fields
	 *            you want to group by on
	 * @return group by condition builder
	 */
	public static OnGoingGroupByCondition groupBy(Object... values) {

		TorpedoMethodHandler fjpaMethodHandler = getTorpedoMethodHandler();
		final QueryBuilder root = fjpaMethodHandler.getRoot();
		final GroupBy groupBy = new GroupBy();

		fjpaMethodHandler.handle(new ArrayCallHandler(new ValueHandler<Void>() {
			@Override
			public Void handle(TorpedoProxy proxy, QueryBuilder queryBuilder,
					Selector selector) {
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
	 * @param fields
	 *            you want to order by on
	 */
	public static void orderBy(Object... values) {
		getTorpedoMethodHandler().handle(
				new ArrayCallHandler(new ValueHandler<Void>() {
					@Override
					public Void handle(TorpedoProxy proxy,
							QueryBuilder queryBuilder, Selector selector) {
						queryBuilder.addOrder(selector);
						return null;
					}
				}, values));

	}

}
