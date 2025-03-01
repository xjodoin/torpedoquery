# Working with Joins

TorpedoQuery makes it easy to create joins between entities. Joins are essential when your data is spread across multiple tables or entities.

## Inner Joins

An inner join returns records that have matching values in both tables.

### Basic Inner Join

```java
Entity entity = from(Entity.class);
SubEntity subEntity = innerJoin(entity.getSubEntity());

Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 INNER JOIN entity_0.subEntity subEntity_1`

### Selecting from Joined Entities

```java
Entity entity = from(Entity.class);
SubEntity subEntity = innerJoin(entity.getSubEntity());

Query<Object[]> query = select(entity.getCode(), subEntity.getCode());
```

Generated HQL: `SELECT entity_0.code, subEntity_1.code FROM Entity entity_0 INNER JOIN entity_0.subEntity subEntity_1`

### Joining with Collections

You can join with collection relationships:

```java
Entity entity = from(Entity.class);
SubEntity subEntity = innerJoin(entity.getSubEntities());

Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 INNER JOIN entity_0.subEntities subEntity_1`

## Left Joins

A left join returns all records from the left table and the matched records from the right table. The result is NULL from the right side if there is no match.

```java
Entity entity = from(Entity.class);
SubEntity subEntity = leftJoin(entity.getSubEntity());

Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 LEFT JOIN entity_0.subEntity subEntity_1`

## Right Joins

A right join returns all records from the right table and the matched records from the left table. The result is NULL from the left side if there is no match.

```java
Entity entity = from(Entity.class);
SubEntity subEntity = rightJoin(entity.getSubEntity());

Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 RIGHT JOIN entity_0.subEntity subEntity_1`

## Multiple Joins

You can create multiple joins in a single query:

```java
Entity entity = from(Entity.class);
SubEntity subEntity = innerJoin(entity.getSubEntity());
SubEntity subEntities = innerJoin(entity.getSubEntities());

Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 INNER JOIN entity_0.subEntity subEntity_1 INNER JOIN entity_0.subEntities subEntity_2`

## Multiple Joins on the Same Property

You can also join the same property multiple times:

```java
Entity entity = from(Entity.class);
innerJoin(entity.getSubEntity());
leftJoin(entity.getSubEntity());

Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 INNER JOIN entity_0.subEntity subEntity_1 LEFT JOIN entity_0.subEntity subEntity_2`

## Conditions on Joined Entities

You can add where conditions on joined entities:

```java
Entity entity = from(Entity.class);
SubEntity subEntity = innerJoin(entity.getSubEntities());
where(subEntity.getCode()).eq("test");

Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 INNER JOIN entity_0.subEntities subEntity_1 WHERE subEntity_1.code = :code_2`

## WITH Clause (Join Conditions)

The WITH clause allows you to specify conditions directly on the join:

```java
Entity entity = from(Entity.class);
SubEntity subEntity = innerJoin(entity.getSubEntities());
with(subEntity.getCode()).eq("test");

Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 INNER JOIN entity_0.subEntities subEntity_1 WITH subEntity_1.code = :code_2`

### Complex WITH Conditions

You can create complex join conditions:

```java
Entity entity = from(Entity.class);
SubEntity subEntity = innerJoin(entity.getSubEntities());
OnGoingLogicalCondition withCondition = 
    condition(subEntity.getCode()).eq("test").or(subEntity.getCode()).eq("test2");
with(withCondition);

Query<SubEntity> query = select(subEntity);
```

Generated HQL: `SELECT subEntity_1 FROM Entity entity_0 INNER JOIN entity_0.subEntities subEntity_1 WITH ( subEntity_1.code = :code_2 OR subEntity_1.code = :code_3 )`

## Custom Entity Joins

TorpedoQuery also allows joining unrelated entities with custom conditions:

```java
Entity entity = from(Entity.class);
Entity2 entity2 = innerJoin(Entity2.class).on(query2 -> {
    return condition(query2.getCode()).eq(entity.getCode());
});

Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 INNER JOIN Entity2 entity2_1 ON entity2_1.code = entity_0.code`

### Custom Entity Joins with Multiple Conditions

```java
Entity entity = from(Entity.class);
Entity2 entity2 = innerJoin(Entity2.class).on(query2 -> {
    return condition(query2.getCode()).eq(entity.getCode())
        .and(query2.getVar()).eq("test");
});

Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 INNER JOIN Entity2 entity2_1 ON entity2_1.code = entity_0.code AND entity2_1.var = :var_2`

## Joining Maps

You can also join with map collections:

```java
Entity entity = from(Entity.class);
SubEntity innerJoin = innerJoin(entity.getSubEntityMap());
Query<SubEntity> query = select(innerJoin);
```

Generated HQL: `SELECT subEntity_1 FROM Entity entity_0 INNER JOIN entity_0.subEntityMap subEntity_1`

## Advanced Example

Here's a comprehensive example that showcases a chain of joins:

```java
User from = from(User.class);
City city = innerJoin(from.getCity());
with(city.getCode()).in("one", "two").or(city.getCode()).notIn("three", "four");
District district = innerJoin(city.getDistrict());
with(district.getCode()).notIn("exclude1", "exclude2");
State state = innerJoin(district.getState());
with(state.getCode()).eq("AP").or(state.getCode()).eq("GUJ").or(state.getCode()).eq("KTK");
with(state.getCountry().getCode()).eq("india");

Query<User> select = select(from);
```

This example shows how to join multiple entities together with conditions on each join, creating a complex but readable query.