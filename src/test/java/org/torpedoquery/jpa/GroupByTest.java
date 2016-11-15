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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.torpedoquery.jpa.Torpedo.condition;
import static org.torpedoquery.jpa.Torpedo.from;
import static org.torpedoquery.jpa.Torpedo.groupBy;
import static org.torpedoquery.jpa.Torpedo.innerJoin;
import static org.torpedoquery.jpa.Torpedo.select;
import static org.torpedoquery.jpa.TorpedoFunction.coalesce;
import static org.torpedoquery.jpa.TorpedoFunction.constant;
import static org.torpedoquery.jpa.TorpedoFunction.sum;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.Test;
import org.torpedoquery.jpa.test.bo.Entity;
import org.torpedoquery.jpa.test.bo.SubEntity;
public class GroupByTest {

	/**
	 * <p>testSingleGroubBy.</p>
	 */
	@Test
	public void testSingleGroubBy() {
		Entity from = from(Entity.class);
		groupBy(from.getName());
		Query<Object[]> select = select(from.getName(), sum(from.getIntegerField()));
		String query = select.getQuery();

		assertEquals("select entity_0.name, sum(entity_0.integerField) from Entity entity_0 group by entity_0.name", query);
	}

	/**
	 * <p>testGroubBy_with_having_clause.</p>
	 */
	@Test
	public void testGroubBy_with_having_clause() {
		Entity from = from(Entity.class);
		groupBy(from.getName()).having(from.getName()).eq("test");
		Query<Object[]> select = select(from.getName(), sum(from.getIntegerField()));
		String query = select.getQuery();

		assertEquals("select entity_0.name, sum(entity_0.integerField) from Entity entity_0 group by entity_0.name having entity_0.name = :name_1",
				query);
		assertEquals("test", select.getParameters().get("name_1"));
	}

	/**
	 * <p>testGroubBy_with_having_with_function.</p>
	 */
	@Test
	public void testGroubBy_with_having_with_function() {
		Entity from = from(Entity.class);
		SubEntity subEntity = innerJoin(from.getSubEntities());
		groupBy(from.getName()).having(sum(from.getIntegerField())).lt(sum(subEntity.getNumberField()));
		Query<String> select = select(from.getName());
		String query = select.getQuery();

		assertEquals(
				"select entity_0.name from Entity entity_0 inner join entity_0.subEntities subEntity_1 group by entity_0.name having sum(entity_0.integerField) < sum(subEntity_1.numberField)",
				query);
	}
	
	/**
	 * <p>testGroubBy_with_having_with_groupCondition.</p>
	 */
	@Test
	public void testGroubBy_with_having_with_groupCondition() {
		Entity from = from(Entity.class);
		OnGoingLogicalCondition condition = condition(from.getName()).eq("test").or(from.getName()).eq("test2");
		groupBy(from.getName()).having(condition).and(sum(from.getIntegerField())).gt(2);
		Query<String> select = select(from.getName());
		String query = select.getQuery();

		assertEquals(
				"select entity_0.name from Entity entity_0 group by entity_0.name having ( entity_0.name = :name_1 or entity_0.name = :name_2 ) and sum(entity_0.integerField) > :function_3",
				query);
	}
	
	/**
	 * <p>testGroubBy_with_having_with_groupCondition_reverse.</p>
	 */
	@Test
	public void testGroubBy_with_having_with_groupCondition_reverse() {
		Entity from = from(Entity.class);
		OnGoingLogicalCondition condition = condition(from.getName()).eq("test").or(from.getName()).eq("test2");
		groupBy(from.getName()).having(sum(from.getIntegerField())).gt(2).and(condition);
		Query<String> select = select(from.getName());
		String query = select.getQuery();

		assertEquals(
				"select entity_0.name from Entity entity_0 group by entity_0.name having sum(entity_0.integerField) > :function_1 and ( entity_0.name = :name_2 or entity_0.name = :name_3 )",
				query);
	}
	
	/**
	 * <p>testHavingWithFunction_withProxyRelativeParameter.</p>
	 */
	@Test
	public void testHavingWithFunction_withProxyRelativeParameter()
	{
		Entity from = from(Entity.class);
		SubEntity subEntity = innerJoin(from.getSubEntities());
		groupBy(from.getName()).having(sum(from.getIntegerField())).lt(subEntity.getNumberField());
		Query<String> select = select(from.getName());
		String query = select.getQuery();

		assertEquals(
				"select entity_0.name from Entity entity_0 inner join entity_0.subEntities subEntity_1 group by entity_0.name having sum(entity_0.integerField) < subEntity_1.numberField",
				query);
		assertTrue(select.getParameters().isEmpty());
	}
	
	/**
	 * <p>testHavingFunctionParameterMustBeConvertToString.</p>
	 */
	@Test
	public void testHavingFunctionParameterMustBeConvertToString()
	{
		Entity from = from(Entity.class);
		groupBy(from.getIntegerField()).having(from.getBigDecimalField()).gt(coalesce(sum(from.getBigDecimalField2()),constant(BigDecimal.ZERO)));
		Query<Integer> select = select(sum(from.getIntegerField()));
		assertEquals("select sum(entity_0.integerField) from Entity entity_0 group by entity_0.integerField having entity_0.bigDecimalField > coalesce(sum(entity_0.bigDecimalField2),0)", select.getQuery());
	}
	
	/**
	 * GitHub -> Bug 14
	 */
	@Test
	public void groupByCombineWithConditionParameterintoHavingClause()
	{
		Entity from = from(Entity.class);
		OnGoingLogicalCondition condition = condition(from.isActive()).eq(true);
		groupBy(from.getName()).having(sum(from.getIntegerField())).gt(sum(from.getPrimitiveInt())).or(condition);
		Query<Entity> select = select(from);
		Map<String, Object> parameters = select.getParameters();
		assertFalse(parameters.isEmpty());
		assertEquals(true, parameters.get("active_1"));
		assertEquals("select entity_0 from Entity entity_0 group by entity_0.name having sum(entity_0.integerField) > sum(entity_0.primitiveInt) or ( entity_0.active = :active_1 )", select.getQuery());
	}
}

