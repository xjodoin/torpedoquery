TorpedoQuery
============
[![Build Status](https://secure.travis-ci.org/xjodoin/torpedoquery.png?branch=master)](http://travis-ci.org/xjodoin/torpedoquery)

Simple and powerful query builder for your project. Can be use with any existing Hibernate or JPA application.  
Stop wasting your time to maintain complex HQL queries and start today with the new generation of query builder.
 
#### Maven ####
```xml
<dependency>
		<groupId>org.torpedoquery</groupId>
		<artifactId>org.torpedoquery</artifactId>
		<version>1.6.0</version>
</dependency>
```
 
  
#### Quick start ####

  	First add this import static org.torpedoquery.jpa.Torpedo.*;
  
  	1. Create simple select
  		
  		Entity entity = from(Entity.class);
 		org.torpedoquery.jpa.Query<Entity> select = select(entity);
 		
 	2. Create scalar queries
 
 		Entity entity = from(Entity.class);
 		org.torpedoquery.jpa.Query<String> select = select(entity.getCode());	
 
   	3. How to execute your query
   
   		Entity entity = from(Entity.class);
 		org.torpedoquery.jpa.Query<Entity> select = select(entity);
 		List<Entity> entityList = select.list(entityManager);
 
 	4. Create simple condition
 
 		Entity entity = from(Entity.class);
 		where(entity.getCode()).eq("mycode");
 		org.torpedoquery.jpa.Query<Entity> select = select(entity);
 
 	5. Create join on your entities
 
 		Entity entity = from(Entity.class);
 		SubEntity subEntity = innerJoin(entity.getSubEntities());
 		org.torpedoquery.jpa.Query<String[]> select = select(entity.getCode(), subEntity.getName());
 
   	6. Group your conditions
   
   		Entity from = from(Entity.class);
 		OnGoingLogicalCondition condition = condition(from.getCode()).eq("test").or(from.getCode()).eq("test2");
 		where(from.getName()).eq("test").and(condition);
 		Query<Entity> select = select(from);


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


