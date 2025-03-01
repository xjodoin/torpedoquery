# TorpedoQuery

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.torpedoquery/org.torpedoquery/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.torpedoquery/org.torpedoquery)
[![license](https://img.shields.io/github/license/xjodoin/torpedoquery.svg)](https://github.com/xjodoin/torpedoquery/blob/master/LICENSE)

TorpedoQuery is a simple and powerful query builder for your Java projects. It can be used with any existing Hibernate or JPA application.

Stop wasting your time maintaining complex HQL queries and start today with the new generation of query builders.

## Why TorpedoQuery?

- **Type-safe**: Leverage Java's type system to avoid runtime errors
- **Refactoring-friendly**: Changes to your entity model are immediately reflected in your queries
- **Intuitive API**: Chain methods naturally to build complex queries
- **Integration-ready**: Works with existing JPA/Hibernate infrastructure
- **Zero configuration**: Simply import the library and start using it

## Quick Example

```java
// Import the static methods
import static org.torpedoquery.jpa.Torpedo.*;

// Create a simple query
Entity entity = from(Entity.class);
where(entity.getCode()).eq("mycode");
Query<Entity> query = select(entity);

// Execute the query using your EntityManager
List<Entity> results = query.list(entityManager);
```

## Features

- Create queries with a fluent, type-safe API
- Support for all major JPA operations:
  - SELECT, FROM, WHERE, JOIN, GROUP BY, ORDER BY
  - Subqueries
  - Functions (COUNT, SUM, AVG, etc.)
  - Dynamic query building
- Integration with standard JPA EntityManager
- Minimal dependencies
- Small footprint

## Getting Started

Check out the [Quick Start](getting-started/quick-start.md) guide to begin using TorpedoQuery in your application.

## Security

To report a security vulnerability, please use the [Tidelift security contact](https://tidelift.com/security). Tidelift will coordinate the fix and disclosure.