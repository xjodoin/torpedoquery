# ORDER BY Operations

ORDER BY clauses allow you to sort your query results. TorpedoQuery makes it easy to define sophisticated sorting rules.

## Basic Ordering

Order by a single field:

```java
Entity entity = from(Entity.class);
orderBy(entity.getCode());
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 ORDER BY entity_0.code`

## Multiple Fields Ordering

Order by multiple fields:

```java
Entity entity = from(Entity.class);
orderBy(entity.getCode(), entity.getName());
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 ORDER BY entity_0.code, entity_0.name`

## Ordering Direction

### Ascending Order (ASC)

Use the `asc` function to explicitly specify ascending order:

```java
Entity entity = from(Entity.class);
orderBy(asc(entity.getCode()));
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 ORDER BY entity_0.code ASC`

### Descending Order (DESC)

Use the `desc` function to specify descending order:

```java
Entity entity = from(Entity.class);
orderBy(desc(entity.getCode()));
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 ORDER BY entity_0.code DESC`

## Mixed Ordering Directions

Combine ASC and DESC for different fields:

```java
Entity entity = from(Entity.class);
orderBy(asc(entity.getCode()), desc(entity.getName()));
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 ORDER BY entity_0.code ASC, entity_0.name DESC`

## Combining Default and Explicit Directions

Mix default direction (implicitly ascending) with explicit directions:

```java
Entity entity = from(Entity.class);
orderBy(asc(entity.getCode()), entity.getName());
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 ORDER BY entity_0.code ASC, entity_0.name`

## Ordering with Joined Entities

Order by fields from joined entities:

```java
Entity entity = from(Entity.class);
SubEntity innerJoin = innerJoin(entity.getSubEntity());
orderBy(innerJoin.getCode());
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 INNER JOIN entity_0.subEntity subEntity_1 ORDER BY subEntity_1.code`

## Multi-level Ordering with Joins

Combine ordering on main entity and joined entities:

```java
Entity entity = from(Entity.class);
SubEntity innerJoin = innerJoin(entity.getSubEntity());
orderBy(entity.getCode(), innerJoin.getCode());
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 INNER JOIN entity_0.subEntity subEntity_1 ORDER BY entity_0.code, subEntity_1.code`

## Ordering with Collection Joins

Order by fields from collection relationships:

```java
Entity entity = from(Entity.class);
SubEntity subEntity = innerJoin(entity.getSubEntities());
orderBy(subEntity.getCode(), entity.getName());
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 INNER JOIN entity_0.subEntities subEntity_1 ORDER BY subEntity_1.code, entity_0.name`

## Integration with Other Clauses

ORDER BY works seamlessly with other clauses:

```java
Entity entity = from(Entity.class);
SubEntity subEntity = innerJoin(entity.getSubEntities());
where(entity.isActive()).eq(true);
groupBy(entity.getName());
orderBy(entity.getName(), count(subEntity));
Query<Object[]> query = select(entity.getName(), count(subEntity));
```

This creates a query that:
1. Joins entities with their sub-entities
2. Filters to include only active entities
3. Groups results by name
4. Orders results by name and the count of sub-entities
5. Returns the name and count for each group

## Practical Example

Let's look at a practical example for ordering a result set:

```java
// Find all active entities ordered by name (A-Z) and creation date (newest first)
Entity entity = from(Entity.class);
where(entity.isActive()).eq(true);
orderBy(asc(entity.getName()), desc(entity.getDateField()));
Query<Entity> query = select(entity);
List<Entity> results = query.list(entityManager);
```

This query:
1. Filters to include only active entities
2. Orders results alphabetically by name
3. For entities with the same name, orders by date with newest first
4. Returns the complete entity objects

## Order By with Pagination

When implementing pagination, ordering becomes crucial for consistent results:

```java
// Implementation for paginated results
Entity entity = from(Entity.class);
where(entity.isActive()).eq(true);
orderBy(entity.getName());  // Always include ordering for consistent pagination
Query<Entity> query = select(entity);

// Apply pagination in your EntityManager query
javax.persistence.Query emQuery = entityManager.createQuery(query.getQuery());
// Set parameters
for (Map.Entry<String, Object> entry : query.getParameters().entrySet()) {
    emQuery.setParameter(entry.getKey(), entry.getValue());
}
// Set pagination
emQuery.setFirstResult(pageNumber * pageSize);
emQuery.setMaxResults(pageSize);

List<Entity> results = emQuery.getResultList();
```

By including a consistent ordering, you ensure that as you paginate through results, entities don't appear multiple times or get skipped if the database order changes between queries.