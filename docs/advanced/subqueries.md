# Using Subqueries

Subqueries are queries nested within another query. They are powerful tools for creating complex queries that can reference and compare results between different data sets. TorpedoQuery offers a clean API for creating and using subqueries.

## Subqueries in WHERE Clauses

### IN Subquery

Use a subquery with the IN operator:

```java
Entity subSelect = from(Entity.class);
Entity entity = from(Entity.class);
where(entity.getCode()).in(select(subSelect.getCode()));
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.code IN ( SELECT entity_1.code FROM Entity entity_1 )`

### NOT IN Subquery

Use a subquery with the NOT IN operator:

```java
Entity subSelect = from(Entity.class);
Entity entity = from(Entity.class);
where(entity.getCode()).notIn(select(subSelect.getCode()));
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.code NOT IN ( SELECT entity_1.code FROM Entity entity_1 )`

### Comparison with Subquery

Compare a field with a subquery result:

```java
Entity subSelect = from(Entity.class);
Entity entity = from(Entity.class);
where(entity.getIntegerField()).gt(select(avg(subSelect.getIntegerField())));
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.integerField > ( SELECT AVG(entity_1.integerField) FROM Entity entity_1 )`

## Subqueries with Parameters

Subqueries can have their own conditions:

```java
Entity subSelect = from(Entity.class);
where(subSelect.getCode()).eq("subquery");
Entity entity = from(Entity.class);
where(entity.getCode()).in(select(subSelect.getCode()));
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.code IN ( SELECT entity_1.code FROM Entity entity_1 WHERE entity_1.code = :code_2 )`

```java
Entity subSelect = from(Entity.class);
where(subSelect.getCode()).eq("toto");
Entity entity = from(Entity.class);
where(entity.getIntegerField()).gt(select(avg(subSelect.getIntegerField())));
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.integerField > ( SELECT AVG(entity_1.integerField) FROM Entity entity_1 WHERE entity_1.code = :code_2 )`

## Subqueries in SELECT Clauses

Use subqueries directly in the SELECT clause:

```java
Entity subSelect = from(Entity.class);
Entity entity = from(Entity.class);
Query<Integer> maxIntegerSubQuery = select(max(subSelect.getIntegerField()));
Query<Object[]> query = select(entity.getName(), maxIntegerSubQuery);
```

Generated HQL: `SELECT entity_0.name, ( SELECT MAX(entity_1.integerField) FROM Entity entity_1 ) FROM Entity entity_0`

## Arithmetic Operations with Subqueries

Use subqueries in arithmetic operations:

```java
Entity subSelect = from(Entity.class);
Entity entity = from(Entity.class);
where(entity.getIntegerField())
        .eq(select(operation(subSelect.getIntegerField()).subtract(constant(1))));
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.integerField = ( SELECT entity_1.integerField - 1 FROM Entity entity_1 )`

## Correlated Subqueries

Create correlated subqueries where the inner query references the outer query:

```java
Entity entity = from(Entity.class);
Entity subEntity = from(Entity.class);
where(subEntity.getCode()).eq(entity.getCode());
where(entity.getIntegerField()).gt(select(avg(subEntity.getIntegerField())));
Query<Entity> query = select(entity);
```

This creates a query where each row in the outer query is compared with a subquery result that depends on that row.

## Subqueries with Join Conditions

Subqueries can include joins:

```java
Entity entity = from(Entity.class);
Entity subSelectEntity = from(Entity.class);
SubEntity subSelectSubEntity = innerJoin(subSelectEntity.getSubEntities());
where(subSelectSubEntity.getCode()).eq("test");
where(entity.getIntegerField()).lt(select(max(subSelectEntity.getIntegerField())));
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.integerField < ( SELECT MAX(entity_1.integerField) FROM Entity entity_1 INNER JOIN entity_1.subEntities subEntity_2 WHERE subEntity_2.code = :code_3 )`

## Practical Examples

### Finding Entities Above Average

Find all entities with an integer field above the average:

```java
Entity entity = from(Entity.class);
Entity subSelect = from(Entity.class);
where(entity.getIntegerField()).gt(select(avg(subSelect.getIntegerField())));
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.integerField > ( SELECT AVG(entity_1.integerField) FROM Entity entity_1 )`

### Finding Entities with Specific Related Records

Find entities that have at least one related sub-entity with a specific code:

```java
Entity entity = from(Entity.class);
SubEntity subSelectSubEntity = from(SubEntity.class);
where(subSelectSubEntity.getCode()).eq("test");
where(entity.getId()).in(select(subSelectSubEntity.getId()));
Query<Entity> query = select(entity);
```

### Finding Entities Without Related Records

Find entities that don't have any related sub-entities:

```java
Entity entity = from(Entity.class);
SubEntity subSelectSubEntity = from(SubEntity.class);
where(entity.getId()).notIn(select(subSelectSubEntity.getEntityId()));
Query<Entity> query = select(entity);
```

## Performance Considerations

While subqueries are powerful, they can impact performance. Consider these tips:

1. **Join vs. Subquery**: In many cases, a join might be more efficient than a subquery.
   
2. **Unnecessary Nesting**: Avoid unnecessarily deep nesting of subqueries.
   
3. **Correlation**: Correlated subqueries (where the inner query depends on the outer query) can be particularly expensive as they must be executed for each row of the outer query.

4. **EXISTS vs. IN**: In some databases, EXISTS might perform better than IN for certain types of subqueries.

Always test your queries with realistic data volumes to ensure good performance.