# Query Conditions

TorpedoQuery provides a powerful set of methods for creating complex query conditions. This page covers the various ways to filter your queries.

## Basic Comparison Operators

### Equality (=)

```java
Entity entity = from(Entity.class);
where(entity.getCode()).eq("test");
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.code = :code_1`

### Inequality (<>)

```java
Entity entity = from(Entity.class);
where(entity.getCode()).neq("test");
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.code <> :code_1`

### Greater Than (>)

```java
Entity entity = from(Entity.class);
where(entity.getIntegerField()).gt(2);
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.integerField > :integerField_1`

### Greater Than or Equal (>=)

```java
Entity entity = from(Entity.class);
where(entity.getIntegerField()).gte(2);
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.integerField >= :integerField_1`

### Less Than (<)

```java
Entity entity = from(Entity.class);
where(entity.getIntegerField()).lt(2);
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.integerField < :integerField_1`

### Less Than or Equal (<=)

```java
Entity entity = from(Entity.class);
where(entity.getIntegerField()).lte(2);
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.integerField <= :integerField_1`

## Null Checks

### Is Null

```java
Entity entity = from(Entity.class);
where(entity.getCode()).isNull();
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.code IS NULL`

### Is Not Null

```java
Entity entity = from(Entity.class);
where(entity.getCode()).isNotNull();
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.code IS NOT NULL`

## Between Operator

### Between

```java
Entity entity = from(Entity.class);
where(entity.getCode()).between("A", "C");
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.code BETWEEN :code_1 AND :code_2`

### Not Between

```java
Entity entity = from(Entity.class);
where(entity.getCode()).notBetween("A", "C");
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.code NOT BETWEEN :code_1 AND :code_2`

## IN Operator

### In (Values)

```java
Entity entity = from(Entity.class);
where(entity.getPrimitiveInt()).in(3, 4);
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.primitiveInt IN ( :primitiveInt_1 )`

### In (Subquery)

```java
Entity subSelect = from(Entity.class);
Entity entity = from(Entity.class);
where(entity.getCode()).in(select(subSelect.getCode()));
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.code IN ( SELECT entity_1.code FROM Entity entity_1 )`

### Not In (Values)

```java
Entity entity = from(Entity.class);
where(entity.getPrimitiveInt()).notIn(3, 4);
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.primitiveInt NOT IN ( :primitiveInt_1 )`

### Not In (Subquery)

```java
Entity subSelect = from(Entity.class);
Entity entity = from(Entity.class);
where(entity.getCode()).notIn(select(subSelect.getCode()));
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.code NOT IN ( SELECT entity_1.code FROM Entity entity_1 )`

## LIKE Operator

### Like (Contains)

```java
Entity entity = from(Entity.class);
where(entity.getCode()).like().any("test");
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.code LIKE :code_1`
Parameter: `%test%`

### Like (Starts With)

```java
Entity entity = from(Entity.class);
where(entity.getCode()).like().startsWith("test");
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.code LIKE :code_1`
Parameter: `test%`

### Like (Ends With)

```java
Entity entity = from(Entity.class);
where(entity.getCode()).like().endsWith("test");
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.code LIKE :code_1`
Parameter: `%test`

### Not Like

```java
Entity entity = from(Entity.class);
where(entity.getCode()).notLike().any("test");
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.code NOT LIKE :code_1`
Parameter: `%test%`

## Collection Operations

### Is Empty

```java
Entity entity = from(Entity.class);
where(entity.getSubEntities()).isEmpty();
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.subEntities IS EMPTY`

### Is Not Empty

```java
Entity entity = from(Entity.class);
where(entity.getSubEntities()).isNotEmpty();
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.subEntities IS NOT EMPTY`

### Collection Size

```java
Entity entity = from(Entity.class);
where(entity.getSubEntities()).size().gt(2);
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.subEntities.size > :subEntities_1`

### Member Of

```java
Entity entity = from(Entity.class);
where(entity.getValueCollection()).memberOf("VALUE");
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE :valueCollection_1 MEMBER OF entity_0.valueCollection`

## Logical Operators

### AND

```java
Entity entity = from(Entity.class);
where(entity.getName()).eq("test").and(entity.getPrimitiveInt()).gt(10);
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.name = :name_1 AND entity_0.primitiveInt > :primitiveInt_2`

### OR

```java
Entity entity = from(Entity.class);
where(entity.getName()).eq("test").or(entity.getPrimitiveInt()).gt(10);
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.name = :name_1 OR entity_0.primitiveInt > :primitiveInt_2`

## Grouping Conditions

You can create complex conditions by grouping them with parentheses:

```java
Entity entity = from(Entity.class);
OnGoingLogicalCondition condition = condition(entity.getCode()).eq("test").or(entity.getCode()).eq("test2");
where(entity.getName()).eq("test").and(condition);
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.name = :name_1 AND ( entity_0.code = :code_2 OR entity_0.code = :code_3 )`

### Inline Condition Grouping

```java
Entity entity = from(Entity.class);
where(entity.getName()).eq("test").or(condition(entity.getCode()).eq("test").or(entity.getCode()).eq("test2"));
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.name = :name_1 OR ( entity_0.code = :code_2 OR entity_0.code = :code_3 )`

## Comparing Entity Fields

You can compare one entity field to another:

```java
Entity entity = from(Entity.class);
where(entity.getCode()).eq(entity.getName());
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.code = entity_0.name`

## Type Conditions

Check the concrete type of an entity:

```java
Entity entity = from(Entity.class);
where(entity).eq(ExtendEntity.class);
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.class = ExtendEntity`

## Empty Conditions

TorpedoQuery allows you to create empty conditions that can be filled later:

```java
Entity entity = from(Entity.class);
OnGoingLogicalCondition emptyCondition = condition();
Query<Entity> query = select(entity);
where(emptyCondition);
// No WHERE clause is added since the condition is empty
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0`

## Using Functions in Conditions

You can use functions in your conditions:

```java
Entity entity = from(Entity.class);
where(lower(entity.getCode())).like().any("test");
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE lower(entity_0.code) LIKE :function_1`

## Accessing Condition Objects

You can retrieve the condition object from a query:

```java
Entity from = from(Entity.class);
where(from.getSmallChar()).eq('c');
Query<String> select = select(from.getName());

// Get the current condition
Optional<OnGoingLogicalCondition> condition = select.condition();
if (condition.isPresent()) {
    condition.get().and(from.getId()).eq("test");
}
```

Generated HQL: `SELECT entity_0.name FROM Entity entity_0 WHERE entity_0.smallChar = :smallChar_1 AND entity_0.id = :id_2`

## Chained Property Conditions

You can apply conditions to nested properties:

```java
Entity entity = from(Entity.class);
where(entity.getSubEntity().getName()).eq("test");
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.subEntity.name = :name_1`