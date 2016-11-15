/**
 * Copyright (C) 2011 Xavier Jodoin (xavier@jodoin.me)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.torpedoquery.jpa;

import static org.junit.Assert.assertEquals;
import static org.torpedoquery.jpa.Torpedo.from;
import static org.torpedoquery.jpa.Torpedo.innerJoin;
import static org.torpedoquery.jpa.Torpedo.orderBy;
import static org.torpedoquery.jpa.Torpedo.select;
import static org.torpedoquery.jpa.TorpedoFunction.asc;
import static org.torpedoquery.jpa.TorpedoFunction.desc;

import org.junit.Test;
import org.torpedoquery.jpa.test.bo.Entity;
import org.torpedoquery.jpa.test.bo.SubEntity;
public class OrderByTest {

	/**
	 * <p>test_simpleOrderBy.</p>
	 */
	@Test
	public void test_simpleOrderBy() {
		Entity from = from(Entity.class);
		orderBy(from.getCode());
		Query<Entity> select = select(from);
		
		assertEquals("select entity_0 from Entity entity_0 order by entity_0.code", select.getQuery());
	}

	/**
	 * <p>test_multipleOrderBy.</p>
	 */
	@Test
	public void test_multipleOrderBy() {
		Entity from = from(Entity.class);
		orderBy(from.getCode(), from.getName());
		Query<Entity> select = select(from);
		
		assertEquals("select entity_0 from Entity entity_0 order by entity_0.code,entity_0.name", select.getQuery());
	}

	/**
	 * <p>test_OrderByOnJoin.</p>
	 */
	@Test
	public void test_OrderByOnJoin() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntity());

		orderBy(innerJoin.getCode());
		Query<Entity> select = select(from);
		
		assertEquals("select entity_0 from Entity entity_0 inner join entity_0.subEntity subEntity_1 order by subEntity_1.code", select.getQuery());
	}

	/**
	 * <p>test_OrderByTwoLevel.</p>
	 */
	@Test
	public void test_OrderByTwoLevel() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntity());

		orderBy(from.getCode(), innerJoin.getCode());
		Query<Entity> select = select(from);
		
		assertEquals("select entity_0 from Entity entity_0 inner join entity_0.subEntity subEntity_1 order by entity_0.code,subEntity_1.code", select.getQuery());
	}

	/**
	 * <p>test_simpleOrderBy_asc.</p>
	 */
	@Test
	public void test_simpleOrderBy_asc() {
		Entity from = from(Entity.class);
		orderBy(asc(from.getCode()));
		Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 order by entity_0.code asc", select.getQuery());
	}

	/**
	 * <p>test_simpleOrderBy_desc.</p>
	 */
	@Test
	public void test_simpleOrderBy_desc() {
		Entity from = from(Entity.class);
		orderBy(desc(from.getCode()));
		Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 order by entity_0.code desc", select.getQuery());
	}

	/**
	 * <p>test_simpleOrderBy_asc_and_default.</p>
	 */
	@Test
	public void test_simpleOrderBy_asc_and_default() {
		Entity from = from(Entity.class);
		orderBy(asc(from.getCode()), from.getName());
		Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 order by entity_0.code asc,entity_0.name", select.getQuery());
	}
}
