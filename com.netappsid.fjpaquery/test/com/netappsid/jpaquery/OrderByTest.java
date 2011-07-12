package com.netappsid.jpaquery;

import static com.netappsid.jpaquery.FJPAQuery.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.netappsid.jpaquery.test.bo.Entity;
import com.netappsid.jpaquery.test.bo.SubEntity;

public class OrderByTest {

	@Test
	public void test_simpleOrderBy() {
		Entity from = from(Entity.class);
		orderBy(from.getCode());
		assertEquals("from Entity entity_0 order by entity_0.code", query(from));
	}

	@Test
	public void test_multipleOrderBy() {
		Entity from = from(Entity.class);
		orderBy(from.getCode(), from.getName());
		assertEquals("from Entity entity_0 order by entity_0.code,entity_0.name", query(from));
	}

	@Test
	public void test_OrderByOnJoin() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntity());

		orderBy(innerJoin.getCode());
		assertEquals("from Entity entity_0 inner join entity_0.subEntity subEntity_1 order by subEntity_1.code", query(from));
	}

	@Test
	public void test_OrderByTwoLevel() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntity());

		orderBy(from.getCode(), innerJoin.getCode());
		assertEquals("from Entity entity_0 inner join entity_0.subEntity subEntity_1 order by entity_0.code,subEntity_1.code", query(from));
	}

	@Test
	public void test_simpleOrderBy_asc() {
		Entity from = from(Entity.class);
		orderBy(asc(from.getCode()));
		assertEquals("from Entity entity_0 order by entity_0.code asc", query(from));
	}

	@Test
	public void test_simpleOrderBy_desc() {
		Entity from = from(Entity.class);
		orderBy(desc(from.getCode()));
		assertEquals("from Entity entity_0 order by entity_0.code desc", query(from));
	}

	@Test
	public void test_simpleOrderBy_asc_and_default() {
		Entity from = from(Entity.class);
		orderBy(asc(from.getCode()), from.getName());
		assertEquals("from Entity entity_0 order by entity_0.code asc,entity_0.name", query(from));
	}
}
