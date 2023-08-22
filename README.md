TorpedoQuery
============

## Status

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.torpedoquery/org.torpedoquery/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.torpedoquery/org.torpedoquery)
[![license](https://img.shields.io/github/license/xjodoin/torpedoquery.svg)](https://github.com/xjodoin/torpedoquery/blob/master/LICENSE)




Simple and powerful query builder for your project. Can be use with any existing Hibernate or JPA application.  
Stop wasting your time to maintain complex HQL queries and start today with the new generation of query builder.

**Torpedo Query Quick Start Guide**

**Step 1:** **Set Up Your Environment**

Start by importing the necessary classes from the Torpedo Query library. This will enable you to use the query methods directly.
```java
import static org.torpedoquery.jpa.Torpedo.*;
```

**Step 2:** **Simple Select Query**

A basic query retrieves all columns for the Entity rows. This can be compared to SQL's `SELECT *`.
```java
Entity entity = from(Entity.class);
org.torpedoquery.jpa.Query<Entity> selectQuery = select(entity);
```

**Step 3:** **Scalar Queries**

Scalar queries return specific columns rather than the entire row. This is useful when you only need particular data points.
```java
Entity entity = from(Entity.class);
org.torpedoquery.jpa.Query<String> scalarQuery = select(entity.getCode());
```

**Step 4:** **Executing Your Query**

After constructing your query, execute it using an `EntityManager` to retrieve results. Here, we're getting a list of entities.
```java
Entity entity = from(Entity.class);
org.torpedoquery.jpa.Query<Entity> selectQuery = select(entity);
List<Entity> entityList = selectQuery.list(entityManager);
```

**Step 5:** **Adding Conditions**

Queries can be filtered using conditions. Here, we're querying entities with a specific code.
```java
Entity entity = from(Entity.class);
where(entity.getCode()).eq("mycode");
org.torpedoquery.jpa.Query<Entity> conditionalQuery = select(entity);
```
The `.eq("mycode")` is equivalent to SQL's `WHERE code = 'mycode'`.

**Step 6:** **Joining Entities**

Torpedo Query supports joining tables. Here's how you can create an inner join between `Entity` and its associated `SubEntity`.
```java
Entity entity = from(Entity.class);
SubEntity subEntity = innerJoin(entity.getSubEntities());
org.torpedoquery.jpa.Query<String[]> joinQuery = select(entity.getCode(), subEntity.getName());
```
The result will be a combination of `entity.getCode()` and `subEntity.getName()` for each matching row.

**Step 7:** **Grouping Conditions**

Complex conditions can be grouped together using logical operations like `and` & `or`.
```java
Entity fromEntity = from(Entity.class);
OnGoingLogicalCondition groupedCondition = condition(fromEntity.getCode()).eq("test")
                                               .or(fromEntity.getCode()).eq("test2");
where(fromEntity.getName()).eq("test").and(groupedCondition);
Query<Entity> groupedSelect = select(fromEntity);
```
Here, we're selecting entities where the name equals "test" and the code is either "test" or "test2".

---

Remember, always refer to the official documentation of Torpedo Query for a comprehensive understanding and best practices. This guide is meant to get you started quickly, but the library offers much more flexibility and depth.


#### How to Improve It ####

Create your own fork of [xjodoin/torpedoquery](https://github.com/xjodoin/torpedoquery)

To share your changes, [submit a pull request](https://github.com/xjodoin/torpedoquery/pull/new/master).

Don't forget to add new units tests on your change.


#### License ####

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  
## Security contact information

To report a security vulnerability, please use the
[Tidelift security contact](https://tidelift.com/security).
Tidelift will coordinate the fix and disclosure.  
