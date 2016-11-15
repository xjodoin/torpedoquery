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
import static org.junit.Assert.assertTrue;
import static org.torpedoquery.jpa.Torpedo.and;
import static org.torpedoquery.jpa.Torpedo.condition;
import static org.torpedoquery.jpa.Torpedo.from;
import static org.torpedoquery.jpa.Torpedo.or;
import static org.torpedoquery.jpa.Torpedo.select;
import static org.torpedoquery.jpa.Torpedo.where;
import static org.torpedoquery.jpa.TorpedoFunction.length;
import static org.torpedoquery.jpa.TorpedoFunction.lower;

import java.util.Arrays;

import org.junit.Test;
import org.torpedoquery.jpa.test.bo.Entity;
import org.torpedoquery.jpa.test.bo.ExtendEntity;
import org.torpedoquery.jpa.test.bo.ExtendSubEntity;
public class ValueOnGoingConditionTest {

	/**
	 * <p>testEqClassOfQextendsT.</p>
	 */
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

	/**
	 * <p>testNeqClassOfQextendsT.</p>
	 */
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

	/**
	 * <p>testEdClassWithFromProxy.</p>
	 */
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

	/**
	 * <p>testBetweenCondition.</p>
	 */
	@Test
	public void testBetweenCondition() {
		Entity from = from(Entity.class);
		where(from.getCode()).between("A", "C");
		Query<Entity> select = select(from);
		assertEquals(
				"select entity_0 from Entity entity_0 where entity_0.code between :code_1 and :code_2",
				select.getQuery());
		assertEquals("A", select.getParameters().get("code_1"));
		assertEquals("C", select.getParameters().get("code_2"));
	}

	/**
	 * <p>testNotBetweenCondition.</p>
	 */
	@Test
	public void testNotBetweenCondition() {
		Entity from = from(Entity.class);
		where(from.getCode()).notBetween("A", "C");
		Query<Entity> select = select(from);
		assertEquals(
				"select entity_0 from Entity entity_0 where entity_0.code not between :code_1 and :code_2",
				select.getQuery());
		assertEquals("A", select.getParameters().get("code_1"));
		assertEquals("C", select.getParameters().get("code_2"));
	}

	/**
	 * <p>testLowerFunctionInCondition.</p>
	 */
	@Test
	public void testLowerFunctionInCondition() {
		Entity entity = from(Entity.class);
		OnGoingLogicalCondition condition = condition(lower(entity.getCode()))
				.like().any("test");
		where(condition);
		Query<Entity> select = select(entity);
		assertEquals(
				"select entity_0 from Entity entity_0 where ( lower(entity_0.code) like '%test%'  )",
				select.getQuery());
	}

	/**
	 * <p>testComparableFunctionInCondition.</p>
	 */
	@Test
	public void testComparableFunctionInCondition() {
		Entity entity = from(Entity.class);
		OnGoingLogicalCondition condition = condition(length(entity.getCode()))
				.gt(5);
		where(condition);
		Query<Entity> select = select(entity);
		assertEquals(
				"select entity_0 from Entity entity_0 where ( length(entity_0.code) > :function_1 )",
				select.getQuery());
	}

	/**
	 * <p>testOrMultipleOnGoingLogicalConditions.</p>
	 */
	@Test
	public void testOrMultipleOnGoingLogicalConditions() {
		Entity entity = from(Entity.class);
		OnGoingLogicalCondition condition = condition(entity.getCode()).eq(
				"test");
		OnGoingLogicalCondition condition2 = condition(entity.getCode()).eq(
				"test2");

		where(or(condition, condition2));
		Query<Entity> select = select(entity);
		assertEquals(
				"select entity_0 from Entity entity_0 where ( ( entity_0.code = :code_1 ) or ( entity_0.code = :code_2 ) )",
				select.getQuery());
	}

	/**
	 * <p>testAndMultipleOnGoingLogicalConditions2.</p>
	 */
	@Test
	public void testAndMultipleOnGoingLogicalConditions2() {
		Entity entity = from(Entity.class);
		OnGoingLogicalCondition condition = condition(entity.getCode()).eq(
				"test");
		OnGoingLogicalCondition condition2 = condition(entity.getCode()).eq(
				"test2");

		where(and(Arrays.asList(condition, condition2)));
		Query<Entity> select = select(entity);
		assertEquals(
				"select entity_0 from Entity entity_0 where ( ( entity_0.code = :code_1 ) and ( entity_0.code = :code_2 ) )",
				select.getQuery());
	}

}
