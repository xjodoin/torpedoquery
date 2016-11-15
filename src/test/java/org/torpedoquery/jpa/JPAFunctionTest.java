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
import static org.torpedoquery.jpa.Torpedo.groupBy;
import static org.torpedoquery.jpa.Torpedo.innerJoin;
import static org.torpedoquery.jpa.Torpedo.select;
import static org.torpedoquery.jpa.Torpedo.where;
import static org.torpedoquery.jpa.TorpedoFunction.avg;
import static org.torpedoquery.jpa.TorpedoFunction.coalesce;
import static org.torpedoquery.jpa.TorpedoFunction.count;
import static org.torpedoquery.jpa.TorpedoFunction.distinct;
import static org.torpedoquery.jpa.TorpedoFunction.function;
import static org.torpedoquery.jpa.TorpedoFunction.index;
import static org.torpedoquery.jpa.TorpedoFunction.max;
import static org.torpedoquery.jpa.TorpedoFunction.min;
import static org.torpedoquery.jpa.TorpedoFunction.sum;

import java.math.BigDecimal;

import org.junit.Test;
import org.torpedoquery.jpa.test.bo.Entity;
import org.torpedoquery.jpa.test.bo.SubEntity;
public class JPAFunctionTest {

	/**
	 * <p>testCountFunction_defaultCount.</p>
	 */
	@Test
	public void testCountFunction_defaultCount() {
		Entity from = from(Entity.class);
		Query<Long> select = select(count(from));
		assertEquals("select count(entity_0) from Entity entity_0", select.getQuery());
	}

	/**
	 * <p>testCountFunction_withSpecifiedField.</p>
	 */
	@Test
	public void testCountFunction_withSpecifiedField() {
		Entity from = from(Entity.class);
		Query<Long> select = select(count(from.getCode()));
		assertEquals("select count(entity_0.code) from Entity entity_0",
				select.getQuery());
	}

	/**
	 * <p>testCountFunction_withSpecifiedField_plusOneSelect.</p>
	 */
	@Test
	public void testCountFunction_withSpecifiedField_plusOneSelect() {
		Entity from = from(Entity.class);
		Query<Object[]> select = select(count(from.getCode()), from.getCode());
		assertEquals(
				"select count(entity_0.code), entity_0.code from Entity entity_0",
				select.getQuery());
	}

	/**
	 * <p>testCountFunction_withSpecifiedField_plusOneSelect_inverse.</p>
	 */
	@Test
	public void testCountFunction_withSpecifiedField_plusOneSelect_inverse() {
		Entity from = from(Entity.class);
		Query<Object[]> select = select(from.getCode(), count(from.getCode()));
		assertEquals(
				"select entity_0.code, count(entity_0.code) from Entity entity_0",
				select.getQuery());
	}

	/**
	 * <p>testSumFunction.</p>
	 */
	@Test
	public void testSumFunction() {
		Entity from = from(Entity.class);
		Query<Integer> select = select(sum(from.getIntegerField()));
		assertEquals("select sum(entity_0.integerField) from Entity entity_0",
				select.getQuery());
	}

	/**
	 * <p>testMinFunction.</p>
	 */
	@Test
	public void testMinFunction() {
		Entity from = from(Entity.class);
		Query<Integer> select = select(min(from.getIntegerField()));
		assertEquals("select min(entity_0.integerField) from Entity entity_0",
				select.getQuery());
	}

	/**
	 * <p>testMaxFunction.</p>
	 */
	@Test
	public void testMaxFunction() {
		Entity from = from(Entity.class);
		Query<Integer> select = select(max(from.getIntegerField()));
		assertEquals("select max(entity_0.integerField) from Entity entity_0",
				select.getQuery());
	}

	/**
	 * <p>testAvgFunction.</p>
	 */
	@Test
	public void testAvgFunction() {
		Entity from = from(Entity.class);
		Query<Integer> select = select(avg(from.getIntegerField()));
		assertEquals("select avg(entity_0.integerField) from Entity entity_0",
				select.getQuery());
	}

	/**
	 * <p>testCoalesceFunction.</p>
	 */
	@Test
	public void testCoalesceFunction() {
		Entity from = from(Entity.class);
		Query<String> select = select(coalesce(from.getCode(), from.getName()));
		assertEquals(
				"select coalesce(entity_0.code,entity_0.name) from Entity entity_0",
				select.getQuery());
	}

	/**
	 * <p>testDistinctEntity.</p>
	 */
	@Test
	public void testDistinctEntity() {
		Entity from = from(Entity.class);
		Query<Entity> select = select(distinct(from));
		assertEquals("select distinct entity_0 from Entity entity_0",
				select.getQuery());
	}

	/**
	 * <p>testDistinctOnField.</p>
	 */
	@Test
	public void testDistinctOnField() {
		Entity from = from(Entity.class);
		Query<String> select = select(distinct(from.getCode()));
		assertEquals("select distinct entity_0.code from Entity entity_0",
				select.getQuery());
	}

