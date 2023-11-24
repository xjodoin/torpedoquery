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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.core.TorpedoQueryException;
import org.torpedoquery.jpa.internal.Condition;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.TorpedoMagic;
import org.torpedoquery.jpa.internal.TorpedoProxy;
import org.torpedoquery.jpa.internal.conditions.EmptyLogicalCondition;
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
import org.torpedoquery.jpa.internal.query.DefaultQuery;
import org.torpedoquery.jpa.internal.query.DefaultQueryBuilder;
import org.torpedoquery.jpa.internal.query.GroupBy;
import org.torpedoquery.jpa.internal.query.OrderBy;
import org.torpedoquery.jpa.internal.utils.DoNothingQueryConfigurator;
import org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler;
import org.torpedoquery.jpa.internal.utils.WhereQueryConfigurator;
import org.torpedoquery.jpa.internal.utils.WithQueryConfigurator;

/**
 * The Torpedo class serves as the primary interface for constructing and executing
 * database queries in a fluent, intuitive manner using the JPA (Java Persistence API).
 * This class provides static methods to initiate query building, apply various query 
 * operations like joins, selections, and conditions, and to execute these queries 
 * against a JPA-compliant database.
 *
 * The design of Torpedo emphasizes a fluent API, enabling the creation of complex 
 * queries through a chainable method syntax. This approach simplifies the process 
 * of query construction, making it more intuitive and readable.
 *
 * Usage examples:
 * <pre>{@code
 * // Example of building a simple query
 * Query<Entity> query = Torpedo.from(Entity.class)
 *                               .where(entity.getField())
 *                               .eq("value")
 *                               .select(entity);
 *
 * // Example of executing a query
 * List<Entity> result = query.list();
 * }</pre>
 *
 * The Torpedo class is central to the library and is typically the starting point
 * for most query operations. It is designed to work seamlessly with the underlying
 * JPA implementation, abstracting much of the complexity involved in query construction.
 */
public class Torpedo extends TorpedoFunction {

