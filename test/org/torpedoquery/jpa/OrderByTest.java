package org.torpedoquery.jpa;

import static org.junit.Assert.*;
import static org.torpedoquery.jpa.Torpedo.*;

import org.junit.Test;
import org.torpedoquery.jpa.test.bo.Entity;
import org.torpedoquery.jpa.test.bo.SubEntity;

public class OrderByTest {

	@Test
	public void test_simpleOrderBy() {
		Entity from = from(Entity.class);
		orderBy(from.getCode());
		Query<Entity> select = select(from);
		
		assertEquals("select entity_0 from Entity entity_0 order by entity_0.code", select.getQuery());
	}

	@Test
	public void test_multipleOrderBy() {
		Entity from = from(Entity.class);
		orderBy(from.getCode(), from.getName());
		Query<Entity> select = select(from);
		
		assertEquals("select entity_0 from Entity entity_0 order by entity_0.code,entity_0.name", select.getQuery());
	}

	@Test
	public void test_OrderByOnJoin() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntity());

		orderBy(innerJoin.getCode());
		Query<Entity> select = select(from);
		
		assertEquals("select entity_0 from Entity entity_0 inner join entity_0.subEntity subEntity_1 order by subEntity_1.code", select.getQuery());
	}

	@Test
	public void test_OrderByTwoLevel() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntity());

		orderBy(from.getCode(), innerJoin.getCode());
		Query<Entity> select = select(from);
		
		assertEquals("select entity_0 from Entity entity_0 inner join entity_0.subEntity subEntity_1 order by entity_0.code,subEntity_1.code", select.getQuery());
	}

	@Test
	public void test_simpleOrderBy_asc() {
		Entity from = from(Entity.class);
		orderBy(asc(from.getCode()));
		Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 order by entity_0.code asc", select.getQuery());
	}

	@Test
	public void test_simpleOrderBy_desc() {
		Entity from = from(Entity.class);
		orderBy(desc(from.getCode()));
		Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 order by entity_0.code desc", select.getQuery());
	}

	@Test
	public void test_simpleOrderBy_asc_and_default() {
		Entity from = from(Entity.class);
		orderBy(asc(from.getCode()), from.getName());
		Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 order by entity_0.code asc,entity_0.name", select.getQuery());
	}
}
