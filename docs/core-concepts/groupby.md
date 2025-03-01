# GROUP BY Operations

GROUP BY is a crucial operation in SQL that allows you to aggregate data based on one or more columns. TorpedoQuery provides a clean API for creating GROUP BY clauses and accompanying HAVING conditions.

## Basic GROUP BY

Group records by a single field:

```java
Entity entity = from(Entity.class);
groupBy(entity.getName());
Query<Object[]> query = select(entity.getName(), sum(entity.getIntegerField()));
```

Generated HQL: `SELECT entity_0.name, SUM(entity_0.integerField) FROM Entity entity_0 GROUP BY entity_0.name`

## Multiple GROUP BY Fields

Group by multiple fields:

```java
Entity entity = from(Entity.class);
groupBy(entity.getName(), entity.getCode());
Query<Object[]> query = select(entity.getName(), entity.getCode(), sum(entity.getIntegerField()));
```

Generated HQL: `SELECT entity_0.name, entity_0.code, SUM(entity_0.integerField) FROM Entity entity_0 GROUP BY entity_0.name, entity_0.code`

## Adding HAVING Clauses

Filter grouped results with HAVING:

```java
Entity entity = from(Entity.class);
groupBy(entity.getName()).having(entity.getName()).eq("test");
Query<Object[]> query = select(entity.getName(), sum(entity.getIntegerField()));
```

Generated HQL: `SELECT entity_0.name, SUM(entity_0.integerField) FROM Entity entity_0 GROUP BY entity_0.name HAVING entity_0.name = :name_1`

## HAVING with Aggregate Functions

Filter groups using aggregate functions:

```java
Entity entity = from(Entity.class);
SubEntity subEntity = innerJoin(entity.getSubEntities());
groupBy(entity.getName()).having(sum(entity.getIntegerField())).lt(sum(subEntity.getNumberField()));
Query<String> query = select(entity.getName());
```

Generated HQL: `SELECT entity_0.name FROM Entity entity_0 INNER JOIN entity_0.subEntities subEntity_1 GROUP BY entity_0.name HAVING SUM(entity_0.integerField) < SUM(subEntity_1.numberField)`

## Complex HAVING Conditions

Create complex HAVING conditions:

```java
Entity entity = from(Entity.class);
OnGoingLogicalCondition condition = condition(entity.getName()).eq("test").or(entity.getName()).eq("test2");
groupBy(entity.getName()).having(condition).and(sum(entity.getIntegerField())).gt(2);
Query<String> query = select(entity.getName());
```

Generated HQL: `SELECT entity_0.name FROM Entity entity_0 GROUP BY entity_0.name HAVING ( entity_0.name = :name_1 OR entity_0.name = :name_2 ) AND SUM(entity_0.integerField) > :function_3`

## Reversing the Order of Complex HAVING Conditions

You can also reverse the order of conditions:

```java
Entity entity = from(Entity.class);
OnGoingLogicalCondition condition = condition(entity.getName()).eq("test").or(entity.getName()).eq("test2");
groupBy(entity.getName()).having(sum(entity.getIntegerField())).gt(2).and(condition);
Query<String> query = select(entity.getName());
```

Generated HQL: `SELECT entity_0.name FROM Entity entity_0 GROUP BY entity_0.name HAVING SUM(entity_0.integerField) > :function_1 AND ( entity_0.name = :name_2 OR entity_0.name = :name_3 )`

## Using Functions in HAVING Clauses

HAVING clauses can use various functions:

```java
Entity entity = from(Entity.class);
groupBy(entity.getIntegerField()).having(entity.getBigDecimalField()).gt(coalesce(sum(entity.getBigDecimalField2()), constant(BigDecimal.ZERO)));
Query<Integer> query = select(sum(entity.getIntegerField()));
```

Generated HQL: `SELECT SUM(entity_0.integerField) FROM Entity entity_0 GROUP BY entity_0.integerField HAVING entity_0.bigDecimalField > COALESCE(SUM(entity_0.bigDecimalField2), 0)`

## HAVING with Entity Properties

You can refer to entities in HAVING clauses:

```java
Entity entity = from(Entity.class);
SubEntity subEntity = innerJoin(entity.getSubEntities());
groupBy(entity.getName()).having(sum(entity.getIntegerField())).lt(subEntity.getNumberField());
Query<String> query = select(entity.getName());
```

Generated HQL: `SELECT entity_0.name FROM Entity entity_0 INNER JOIN entity_0.subEntities subEntity_1 GROUP BY entity_0.name HAVING SUM(entity_0.integerField) < subEntity_1.numberField`

## Combining GROUP BY with WHERE Clauses

GROUP BY works seamlessly with WHERE clauses:

```java
Entity entity = from(Entity.class);
where(entity.isActive()).eq(true);
groupBy(entity.getName());
Query<Object[]> query = select(entity.getName(), sum(entity.getIntegerField()));
```

Generated HQL: `SELECT entity_0.name, SUM(entity_0.integerField) FROM Entity entity_0 WHERE entity_0.active = :active_1 GROUP BY entity_0.name`

## Combining HAVING with Other Conditions

You can combine HAVING with other logical operations:

```java
Entity entity = from(Entity.class);
OnGoingLogicalCondition condition = condition(entity.isActive()).eq(true);
groupBy(entity.getName()).having(sum(entity.getIntegerField())).gt(sum(entity.getPrimitiveInt())).or(condition);
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 GROUP BY entity_0.name HAVING SUM(entity_0.integerField) > SUM(entity_0.primitiveInt) OR ( entity_0.active = :active_1 )`

## Complete Example

Here's a complete example showing GROUP BY with related entities and multiple conditions:

```java
// Define entities
Entity entity = from(Entity.class);
SubEntity subEntity = innerJoin(entity.getSubEntities());

// Add WHERE conditions
where(entity.isActive()).eq(true);

// Define GROUP BY with HAVING
groupBy(entity.getName(), entity.getCode())
    .having(sum(entity.getIntegerField())).gt(100)
    .and(subEntity.getNumberField()).isNotNull();

// Create the final query
Query<Object[]> query = select(
    entity.getName(),
    entity.getCode(),
    sum(entity.getIntegerField()),
    avg(subEntity.getNumberField())
);
```

This creates a query that:
1. Selects active entities and joins with their sub-entities
2. Groups results by name and code
3. Filters groups to only include those with a sum of integer fields greater than 100 and non-null number fields
4. Returns the name, code, sum of integer fields, and average of number fields for each group