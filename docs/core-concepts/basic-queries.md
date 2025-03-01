# Basic Queries

TorpedoQuery provides a fluent API for building JPA queries. Let's explore the fundamental query operations.

## Creating a Query Root

Every query starts with a call to the `from` method to specify the entity you're querying:

```java
Entity entity = from(Entity.class);
```

This creates a proxy object that captures the methods you call on it to build the query.

## SELECT Queries

### Selecting the Entire Entity

To select all fields from an entity:

```java
Entity entity = from(Entity.class);
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0`

### Selecting Specific Fields

To select specific fields:

```java
Entity entity = from(Entity.class);
Query<String> query = select(entity.getCode());
```

Generated HQL: `SELECT entity_0.code FROM Entity entity_0`

### Selecting Multiple Fields

To select multiple fields (returns an array of Objects):

```java
Entity entity = from(Entity.class);
Query<Object[]> query = select(entity.getCode(), entity.getName());
```

Generated HQL: `SELECT entity_0.code, entity_0.name FROM Entity entity_0`

### Selecting from Chained Properties

You can select properties from nested objects:

```java
Entity entity = from(Entity.class);
Query<String> query = select(entity.getSubEntity().getCode());
```

Generated HQL: `SELECT entity_0.subEntity.code FROM Entity entity_0`

## Executing Queries

### Getting a Single Result

```java
Entity entity = from(Entity.class);
where(entity.getId()).eq("123");
Query<Entity> query = select(entity);
Entity result = query.get(entityManager);
```

### Getting Multiple Results

```java
Entity entity = from(Entity.class);
Query<Entity> query = select(entity);
List<Entity> results = query.list(entityManager);
```

### Parameters Handling

TorpedoQuery automatically handles parameters in your queries. Values are automatically parameterized to prevent SQL injection:

```java
Entity entity = from(Entity.class);
where(entity.getName()).eq("test");
Query<Entity> query = select(entity);

// Get the query string and parameters
String hql = query.getQuery();
Map<String, Object> parameters = query.getParameters();

// Will contain: {"name_1": "test"}
```

## Entity Name Resolution

TorpedoQuery intelligently resolves entity names based on JPA annotations:

```java
// If your entity class has @Entity(name = "myEntity")
@Entity(name = "myEntity")
public class EntityWithAnnotationName {
    // ...
}

// Your query will use the defined name
EntityWithAnnotationName entity = from(EntityWithAnnotationName.class);
Query<EntityWithAnnotationName> query = select(entity);
// Generates: SELECT myEntity_0 FROM myEntity myEntity_0
```

## Query Reuse

Once you've defined a query, it's immutable - which means you can safely reuse it:

```java
Entity entity = from(Entity.class);
Query<Entity> select = select(entity);
// Use select query for one purpose

Query<String> select2 = select(entity.getName());
// Use select2 for another purpose
```

The original `select` query remains unchanged.

## Handling Primitive Types

TorpedoQuery properly handles primitive types in your entities:

```java
Entity entity = from(Entity.class);
where(entity.getPrimitiveInt()).eq(10);
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.primitiveInt = :primitiveInt_1`

## Date and Time Fields

Working with date fields:

```java
Entity entity = from(Entity.class);
where(entity.getDateField()).gt(new Date());
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.dateField > :dateField_1`

## Character Fields

```java
Entity entity = from(Entity.class);
where(entity.getSmallChar()).eq('c');
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.smallChar = :smallChar_1`

## Polymorphic Queries

Working with inheritance hierarchies:

```java
Entity entity = from(Entity.class);
where(entity).eq(ExtendEntity.class);
Query<Entity> query = select(entity);
```

Generated HQL: `SELECT entity_0 FROM Entity entity_0 WHERE entity_0.class = ExtendEntity`

## Dynamic Object Creation

TorpedoQuery supports creating objects directly from query results:

```java
Entity entity = from(Entity.class);
Query<ProjectionEntity> query = select(dyn(new ProjectionEntity(
    param(entity.getCode()), param(entity.getIntegerField()))));
```

Generated HQL: `SELECT new org.torpedoquery.jpa.test.bo.ProjectionEntity(entity_0.code, entity_0.integerField) FROM Entity entity_0`

This technique is useful for projections and DTOs.