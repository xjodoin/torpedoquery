package org.torpedoquery.jpa;

import org.junit.Test;
import org.torpedoquery.jpa.test.bo.Entity;
import org.torpedoquery.jpa.test.bo.Entity2;

import static org.junit.Assert.*;
import static org.torpedoquery.jpa.Torpedo.*;

public class LeftJoinFetchTest {

	@Test
	public void testJoinOnTwoEntityField() {
		Entity from = from(Entity.class);
		Entity2 entity2 = leftJoinFetch(Entity2.class).on(query2 -> {
			return condition(query2.getCode()).eq(from.getCode());
		});
		
		Query<Entity> select = select(from);
		String query = select.getQuery();
		assertEquals("select entity_0 from Entity entity_0 left join fetch Entity2 entity2_1 on entity2_1.code = entity_0.code", query);
	}
	
	@Test
	public void testJoinOnTwoEntityFieldWithCondition() {
		Entity from = from(Entity.class);
		Entity2 entity2 = leftJoinFetch(Entity2.class).on(query2 -> {
			return condition(query2.getCode()).eq(from.getCode());
		});
		
		where(entity2.getCode()).eq("test");
		
		Query<Entity> select = select(from);
		String query = select.getQuery();
		assertEquals("select entity_0 from Entity entity_0 left join fetch Entity2 entity2_1 on entity2_1.code = entity_0.code where entity2_1.code = :code_2", query);
		assertEquals(select.getParameters().get("code_2"), "test");
	}
	
	
}
