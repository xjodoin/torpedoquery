# API Reference

This page provides a reference of TorpedoQuery's API and main classes. For detailed examples and use cases, refer to the specific documentation pages.

## Core Classes

### Torpedo

The `Torpedo` class is the main entry point for creating queries. It contains static methods for all the main query operations.

```java
import static org.torpedoquery.jpa.Torpedo.*;
```

#### Main Methods

| Method | Description |
|--------|-------------|
| `from(Class<T> entityClass)` | Creates a proxy for the specified entity class to start building a query |
| `select(Object... selects)` | Creates a query with the given selections |
| `where(Object object)` | Starts building a WHERE clause on the specified field |
| `condition()` | Creates an empty logical condition |
| `condition(Object object)` | Creates a logical condition on the specified field |
| `innerJoin(Object object)` | Creates an inner join with the specified relation |
| `leftJoin(Object object)` | Creates a left join with the specified relation |
| `rightJoin(Object object)` | Creates a right join with the specified relation |
| `with(OnGoingLogicalCondition condition)` | Adds a condition to a join (WITH clause) |
| `with(Object value)` | Starts building a WITH condition on a joined relation |
| `groupBy(Object... groups)` | Creates a GROUP BY clause for the specified fields |
| `orderBy(Object... orders)` | Creates an ORDER BY clause for the specified fields |
| `extend(Object proxy, Class<T> subType)` | Extends a proxy to a subtype to access subtype-specific methods |
| `dyn(Object constructor)` | Creates a dynamic instantiation expression (for custom result mapping) |
| `param(Object value)` | Used inside dyn() to specify constructor parameters |
| `and(OnGoingLogicalCondition... conditions)` | Combines conditions with AND |
| `or(OnGoingLogicalCondition... conditions)` | Combines conditions with OR |

### TorpedoFunction

The `TorpedoFunction` class provides access to JPA/JPQL functions.

```java
import static org.torpedoquery.jpa.TorpedoFunction.*;
```

#### Available Functions

| Function | Description |
|----------|-------------|
| `count(Object value)` | COUNT function |
| `sum(Object value)` | SUM function |
| `min(Object value)` | MIN function |
| `max(Object value)` | MAX function |
| `avg(Object value)` | AVG function |
| `distinct(Object value)` | DISTINCT function |
| `coalesce(Object... values)` | COALESCE function |
| `length(Object value)` | LENGTH function |
| `lower(Object value)` | LOWER function |
| `upper(Object value)` | UPPER function |
| `trim(Object value)` | TRIM function |
| `substring(Object value, int start, int length)` | SUBSTRING function |
| `index(Object value)` | INDEX function |
| `function(String name, Class<?> returnType, Object... params)` | Generic function support |
| `constant(Object constant)` | Creates a constant value for use in operations |
| `operation(Object left)` | Starts an arithmetic operation |
| `asc(Object value)` | ASC ordering directive |
| `desc(Object value)` | DESC ordering directive |

### Query<T>

The `Query<T>` interface represents a compiled query ready for execution.

#### Methods

| Method | Description |
|--------|-------------|
| `T get(EntityManager entityManager)` | Executes the query and returns a single result |
| `List<T> list(EntityManager entityManager)` | Executes the query and returns a list of results |
| `String getQuery()` | Returns the generated HQL/JPQL query string |
| `Map<String, Object> getParameters()` | Returns the parameter map for the query |
| `Optional<OnGoingLogicalCondition> condition()` | Returns the query's condition if it exists |

## Condition Interfaces

### OnGoingComparableCondition<T>

Interface for conditions on comparable values.

#### Methods

| Method | Description |
|--------|-------------|
| `OnGoingLogicalCondition eq(T value)` | Equality |
| `OnGoingLogicalCondition neq(T value)` | Inequality |
| `OnGoingLogicalCondition lt(T value)` | Less than |
| `OnGoingLogicalCondition lte(T value)` | Less than or equal |
| `OnGoingLogicalCondition gt(T value)` | Greater than |
| `OnGoingLogicalCondition gte(T value)` | Greater than or equal |
| `OnGoingLogicalCondition between(T start, T end)` | Between |
| `OnGoingLogicalCondition notBetween(T start, T end)` | Not between |
| `OnGoingLogicalCondition in(T... values)` | In a list of values |
| `OnGoingLogicalCondition in(Collection<T> values)` | In a collection |
| `OnGoingLogicalCondition in(Query<T> subQuery)` | In a subquery |
| `OnGoingLogicalCondition notIn(T... values)` | Not in a list of values |
| `OnGoingLogicalCondition notIn(Collection<T> values)` | Not in a collection |
| `OnGoingLogicalCondition notIn(Query<T> subQuery)` | Not in a subquery |

### OnGoingStringCondition

Interface for string-specific conditions.

#### Methods

| Method | Description |
|--------|-------------|
| `ValueOnGoingStringCondition like()` | Like comparison |
| `ValueOnGoingStringCondition notLike()` | Not like comparison |

### ValueOnGoingStringCondition

Interface for specifying like patterns.

#### Methods

| Method | Description |
|--------|-------------|
| `OnGoingLogicalCondition any(String value)` | %value% |
| `OnGoingLogicalCondition startsWith(String value)` | value% |
| `OnGoingLogicalCondition endsWith(String value)` | %value |

### OnGoingCollectionCondition

Interface for collection-specific conditions.

#### Methods

| Method | Description |
|--------|-------------|
| `OnGoingLogicalCondition isEmpty()` | Collection is empty |
| `OnGoingLogicalCondition isNotEmpty()` | Collection is not empty |
| `OnGoingLogicalCondition memberOf(Object value)` | Value is a member of collection |
| `OnGoingComparableCondition<Integer> size()` | Collection size |

### OnGoingLogicalCondition

Interface for logical conditions that can be combined.

#### Methods

| Method | Description |
|--------|-------------|
| `OnGoingLogicalCondition and(Object value)` | AND with another condition |
| `OnGoingLogicalCondition or(Object value)` | OR with another condition |
| `boolean isEmpty()` | Check if the condition is empty |

## Examples

### Basic Query

```java
// Import static methods
import static org.torpedoquery.jpa.Torpedo.*;
import static org.torpedoquery.jpa.TorpedoFunction.*;

// Create a query
Entity entity = from(Entity.class);
where(entity.getCode()).eq("test");
Query<Entity> query = select(entity);

// Execute the query
EntityManager em = getEntityManager();
List<Entity> results = query.list(em);
```

### Complex Query

```java
// Create entity proxies
Entity entity = from(Entity.class);
SubEntity subEntity = innerJoin(entity.getSubEntities());

// Add conditions
where(entity.isActive()).eq(true)
    .and(subEntity.getCode()).like().startsWith("PREFIX_");

// Add grouping and aggregation
groupBy(entity.getName())
    .having(count(subEntity)).gt(5);

// Add ordering
orderBy(desc(count(subEntity)), entity.getName());

// Create and execute query
Query<Object[]> query = select(entity.getName(), count(subEntity));
List<Object[]> results = query.list(entityManager);
```

For more detailed examples and advanced usage, refer to the other documentation pages.