TorpedoQuery
============

Torpedo Query goal is to simplify how you create and maintain your HQL query.
  	(http://docs.jboss.org/hibernate/core/3.3/reference/en/html/queryhql.html)	
  
  	(All following examples are extract from Torpedo's Tests cases)
  
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
