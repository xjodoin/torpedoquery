package org.torpedoquery.jpa;

import static org.junit.Assert.*;
import static org.torpedoquery.jpa.Torpedo.*;

import org.junit.Test;
import org.torpedoquery.jpa.test.bo.Entity;
import org.torpedoquery.jpa.test.bo.ProjectionEntity;

public class DynamicIntantiationTest {

	@Test
	public void test() {
		Entity entity = from(Entity.class);

		Query<ProjectionEntity> select = select(dyn(new ProjectionEntity(
				param(entity.getCode()), param(entity.getIntegerField()))));
		assertEquals(
				"select new org.torpedoquery.jpa.test.bo.ProjectionEntity(entity_0.code,entity_0.integerField) from Entity entity_0",
				select.getQuery());
	}

}
