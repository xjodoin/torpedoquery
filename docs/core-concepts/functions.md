# JPA Functions

TorpedoQuery provides a comprehensive set of functions that correspond to JPA/JPQL functions. These functions can be used in SELECT statements, WHERE clauses, and other parts of your queries.

## Aggregate Functions

### COUNT

Count the number of entities or non-null values:

```java
Entity entity = from(Entity.class);
Query<Long> query = select(count(entity));
```

Generated HQL: `SELECT COUNT(entity_0) FROM Entity entity_0`

Count a specific field:

```java
Entity entity = from(Entity.class);
Query<Long> query = select(count(entity.getCode()));
```

Generated HQL: `SELECT COUNT(entity_0.code) FROM Entity entity_0`

### SUM

Calculate the sum of numeric values:

```java
Entity entity = from(Entity.class);
Query<Integer> query = select(sum(entity.getIntegerField()));
```

Generated HQL: `SELECT SUM(entity_0.integerField) FROM Entity entity_0`

### AVG

Calculate the average of numeric values:

```java
Entity entity = from(Entity.class);
Query<Integer> query = select(avg(entity.getIntegerField()));
```

Generated HQL: `SELECT AVG(entity_0.integerField) FROM Entity entity_0`

### MIN

Find the minimum value:

```java
Entity entity = from(Entity.class);
Query<Integer> query = select(min(entity.getIntegerField()));
```

Generated HQL: `SELECT MIN(entity_0.integerField) FROM Entity entity_0`

### MAX

Find the maximum value:

```java
Entity entity = from(Entity.class);
Query<Integer> query = select(max(entity.getIntegerField()));
```

Generated HQL: `SELECT MAX(entity_0.integerField) FROM Entity entity_0`

## String Functions

### LOWER

Convert a string to lowercase:

```java
Entity entity = from(Entity.class);
Query<String> query = select(lower(entity.getCode()));
```

Generated HQL: `SELECT LOWER(entity_0.code) FROM Entity entity_0`

### UPPER

Convert a string to uppercase:

```java
Entity entity = from(Entity.class);
Query<String> query = select(upper(entity.getCode()));
```

Generated HQL: `SELECT UPPER(entity_0.code) FROM Entity entity_0`

### TRIM

Remove leading and trailing spaces:

```java
Entity entity = from(Entity.class);
Query<String> query = select(trim(entity.getCode()));
```

Generated HQL: `SELECT TRIM(entity_0.code) FROM Entity entity_0`

### LENGTH

Get the length of a string:

```java
Entity entity = from(Entity.class);
Query<Integer> query = select(length(entity.getCode()));
```

Generated HQL: `SELECT LENGTH(entity_0.code) FROM Entity entity_0`

### SUBSTRING

Extract a portion of a string:

```java
Entity entity = from(Entity.class);
Query<String> query = select(substring(entity.getCode(), 2, 4));
```

Generated HQL: `SELECT SUBSTRING(entity_0.code, 2, 4) FROM Entity entity_0`

## Other Functions

### COALESCE

Return the first non-null value:

```java
Entity entity = from(Entity.class);
Query<String> query = select(coalesce(entity.getCode(), entity.getName()));
```

Generated HQL: `SELECT COALESCE(entity_0.code, entity_0.name) FROM Entity entity_0`

### DISTINCT

Return distinct values:

```java
Entity entity = from(Entity.class);
Query<Entity> query = select(distinct(entity));
```

Generated HQL: `SELECT DISTINCT entity_0 FROM Entity entity_0`

Distinct on a field:

```java
Entity entity = from(Entity.class);
Query<String> query = select(distinct(entity.getCode()));
```

Generated HQL: `SELECT DISTINCT entity_0.code FROM Entity entity_0`

### INDEX

Get the index of an element in a collection:

```java
Entity entity = from(Entity.class);
SubEntity innerJoin = innerJoin(entity.getSubEntities());
Query<Object[]> query = select(innerJoin, index(innerJoin));
```

Generated HQL: `SELECT subEntity_1, INDEX(subEntity_1) FROM Entity entity_0 INNER JOIN entity_0.subEntities subEntity_1`

## Using Functions in WHERE Clauses

Functions can be used in WHERE clauses:

```java
Entity entity = from(Entity.class);
where(length(entity.getCode())).gt(5);
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE LENGTH(entity_0.code) > :function_1`

```java
Entity entity = from(Entity.class);
where(lower(entity.getCode())).like().any("test");
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE LOWER(entity_0.code) LIKE :function_1`

## Custom Functions

TorpedoQuery allows you to use custom functions defined in your JPA provider:

```java
Entity entity = from(Entity.class);
Query<String> query = select(function("toto", String.class, entity.getName()));
```

Generated HQL: `SELECT toto(entity_0.name) FROM Entity entity_0`

## Combining Functions

Functions can be nested:

```java
Entity entity = from(Entity.class);
Query<Integer> query = select(length(trim(entity.getCode())));
```

Generated HQL: `SELECT LENGTH(TRIM(entity_0.code)) FROM Entity entity_0`

```java
Entity entity = from(Entity.class);
Query<String> query = select(function("toto", String.class, max(entity.getIntegerField())));
```

Generated HQL: `SELECT toto(MAX(entity_0.integerField)) FROM Entity entity_0`

## Counting Distinct Values

Count distinct values:

```java
Entity entity = from(Entity.class);
Query<Long> query = select(count(distinct(entity.getInterface())));
```

Generated HQL: `SELECT COUNT(DISTINCT entity_0.interface) FROM Entity entity_0`

## Arithmetic Operations

TorpedoQuery supports arithmetic operations on numeric fields:

### Addition

```java
Entity entity = from(Entity.class);
Query<Integer> query = select(operation(entity.getIntegerField()).plus(entity.getPrimitiveInt()));
```

Generated HQL: `SELECT entity_0.integerField + entity_0.primitiveInt FROM Entity entity_0`

### Subtraction

```java
Entity entity = from(Entity.class);
where(operation(entity.getBigDecimalField()).subtract(entity.getBigDecimalField2())).gt(constant(BigDecimal.ZERO));
Query<BigDecimal> query = select(sum(operation(entity.getBigDecimalField()).subtract(entity.getBigDecimalField2())));
```

Generated HQL: `SELECT SUM(entity_0.bigDecimalField - entity_0.bigDecimalField2) FROM Entity entity_0 WHERE entity_0.bigDecimalField - entity_0.bigDecimalField2 > 0`

### Using Constants in Operations

```java
Entity entity = from(Entity.class);
Entity existingEntity = from(Entity.class);
where(existingEntity.getId()).eq("testid");
where(entity.getIntegerField()).eq(select(operation(existingEntity.getIntegerField()).subtract(constant(1))));
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.integerField = ( SELECT entity_1.integerField - 1 FROM Entity entity_1 WHERE entity_1.id = :id_2 )`

## Using Functions in GROUP BY and HAVING Clauses

Functions can also be used in GROUP BY and HAVING clauses:

```java
Entity entity = from(Entity.class);
groupBy(entity.getIntegerField()).having(entity.getBigDecimalField()).gt(coalesce(sum(entity.getBigDecimalField2()), constant(BigDecimal.ZERO)));
Query<Integer> query = select(sum(entity.getIntegerField()));
```

Generated HQL: `SELECT SUM(entity_0.integerField) FROM Entity entity_0 GROUP BY entity_0.integerField HAVING entity_0.bigDecimalField > COALESCE(SUM(entity_0.bigDecimalField2), 0)`