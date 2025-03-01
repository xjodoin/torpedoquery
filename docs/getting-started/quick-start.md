# Quick Start Guide

This guide will help you get up and running with TorpedoQuery quickly.

## Basic Setup

First, import the necessary static methods:

```java
import static org.torpedoquery.jpa.Torpedo.*;
```

## Basic Queries

### Simple Select Query

Retrieving all records from an entity:

```java
Entity entity = from(Entity.class);
Query<Entity> query = select(entity);
List<Entity> results = query.list(entityManager);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0`

### Selecting Specific Fields

Retrieve only specific columns:

```java
Entity entity = from(Entity.class);
Query<String> query = select(entity.getName());
List<String> names = query.list(entityManager);
```

Generated HQL: `SELECT entity_0.name FROM Entity entity_0`

### Multiple Fields Selection

Select multiple fields as an array:

```java
Entity entity = from(Entity.class);
Query<Object[]> query = select(entity.getName(), entity.getCode());
List<Object[]> results = query.list(entityManager);
```

Generated HQL: `SELECT entity_0.name, entity_0.code FROM Entity entity_0`

## Adding Conditions

### Simple Where Clause

Filter results with a where clause:

```java
Entity entity = from(Entity.class);
where(entity.getName()).eq("test");
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.name = :name_1`

### Multiple Conditions

Combine conditions with AND:

```java
Entity entity = from(Entity.class);
where(entity.getName()).eq("test").and(entity.getCode()).eq("ABC");
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.name = :name_1 AND entity_0.code = :code_2`

Or with OR:

```java
Entity entity = from(Entity.class);
where(entity.getName()).eq("test").or(entity.getName()).eq("test2");
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.name = :name_1 OR entity_0.name = :name_2`

## Basic Joins

Join with related entities:

```java
Entity entity = from(Entity.class);
SubEntity subEntity = innerJoin(entity.getSubEntity());
Query<Object[]> query = select(entity.getName(), subEntity.getCode());
```

Generated HQL: `SELECT entity_0.name, subEntity_1.code FROM Entity entity_0 INNER JOIN entity_0.subEntity subEntity_1`

## Scalar Functions

Use aggregate functions:

```java
Entity entity = from(Entity.class);
Query<Long> query = select(count(entity));
```

Generated HQL: `SELECT count(entity_0) FROM Entity entity_0`

## Grouping Results

Group by a field:

```java
Entity entity = from(Entity.class);
groupBy(entity.getName());
Query<Object[]> query = select(entity.getName(), count(entity));
```

Generated HQL: `SELECT entity_0.name, count(entity_0) FROM Entity entity_0 GROUP BY entity_0.name`

## Ordering Results

Order the results:

```java
Entity entity = from(Entity.class);
orderBy(entity.getName());
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 ORDER BY entity_0.name`

## Executing Queries

Use your EntityManager to execute the query:

```java
// Get a single result
Entity result = query.get(entityManager);

// Get a list of results
List<Entity> results = query.list(entityManager);
```

## Dynamic Query Building

Create conditions dynamically:

```java
Entity entity = from(Entity.class);

// Only add condition if value is provided
String name = getUserInput();
if (name != null && !name.isEmpty()) {
    where(entity.getName()).eq(name);
}

Query<Entity> query = select(entity);
```

## Complete Example

Here's a more comprehensive example showing several features together:

```java
// Define query
Entity entity = from(Entity.class);
SubEntity subEntity = innerJoin(entity.getSubEntities());

// Add conditions
where(entity.isActive()).eq(true)
    .and(subEntity.getCode()).like().startsWith("ABC");

// Group and order
groupBy(entity.getName());
orderBy(entity.getName());

// Create and execute the query
Query<Object[]> query = select(entity.getName(), count(subEntity));
List<Object[]> results = query.list(entityManager);
```

Generated HQL: 
```
SELECT entity_0.name, count(subEntity_1) 
FROM Entity entity_0 
INNER JOIN entity_0.subEntities subEntity_1 
WHERE entity_0.active = :active_2 AND subEntity_1.code LIKE :code_3 
GROUP BY entity_0.name 
ORDER BY entity_0.name
```

That's it! You've learned the basics of TorpedoQuery. Check out the other sections for more advanced features.