	/**
	 * <p>testCombiningFunctionWithInnerJoin.</p>
	 */
	@Test
	public void testCombiningFunctionWithInnerJoin() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntities());
		Query<Object[]> select = select(distinct(from), innerJoin);
		assertEquals(
				"select distinct entity_0, subEntity_1 from Entity entity_0 inner join entity_0.subEntities subEntity_1",
				select.getQuery());
	}

	/**
	 * <p>testFunctionOnObjectWithoutConstructor.</p>
	 */
	@Test
	public void testFunctionOnObjectWithoutConstructor() {
		Entity from = from(Entity.class);
		groupBy(from.getCode());
		Query<BigDecimal> select = select(sum(from.getBigDecimalField()));

		assertEquals(
				"select sum(entity_0.bigDecimalField) from Entity entity_0 group by entity_0.code",
				select.getQuery());

	}

	/**
	 * <p>testSpecifyFieldBeforeFunctionCount.</p>
	 */
	@Test
	public void testSpecifyFieldBeforeFunctionCount() {
		Entity from = from(Entity.class);
		groupBy(from.getCode());
		Query<Object[]> select = select(from.getCode(), count(from));

		assertEquals(
				"select entity_0.code, count(entity_0) from Entity entity_0 group by entity_0.code",
				select.getQuery());
	}

	/**
	 * <p>testIndexFunction.</p>
	 */
	@Test
	public void testIndexFunction() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntities());
		Query<Object[]> select = select(innerJoin, index(innerJoin));
		assertEquals(
				"select subEntity_1, index(subEntity_1) from Entity entity_0 inner join entity_0.subEntities subEntity_1",
				select.getQuery());
	}

	/**
	 * <p>testIndexFunction_in_where.</p>
	 */
	@Test
	public void testIndexFunction_in_where() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntities());
		where(index(innerJoin)).gt(5);
		Query<Object[]> select = select(innerJoin, index(innerJoin));
		assertEquals(
				"select subEntity_1, index(subEntity_1) from Entity entity_0 inner join entity_0.subEntities subEntity_1 where index(subEntity_1) > :function_2",
				select.getQuery());
		assertEquals(5, select.getParameters().get("function_2"));
	}

	/**
	 * <p>testIndexFunction_in_with_equal.</p>
	 */
	@Test
	public void testIndexFunction_in_with_equal() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntities());
		where(index(innerJoin)).eq(5);
		Query<Object[]> select = select(innerJoin, index(innerJoin));
		assertEquals(
				"select subEntity_1, index(subEntity_1) from Entity entity_0 inner join entity_0.subEntities subEntity_1 where index(subEntity_1) = :function_2",
				select.getQuery());
		assertEquals(5, select.getParameters().get("function_2"));
	}

	/**
	 * <p>testIndexFunction_into_Or.</p>
	 */
	@Test
	public void testIndexFunction_into_Or() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntities());
		where(index(innerJoin)).eq(5).or(index(innerJoin)).lt(2);
		Query<Object[]> select = select(innerJoin, index(innerJoin));
		assertEquals(
				"select subEntity_1, index(subEntity_1) from Entity entity_0 inner join entity_0.subEntities subEntity_1 where index(subEntity_1) = :function_2 or index(subEntity_1) < :function_3",
				select.getQuery());
		assertEquals(2, select.getParameters().get("function_3"));
	}

	/**
	 * <p>testSupportCustomFunction.</p>
	 */
	@Test
	public void testSupportCustomFunction() {
		Entity from = from(Entity.class);
		Query<String> select = select(function("toto", String.class,
				from.getName()));
		assertEquals("select toto(entity_0.name) from Entity entity_0",
				select.getQuery());
	}

	/**
	 * <p>testCustomFunctionWithFunction.</p>
	 */
	@Test
	public void testCustomFunctionWithFunction() {
		Entity from = from(Entity.class);
		Query<String> select = select(function("toto", String.class,
				max(from.getIntegerField())));
		assertEquals(
				"select toto(max(entity_0.integerField)) from Entity entity_0",
				select.getQuery());
	}

	/**
	 * <p>testDistinctOnInterface.</p>
	 */
	@Test
	public void testDistinctOnInterface() {
		Entity fromOrder = from(Entity.class);
		Query select = select(distinct(fromOrder.getInterface()));
		assertEquals(
				"select distinct entity_0.interface from Entity entity_0",
				select.getQuery());
	}
	
	/**
	 * <p>testCountDistinctWorkaround.</p>
	 */
	@Test
	public void testCountDistinctWorkaround() {
		Entity fromOrder = from(Entity.class);
		Query<Long> select = select(function("count", Long.class,
				distinct(fromOrder.getInterface())));
		assertEquals(
				"select count(distinct entity_0.interface) from Entity entity_0",
				select.getQuery());
	}

	/**
	 * <p>testCountDistinct.</p>
	 */
	@Test
	public void testCountDistinct() {
		Entity fromOrder = from(Entity.class);
		// throws an NPE
		Query<Long> select = select(count(distinct(fromOrder.getInterface())));
		assertEquals(
				"select count(distinct entity_0.interface) from Entity entity_0",
				select.getQuery());
	}
	
}
