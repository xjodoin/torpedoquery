package org.torpedoquery.jpa;

import static org.junit.Assert.*;
import static org.torpedoquery.jpa.Torpedo.*;

import org.junit.Test;
import org.torpedoquery.jpa.test.bo.Entity;
import org.torpedoquery.jpa.test.bo.ExtendEntity;
import org.torpedoquery.jpa.test.bo.ExtendSubEntity;

public class ValueOnGoingConditionTest {

	@Test
	public void testEqClassOfQextendsT() {
		Entity from = from(Entity.class);
		where(from.getSubEntity()).eq(ExtendSubEntity.class);
		Query<Entity> select = select(from);

		assertEquals(
				"select entity_0 from Entity entity_0 where entity_0.subEntity.class = ExtendSubEntity",
				select.getQuery());
		assertTrue(select.getParameters().isEmpty());
	}

	@Test
	public void testNeqClassOfQextendsT() {
		Entity from = from(Entity.class);
		where(from.getSubEntity()).neq(ExtendSubEntity.class);
		Query<Entity> select = select(from);

		assertEquals(
				"select entity_0 from Entity entity_0 where entity_0.subEntity.class <> ExtendSubEntity",
				select.getQuery());
		assertTrue(select.getParameters().isEmpty());
	}

	@Test
	public void testEdClassWithFromProxy() {
		Entity from = from(Entity.class);
		where(from).eq(ExtendEntity.class);
		Query<Entity> select = select(from);
		assertEquals(
				"select entity_0 from Entity entity_0 where entity_0.class = ExtendEntity",
				select.getQuery());
		assertTrue(select.getParameters().isEmpty());
	}

}