	/**
	 * Initializes and returns a query object for the specified entity class.
	 * This method is a starting point for building a query on the given entity.
	 * 
	 * @param entityClass the entity class to create the query for
	 * @return an initialized query object for the specified entity class
	 */
	public static <T> T from(Class<T> entityClass) {
		try {

			DefaultQueryBuilder queryBuilder = new DefaultQueryBuilder(entityClass);
			TorpedoMethodHandler fjpaMethodHandler = new TorpedoMethodHandler(queryBuilder);

			T from = TorpedoMagic.getProxyfactoryfactory().createProxy(fjpaMethodHandler, TorpedoProxy.class, entityClass);

			fjpaMethodHandler.addQueryBuilder(from, queryBuilder);

			setQuery((TorpedoProxy) from);

			return from;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates a subclass extension of the specified entity. This method is used in scenarios where
	 * a field is only present in a subclass and not in the base class. It allows for querying
	 * specific fields available in the subclass that are not part of the base entity.
	 *
	 * For example, in an HQL query:
	 * <pre>{@code
	 * Entity from = from(Entity.class);
	 * ExtendEntity extend = extend(from, ExtendEntity.class);
	 * where(extend.getSpecificField()).eq("test");
	 * }</pre>
	 *
	 * @param <T>      the type of the base entity
	 * @param <E>      the type of the subclass entity extending the base entity
	 * @param toExtend the instance of the base entity class to be extended
	 * @param subclass the subclass to extend the base entity with
	 * @return an instance of the subclass entity extending the base entity
	 * @throws RuntimeException if any internal operation fails
	 */
	public static <T, E extends T> E extend(T toExtend, Class<E> subclass) {
		try {

			TorpedoMethodHandler fjpaMethodHandler = getTorpedoMethodHandler(toExtend);
			E proxy = TorpedoMagic.getProxyfactoryfactory().createProxy(fjpaMethodHandler, TorpedoProxy.class,
					subclass);

			QueryBuilder queryBuilder = fjpaMethodHandler.getQueryBuilder(toExtend);
			fjpaMethodHandler.addQueryBuilder(proxy, queryBuilder);

			return proxy;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Constructs a query with the specified function as the selection target.
	 * This method is used to create a query that selects a specific data point or
	 * computation result, as defined by the provided function. It is a key component
	 * in building dynamic queries in the TorpedoQuery framework.
	 *
	 * For instance, to select the average value of a field, you could use:
	 * <pre>{@code
	 * Query<Double> query = select(avg(myEntity.myField));
	 * }</pre>
	 *
	 * This method is an overload of {@link #select(Object)} specifically tailored
	 * for use with functions.
	 *
	 * @param <T>   the type of the result expected from the function
	 * @param value the function to be used in the select clause of the query
	 * @return a {@link org.torpedoquery.jpa.Query} object representing the constructed query
	 */
	public static <T> Query<T> select(Function<T> value) {
		return (Query<T>) Torpedo.select(new Object[] { value });
	}


	/**
	 * Finalizes and returns a query object based on the specified selection criteria.
	 * In the TorpedoQuery framework, this method is used to complete the construction
	 * of a query by defining the selection target. It is versatile, allowing for various
	 * types of selection criteria, including entity fields, functions, or other query components.
	 *
	 * For example, to select an entire entity:
	 * <pre>{@code
	 * Query<MyEntity> query = select(myEntity);
	 * }</pre>
	 *
	 * This method is essential in building a query and dictates what the query will retrieve
	 * or compute when executed.
	 *
	 * @param <T>   the type of the selection target
	 * @param value the selection target (e.g., an entity, a field, a function)
	 * @return a {@link org.torpedoquery.jpa.Query} object, representing the constructed
	 *         and unexecuted query
	 */
	public static <T> Query<T> select(T value) {
		return (Query<T>) Torpedo.select(new Object[] { value });
	}


	/**
	 * Constructs a query based on multiple selection criteria.
	 * This method in the TorpedoQuery framework allows for selecting multiple elements
	 * within a single query. It can handle various selection types, including entity fields 
	 * accessed through getter methods, functions, or other query components. This makes it 
	 * versatile for constructing complex queries.
	 *
	 * For example, to select multiple fields using their getter methods or to include functions:
	 * <pre>{@code
	 * Query<Object[]> query = select(entity.getName(), entity.getAge(), sum(entity.getSalary()));
	 * }</pre>
	 *
	 * This method is especially useful for queries that require multiple selection targets
	 * and provides flexibility in combining different types of selections.
	 *
	 * @param values the selection targets, which can include entity fields accessed via getters,
	 *               functions, or other query components
	 * @return a {@link org.torpedoquery.jpa.Query} object representing the constructed query
	 *         with multiple selections
	 * @throws TorpedoQueryException if any issues are encountered during query construction
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

		if (methodHandler == null) {
			throw new TorpedoQueryException("Unable to find torpedo query reference");
		}

		final QueryBuilder root = methodHandler.getRoot();
		List<Selector> selectors = new ArrayList<Selector>();

		methodHandler.handle(new ArrayCallHandler(new ValueHandler<Void>() {

			@Override
			public Void handle(TorpedoProxy query, QueryBuilder queryBuilder, Selector selector) {
				selectors.add(selector);
				return null;
			}
		}, values));

		return new DefaultQuery<Object[]>(root, selectors);

	}

	/**
	 * Creates an HQL inner join on the specified entity or field.
	 * This method is used within the TorpedoQuery framework to construct an inner join clause
	 * in an HQL (Hibernate Query Language) query. It allows for linking related entities or fields
	 * based on a specified condition, which should be defined in subsequent parts of the query building process.
	 *
	 * For instance, to create an inner join with another entity:
	 * <pre>{@code
	 * Entity entity = from(Entity.class);
	 * RelatedEntity related = innerJoin(entity.getRelatedEntity());
	 * }</pre>
	 *
	 * The method returns a proxy of the query builder, which can be used to further define
	 * the join condition and other aspects of the query.
	 *
	 * @param <T>    the type of the entity or field to join
	 * @param toJoin the entity or field to be joined in the query
	 * @return a proxy of the query builder, facilitating further construction of the join clause
	 */
	public static <T> T innerJoin(T toJoin) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new InnerJoinHandler<T>(torpedoMethodHandler));
	}

	/**
	 * Creates an HQL inner join with explicit control over the joined entity type.
	 * In the TorpedoQuery framework, this method facilitates the construction of an inner join
	 * part of an HQL (Hibernate Query Language) query, allowing for precise specification
	 * of the entity type to be joined. It is particularly useful when the entity type needs
	 * to be dynamically determined at runtime.
	 *
	 * For instance, to create an inner join where the specific type of the joined entity
	 * is determined at runtime:
	 * <pre>{@code
	 * Entity entity = from(Entity.class);
	 * SpecificType joinedEntity = innerJoin(entity.getField(), SpecificType.class);
	 * }</pre>
	 *
	 * This method returns a proxy of the query builder, which can be used to further
	 * define the join condition and other aspects of the query.
	 *
	 * @param <T>      the base type of the entity or field to join
	 * @param <E>      the specific type of the entity to be joined, extending T
	 * @param toJoin   the entity or field to be joined in the query
	 * @param realType the class of the entity type E to join
	 * @return a proxy of the query builder of type E, facilitating further construction of the join clause
	 */
	public static <T, E extends T> E innerJoin(T toJoin, Class<E> realType) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new InnerJoinHandler<E>(torpedoMethodHandler, realType));
	}

	/**
	 * <p>
	 * innerJoin.
	 * </p>
	 *
	 * @see #innerJoin(Object)
	 * @param toJoin a {@link java.util.Collection} object.
	 * @param <T>    a T object.
	 * @return a T object.
	 */
	public static <T> T innerJoin(Collection<T> toJoin) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new InnerJoinHandler<T>(torpedoMethodHandler));
	}

	/**
	 * Creates an HQL inner join on a collection of entities or fields.
	 * This variant of the innerJoin method in the TorpedoQuery framework allows for constructing
	 * an inner join clause in an HQL (Hibernate Query Language) query using a collection.
	 * It is particularly useful when you need to join multiple related entities or fields simultaneously.
	 *
	 * For instance, to create an inner join on a collection of related entities:
	 * <pre>{@code
	 * Collection<RelatedEntity> relatedEntities = entity.getRelatedEntities();
	 * RelatedEntity joinedEntities = innerJoin(relatedEntities);
	 * }</pre>
	 *
	 * This method returns a proxy of the query builder, which can be further utilized to
	 * define join conditions and other aspects of the query.
	 *
	 * @param <T>    the type of the entities or fields within the collection to join
	 * @param toJoin the collection of entities or fields to be joined in the query
	 * @return a proxy of the query builder of type T, facilitating further construction of the join clause
	 */
	public static <T, E extends T> E innerJoin(Collection<T> toJoin, Class<E> realType) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new InnerJoinHandler<E>(torpedoMethodHandler, realType));
	}

	/**
	 * <p>
	 * innerJoin.
	 * </p>
	 *
	 * @see #innerJoin(Object)
	 * @param toJoin a {@link java.util.Map} object.
	 * @param <T>    a T object.
	 * @return a T object.
	 */
	public static <T> T innerJoin(Map<?, T> toJoin) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new InnerJoinHandler<T>(torpedoMethodHandler));
	}

	/**
	 * Creates an HQL inner join using a map of entities or fields.
	 * This version of the innerJoin method in the TorpedoQuery framework enables constructing
	 * an inner join clause in an HQL (Hibernate Query Language) query using a map structure.
	 * It is particularly useful when the entities or fields to be joined are organized within
	 * a map, allowing for more complex join scenarios.
	 *
	 * For example, to create an inner join on entities stored in a map:
	 * <pre>{@code
	 * Map<KeyType, RelatedEntity> entityMap = entity.getEntityMap();
	 * RelatedEntity joinedEntity = innerJoin(entityMap);
	 * }</pre>
	 *
	 * This method returns a proxy of the query builder, which can be used to further
	 * define the join conditions and other aspects of the query.
	 *
	 * @param <T>    the type of the values in the map which are to be joined
	 * @param toJoin the map containing the entities or fields to be joined
	 * @return a proxy of the query builder of type T, facilitating further construction
	 *         of the join clause
	 */
	public static <T, E extends T> E innerJoin(Map<?, T> toJoin, Class<E> realType) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new InnerJoinHandler<E>(torpedoMethodHandler, realType));
	}

	/**
	 * Constructs an inner join for a specified entity class.
	 * This version of the innerJoin method in the TorpedoQuery framework is used to initiate
	 * an inner join operation using the class of the entity to be joined. It is ideal for scenarios
	 * where you need to create a join to an entity based on its class type, typically used when 
	 * the entity instances are not directly available.
	 *
	 * For example, to create an inner join on a specific entity class:
	 * <pre>{@code
	 * JoinBuilder<Entity> joinBuilder = innerJoin(Entity.class);
	 * }</pre>
	 *
	 * This method returns a {@link JoinBuilder} instance, allowing for further configuration
	 * of the join condition and other query parameters.
	 *
	 * @param <T>    the type of the entity class to be joined
	 * @param toJoin the class of the entity to join in the query
	 * @return a {@link JoinBuilder} instance for further configuration of the inner join
	 */
	public static <T> JoinBuilder<T> innerJoin(Class<T> toJoin) {
		return new InnerJoinBuilder<>(toJoin, getTorpedoMethodHandler());
	}

	/**
	 * Constructs a left join for a specified entity class.
	 * In the TorpedoQuery framework, this version of the leftJoin method is utilized to start
	 * a left join operation using the class of the entity to be joined. This approach is particularly
	 * useful when setting up a join to an entity based on its class type, especially in situations 
	 * where direct instances of the entity are not available for joining.
	 *
	 * For instance, to create a left join on an entity class:
	 * <pre>{@code
	 * JoinBuilder<Entity> joinBuilder = leftJoin(Entity.class);
	 * }</pre>
	 *
	 * This method returns a {@link JoinBuilder} instance, enabling further customization 
	 * and configuration of the left join conditions and other aspects of the query.
	 *
	 * @param <T>    the type of the entity class to be joined
	 * @param toJoin the class of the entity for the left join
	 * @return a {@link JoinBuilder} instance for detailed configuration of the left join
	 */
	public static <T> JoinBuilder<T> leftJoin(Class<T> toJoin) {
		return new LeftJoinBuilder<>(toJoin, getTorpedoMethodHandler());
	}

	/**
	 * Constructs a right join for a specified entity class.
	 * This version of the rightJoin method in the TorpedoQuery framework is designed to initiate
	 * a right join operation based on the class of the entity to be joined. It is particularly useful
	 * in scenarios where the join needs to be established using the class type of an entity,
	 * particularly when instances of the entity are not directly accessible for joining.
	 *
	 * For example, to create a right join on an entity class:
	 * <pre>{@code
	 * JoinBuilder<Entity> joinBuilder = rightJoin(Entity.class);
	 * }</pre>
	 *
	 * The method returns a {@link JoinBuilder} instance, which can be used to further configure
	 * the right join conditions and refine other aspects of the query.
	 *
	 * @param <T>    the type of the entity class to be joined
	 * @param toJoin the class of the entity to be involved in the right join
	 * @return a {@link JoinBuilder} instance, enabling detailed configuration of the right join
	 */
	public static <T> JoinBuilder<T> rightJoin(Class<T> toJoin) {
		return new RightJoinBuilder<>(toJoin, getTorpedoMethodHandler());
	}

	/**
	 * Creates an HQL left join on the specified entity or field.
	 * This method in the TorpedoQuery framework is used to construct a left join clause
	 * in an HQL query. It allows for joining related entities or fields based on a specified 
	 * condition, typically defined later in the query building process.
	 *
	 * For example, to create a left join with a related entity or field:
	 * <pre>{@code
	 * Entity entity = from(Entity.class);
	 * RelatedEntity related = leftJoin(entity.getRelatedField());
	 * }</pre>
	 *
	 * The method returns a proxy of the query builder, which can then be used to further
	 * define the join condition and other query parameters.
	 *
	 * @param <T>    the type of the entity or field to join
	 * @param toJoin the entity or field to be left-joined in the query
	 * @return a proxy of the query builder of type T, allowing for further construction
	 *         of the left join clause
	 */
	public static <T> T leftJoin(T toJoin) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new LeftJoinHandler<T>(torpedoMethodHandler));
	}

	/**
	 * Creates an HQL left join with specific control over the joined entity type.
	 * This method in the TorpedoQuery framework facilitates the construction of a left join
	 * part of an HQL (Hibernate Query Language) query, allowing for precise specification
	 * of the entity type to be joined. It is particularly useful when the entity type needs
	 * to be dynamically determined at runtime, or when the base type is a superclass or 
	 * interface that multiple entities implement.
	 *
	 * For instance, to create a left join where the specific type of the joined entity
	 * is determined at runtime or is a subclass of the base entity:
	 * <pre>{@code
	 * Entity entity = from(Entity.class);
	 * SpecificType joinedEntity = leftJoin(entity.getField(), SpecificType.class);
	 * }</pre>
	 *
	 * This method returns a proxy of the query builder, which can be used to further
	 * define the join condition and other aspects of the query.
	 *
	 * @param <T>      the base type of the entity or field to join
	 * @param <E>      the specific type of the entity to be joined, extending T
	 * @param toJoin   the entity or field to be joined in the query
	 * @param realType the class of the entity type E to join
	 * @return a proxy of the query builder of type E, facilitating further construction of the left join clause
	 */
	public static <T, E extends T> E leftJoin(T toJoin, Class<E> realType) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new LeftJoinHandler<E>(torpedoMethodHandler, realType));
	}

	/**
	 * Creates an HQL left join using a collection of entities or fields.
	 * In the TorpedoQuery framework, this variant of the leftJoin method allows for constructing
	 * a left join clause in an HQL (Hibernate Query Language) query using a collection.
	 * This approach is useful when you need to perform a left join on multiple related entities 
	 * or fields simultaneously, as it allows for more complex join scenarios.
	 *
	 * For example, to create a left join on a collection of related entities:
	 * <pre>{@code
	 * Collection<RelatedEntity> relatedEntities = entity.getRelatedEntities();
	 * RelatedEntity joinedEntities = leftJoin(relatedEntities);
	 * }</pre>
	 *
	 * This method returns a proxy of the query builder, which can be used to further
	 * define the join conditions and other aspects of the query.
	 *
	 * @param <T>    the type of the entities or fields within the collection to join
	 * @param toJoin the collection of entities or fields to be left-joined
	 * @return a proxy of the query builder of type T, facilitating further construction
	 *         of the left join clause
	 */
	public static <T> T leftJoin(Collection<T> toJoin) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new LeftJoinHandler<T>(torpedoMethodHandler));
	}

	/**
	 * Creates an HQL left join on a collection with a specific entity type.
	 * This method in the TorpedoQuery framework allows for the construction of a left join
	 * clause in an HQL (Hibernate Query Language) query using a collection of entities or fields,
	 * while also specifying the exact type of the entity to join. It is ideal for cases where
	 * the entities in the collection are of a common base type, but the join needs to be
	 * specifically made with a certain subtype.
	 *
	 * For example, to create a left join on a collection of entities with a specific subclass:
	 * <pre>{@code
	 * Collection<BaseEntity> entities = entity.getEntities();
	 * SubClassEntity joinedEntities = leftJoin(entities, SubClassEntity.class);
	 * }</pre>
	 *
	 * This method returns a proxy of the query builder with the type E, enabling further
	 * configuration of the join conditions and other query parameters.
	 *
	 * @param <T>      the base type of the entities in the collection
	 * @param <E>      the specific type of the entity to join, extending T
	 * @param toJoin   the collection of entities to be left-joined
	 * @param realType the class object representing the specific entity type E for the join
	 * @return a proxy of the query builder of type E, facilitating further construction
	 *         of the left join clause
	 */
	public static <T, E extends T> E leftJoin(Collection<T> toJoin, Class<E> realType) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new LeftJoinHandler<E>(torpedoMethodHandler, realType));
	}

	/**
	 * Creates an HQL left join using a map of entities or fields.
	 * This version of the leftJoin method in the TorpedoQuery framework enables the construction
	 * of a left join clause in an HQL (Hibernate Query Language) query using a map structure.
	 * This approach is useful for scenarios where the entities or fields to be joined are organized
	 * within a map, allowing for more complex join configurations.
	 *
	 * For example, to create a left join on entities stored in a map:
	 * <pre>{@code
	 * Map<KeyType, RelatedEntity> entityMap = entity.getEntityMap();
	 * RelatedEntity joinedEntity = leftJoin(entityMap);
	 * }</pre>
	 *
	 * The method returns a proxy of the query builder, which can be used to further
	 * define the join conditions and other aspects of the query.
	 *
	 * @param <T>    the type of the values in the map which are to be joined
	 * @param toJoin the map containing the entities or fields to be left-joined
	 * @return a proxy of the query builder of type T, facilitating further construction
	 *         of the left join clause
	 */
	public static <T> T leftJoin(Map<?, T> toJoin) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new LeftJoinHandler<T>(torpedoMethodHandler));
	}

	/**
	 * <p>
	 * leftJoin.
	 * </p>
	 *
	 * @see #leftJoin(Object, Class)
	 * @param toJoin   a {@link java.util.Map} object.
	 * @param realType a {@link java.lang.Class} object.
	 * @param <T>      a T object.
	 * @return a E object.
	 * @param <E> a E object.
	 */
	public static <T, E extends T> E leftJoin(Map<?, T> toJoin, Class<E> realType) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new LeftJoinHandler<E>(torpedoMethodHandler, realType));
	}

	/**
	 * Create HQL right join
	 *
	 * @return a query builder proxy
	 * @param toJoin a T object.
	 * @param <T>    a T object.
	 */
	public static <T> T rightJoin(T toJoin) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new RightJoinHandler<T>(torpedoMethodHandler));
	}

	/**
	 * Create HQL right join
	 *
	 * @return a query builder proxy
	 * @param toJoin   a T object.
	 * @param realType a {@link java.lang.Class} object.
	 * @param <T>      a T object.
	 * @param <E> a E object.
	 */
	public static <T, E extends T> E rightJoin(T toJoin, Class<E> realType) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new RightJoinHandler<E>(torpedoMethodHandler, realType));
	}

	/**
	 * <p>
	 * rightJoin.
	 * </p>
	 *
	 * @see #rightJoin(Object)
	 * @param toJoin a {@link java.util.Collection} object.
	 * @param <T>    a T object.
	 * @return a T object.
	 */
	public static <T> T rightJoin(Collection<T> toJoin) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new RightJoinHandler<T>(torpedoMethodHandler));
	}

	/**
	 * <p>
	 * rightJoin.
	 * </p>
	 *
	 * @see #rightJoin(Object, Class)
	 * @param toJoin   a {@link java.util.Collection} object.
	 * @param realType a {@link java.lang.Class} object.
	 * @param <T>      a T object.
	 * @return a E object.
	 * @param <E> a E object.
	 */
	public static <T, E extends T> E rightJoin(Collection<T> toJoin, Class<E> realType) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new RightJoinHandler<E>(torpedoMethodHandler, realType));
	}

	/**
	 * <p>
	 * rightJoin.
	 * </p>
	 *
	 * @see #rightJoin(Object)
	 * @param toJoin a {@link java.util.Map} object.
	 * @param <T>    a T object.
	 * @return a T object.
	 */
	public static <T> T rightJoin(Map<?, T> toJoin) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new RightJoinHandler<T>(torpedoMethodHandler));
	}

	/**
	 * <p>
	 * rightJoin.
	 * </p>
	 *
	 * @see #rightJoin(Object, Class)
	 * @param toJoin   a {@link java.util.Map} object.
	 * @param realType a {@link java.lang.Class} object.
	 * @param <T>      a T object.
	 * @return a E object.
	 * @param <E> a E object.
	 */
	public static <T, E extends T> E rightJoin(Map<?, T> toJoin, Class<E> realType) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(toJoin);
		return torpedoMethodHandler.handle(new RightJoinHandler<E>(torpedoMethodHandler, realType));
	}

	/**
	 * Apply where condition to the query relative to the parameter.
	 *
	 * @return a condition builder
	 * @param object a T object.
	 * @param <T>    a T object.
	 */
	public static <T> ValueOnGoingCondition<T> where(T object) {
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler(object);
		return torpedoMethodHandler.handle(new WhereClauseHandler<T, ValueOnGoingCondition<T>>(object));
	}

	/**
	 * Apply where condition to the query relative to the parameter.
	 *
	 * @param condition group create by @see {@link #condition(Object)}
	 * @return a condition builder
	 * @param <T> a T object.
	 */
	public static <T> OnGoingLogicalCondition where(OnGoingLogicalCondition condition) {
		return getTorpedoMethodHandler(condition)
				.handle(new GroupingConditionHandler<T>(new WhereQueryConfigurator<T>(), (Condition) condition));
	}

	/**
	 * <p>
	 * where.
	 * </p>
	 *
	 * @see #where(Object)
	 * @param object a T object.
	 * @param <V>    a V object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingComparableCondition} object.
	 * @param <T> a T object.
	 */
	public static <V, T extends Comparable<V>> OnGoingComparableCondition<V> where(T object) {
		return getTorpedoMethodHandler(object).handle(new WhereClauseHandler<V, OnGoingComparableCondition<V>>(object));
	}

	/**
	 * <p>
	 * where.
	 * </p>
	 *
	 * @see #where(Object)
	 * @param object a {@link java.lang.String} object.
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
	 * @param function a {@link org.torpedoquery.jpa.Function} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingStringCondition} object.
	 */
	public static OnGoingStringCondition<String> where(Function<String> function) {
		return getTorpedoMethodHandler(function)
				.handle(new WhereClauseHandler<String, OnGoingStringCondition<String>>(function));
	}

	/**
	 * <p>
	 * where.
	 * </p>
	 *
	 * @see #where(Object)
	 * @param object a {@link java.util.Collection} object.
	 * @param <T>    a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingCollectionCondition} object.
	 */
	public static <T> OnGoingCollectionCondition<T> where(Collection<T> object) {
		return getTorpedoMethodHandler(object).handle(
				new WhereClauseHandler<T, OnGoingCollectionCondition<T>>(object, new WhereQueryConfigurator<T>()));
	}

	/**
	 * <p>
	 * where.
	 * </p>
	 *
	 * @see #where(Object)
	 * @param object a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 * @param <T>    a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingComparableCondition} object.
	 */
	public static <T> OnGoingComparableCondition<T> where(ComparableFunction<T> object) {
		return getTorpedoMethodHandler(object).handle(new WhereClauseHandler<T, OnGoingComparableCondition<T>>(object));
	}

	/**
	 * Create hql restriction directly onto hql join
	 *
	 * ex: inner join myObject.getMyJoinObject() joinObject with joinObject.myField
	 * is not null
	 *
	 * @return a condition builder
	 * @param object a T object.
	 * @param <T>    a T object.
	 */
	public static <T> ValueOnGoingCondition<T> with(T object) {
		return getTorpedoMethodHandler(object)
				.handle(new WhereClauseHandler<T, ValueOnGoingCondition<T>>(object, new WithQueryConfigurator<T>()));
	}

	/**
	 * <p>
	 * with.
	 * </p>
	 *
	 * @see #with(Object)
	 * @param object a T object.
	 * @param <V>    a V object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingComparableCondition} object.
	 * @param <T> a T object.
	 */
	public static <V, T extends Comparable<V>> OnGoingComparableCondition<V> with(T object) {
		return getTorpedoMethodHandler(object).handle(
				new WhereClauseHandler<V, OnGoingComparableCondition<V>>(object, new WithQueryConfigurator<V>()));
	}

	/**
	 * <p>
	 * with.
	 * </p>
	 *
	 * @see #with(Object)
	 * @param object a {@link java.lang.String} object.
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
	 * @param object a {@link java.util.Collection} object.
	 * @param <T>    a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingCollectionCondition} object.
	 */
	public static <T> OnGoingCollectionCondition<T> with(Collection<T> object) {
		return getTorpedoMethodHandler(object).handle(
				new WhereClauseHandler<T, OnGoingCollectionCondition<T>>(object, new WithQueryConfigurator<T>()));
	}

	/**
	 * <p>
	 * with.
	 * </p>
	 *
	 * @see #with(Object)
	 * @param condition a {@link org.torpedoquery.jpa.OnGoingLogicalCondition}
	 *                  object.
	 * @param <T>       a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	public static <T> OnGoingLogicalCondition with(OnGoingLogicalCondition condition) {
		return getTorpedoMethodHandler(condition)
				.handle(new GroupingConditionHandler<T>(new WithQueryConfigurator<T>(), (Condition) condition));
	}

	/**
	 * Create a group condition ex: (myObject.myField is not null and
	 * myObject.intField &gt; 2)
	 *
	 * this condition is not bind to any query builder until you pass it to the @see
	 * {@link #where(Object)} or to a ValueOnGoingCondition create by a where
	 *
	 * @return a condition builder
	 * @param object a T object.
	 * @param <T>    a T object.
	 */
	public static <T> ValueOnGoingCondition<T> condition(T object) {
		return getTorpedoMethodHandler(object).handle(
				new WhereClauseHandler<T, ValueOnGoingCondition<T>>(object, new DoNothingQueryConfigurator<T>()));
	}

	/**
	 * <p>
	 * condition.
	 * </p>
	 *
	 * @see #condition(Object)
	 * @param object a T object.
	 * @param <V>    a V object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingComparableCondition} object.
	 * @param <T> a T object.
	 */
	public static <V, T extends Comparable<V>> OnGoingComparableCondition<V> condition(T object) {
		return getTorpedoMethodHandler(object).handle(
				new WhereClauseHandler<V, OnGoingComparableCondition<V>>(object, new DoNothingQueryConfigurator<V>()));
	}

	/**
	 * <p>
	 * condition.
	 * </p>
	 *
	 * @see #condition(Object)
	 * @param object a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 * @param <V>    a V object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingComparableCondition} object.
	 * @param <T> a T object.
	 */
	public static <V, T extends Comparable<V>> OnGoingComparableCondition<V> condition(ComparableFunction<T> object) {
		return getTorpedoMethodHandler(object).handle(
				new WhereClauseHandler<V, OnGoingComparableCondition<V>>(object, new DoNothingQueryConfigurator<V>()));
	}

	/**
	 * <p>
	 * condition.
	 * </p>
	 *
	 * @see #condition(Object)
	 * @param object a {@link java.lang.String} object.
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
	 * @param object a {@link org.torpedoquery.jpa.Function} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingStringCondition} object.
	 */
	public static OnGoingStringCondition<String> condition(Function<String> object) {
		return getTorpedoMethodHandler(object).handle(new WhereClauseHandler<String, OnGoingStringCondition<String>>(
				object, new DoNothingQueryConfigurator<String>()));
	}

	/**
	 * <p>
	 * condition.
	 * </p>
	 *
	 * @see #condition(Object)
	 * @param object a {@link java.util.Collection} object.
	 * @param <T>    a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingCollectionCondition} object.
	 */
	public static <T> OnGoingCollectionCondition<T> condition(Collection<T> object) {
		return getTorpedoMethodHandler(object).handle(
				new WhereClauseHandler<T, OnGoingCollectionCondition<T>>(object, new DoNothingQueryConfigurator<T>()));
	}

	/**
	 * <p>
	 * condition.
	 * </p>
	 *
	 * @see #condition(Object)
	 * @param condition a {@link org.torpedoquery.jpa.OnGoingLogicalCondition}
	 *                  object.
	 * @param <T>       a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	public static <T> OnGoingLogicalCondition condition(OnGoingLogicalCondition condition) {
		return getTorpedoMethodHandler(condition)
				.handle(new GroupingConditionHandler<T>(new DoNothingQueryConfigurator<T>(), (Condition) condition));
	}

	/**
	 * <p>
	 * condition.
	 * </p>
	 *
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	public static OnGoingLogicalCondition condition() {
		return new EmptyLogicalCondition();
	}

	/**
	 * <p>
	 * and.
	 * </p>
	 *
	 * @param conditions a {@link org.torpedoquery.jpa.OnGoingLogicalCondition}
	 *                   object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	public static OnGoingLogicalCondition and(OnGoingLogicalCondition... conditions) {
		return and(Arrays.asList(conditions));
	}

	/**
	 * Group expressions together in a single conjunction (A and B and C...)
	 *
	 * @param conditions a {@link java.lang.Iterable} object.
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
	 * @param conditions a {@link org.torpedoquery.jpa.OnGoingLogicalCondition}
	 *                   object.
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
	 * @param conditions a {@link java.lang.Iterable} object.
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
	 * @param values a {@link java.lang.Object} object.
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
	 * @param values a {@link java.lang.Object} object.
	 */
	public static void orderBy(Object... values) {
		TorpedoMethodHandler fjpaMethodHandler = getTorpedoMethodHandler();
		final QueryBuilder root = fjpaMethodHandler.getRoot();
		final OrderBy orderBy = new OrderBy();

		getTorpedoMethodHandler().handle(new ArrayCallHandler(new ValueHandler<Void>() {
			@Override
			public Void handle(TorpedoProxy proxy, QueryBuilder queryBuilder, Selector selector) {
				orderBy.addOrder(selector);
				return null;
			}
		}, values));

		root.setOrderBy(orderBy);
	}

	/**
	 * <p>
	 * param.
	 * </p>
	 *
	 * @param param a T object.
	 * @param <T>   a T object.
	 * @return a T object.
	 */
	public static <T> T param(T param) {
		getTorpedoMethodHandler().addParam(param);
		return param;
	}

}
