/**
 *
 *   Copyright 2011 Xavier Jodoin xjodoin@gmail.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.torpedoquery.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.torpedoquery.jpa.Torpedo.from;
import static org.torpedoquery.jpa.Torpedo.select;
import static org.torpedoquery.jpa.Torpedo.where;

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

		assertEquals("select entity_0 from Entity entity_0 where entity_0.subEntity.class = ExtendSubEntity", select.getQuery());
		assertTrue(select.getParameters().isEmpty());
	}

	@Test
	public void testNeqClassOfQextendsT() {
		Entity from = from(Entity.class);
		where(from.getSubEntity()).neq(ExtendSubEntity.class);
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.subEntity.class <> ExtendSubEntity", select.getQuery());
		assertTrue(select.getParameters().isEmpty());
	}

	@Test
	public void testEdClassWithFromProxy() {
		Entity from = from(Entity.class);
		where(from).eq(ExtendEntity.class);
		Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 where entity_0.class = ExtendEntity", select.getQuery());
		assertTrue(select.getParameters().isEmpty());
	}

	@Test
	public void testBetweenCondition() {
		Entity from = from(Entity.class);
		where(from.getCode()).between("A", "C");
		Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 where entity_0.code between :code_1 and :code_2", select.getQuery());
		assertEquals("A", select.getParameters().get("code_1"));
		assertEquals("C", select.getParameters().get("code_2"));
	}

	@Test
	public void testNotBetweenCondition() {
		Entity from = from(Entity.class);
		where(from.getCode()).notBetween("A", "C");
		Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 where entity_0.code not between :code_1 and :code_2", select.getQuery());
		assertEquals("A", select.getParameters().get("code_1"));
		assertEquals("C", select.getParameters().get("code_2"));
	}

}
