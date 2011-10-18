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

import static org.junit.Assert.*;
import static org.torpedoquery.jpa.Torpedo.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.torpedoquery.jpa.test.bo.Entity;
import org.torpedoquery.jpa.test.bo.SubEntity;

public class JPAFunctionTest {

	@Test
	public void testCountFunction_defaultCount() {
		Entity from = from(Entity.class);
		Query<Long> select = select(count(from));
		assertEquals("select count(*) from Entity entity_0", select.getQuery());
	}

	@Test
	public void testCountFunction_withSpecifiedField() {
		Entity from = from(Entity.class);
		Query<Long> select = select(count(from.getCode()));
		assertEquals("select count(entity_0.code) from Entity entity_0", select.getQuery());
	}

	@Test
	public void testCountFunction_withSpecifiedField_plusOneSelect() {
		Entity from = from(Entity.class);
		Query<Object[]> select = select(count(from.getCode()), from.getCode());
		assertEquals("select count(entity_0.code), entity_0.code from Entity entity_0", select.getQuery());
	}

	@Test
	public void testCountFunction_withSpecifiedField_plusOneSelect_inverse() {
		Entity from = from(Entity.class);
		Query<Object[]> select = select(from.getCode(), count(from.getCode()));
		assertEquals("select entity_0.code, count(entity_0.code) from Entity entity_0", select.getQuery());
	}

	@Test
	public void testSumFunction() {
		Entity from = from(Entity.class);
		Query<Integer> select = select(sum(from.getIntegerField()));
		assertEquals("select sum(entity_0.integerField) from Entity entity_0", select.getQuery());
	}

	@Test
	public void testMinFunction() {
		Entity from = from(Entity.class);
		Query<Integer> select = select(min(from.getIntegerField()));
		assertEquals("select min(entity_0.integerField) from Entity entity_0", select.getQuery());
	}

	@Test
	public void testMaxFunction() {
		Entity from = from(Entity.class);
		Query<Integer> select = select(max(from.getIntegerField()));
		assertEquals("select max(entity_0.integerField) from Entity entity_0", select.getQuery());
	}

	@Test
	public void testAvgFunction() {
		Entity from = from(Entity.class);
		Query<Integer> select = select(avg(from.getIntegerField()));
		assertEquals("select avg(entity_0.integerField) from Entity entity_0", select.getQuery());
	}

	@Test
	public void testCoalesceFunction() {
		Entity from = from(Entity.class);
		Query<String> select = select(coalesce(from.getCode(), from.getName()));
		assertEquals("select coalesce(entity_0.code,entity_0.name) from Entity entity_0", select.getQuery());
	}

	@Test
	public void testDistinctEntity() {
		Entity from = from(Entity.class);
		Query<Entity> select = select(distinct(from));
		assertEquals("select distinct entity_0 from Entity entity_0", select.getQuery());
	}

	@Test
	public void testDistinctOnField() {
		Entity from = from(Entity.class);
		Query<String> select = select(distinct(from.getCode()));
		assertEquals("select distinct entity_0.code from Entity entity_0", select.getQuery());
	}

	@Test
	public void testCombiningFunctionWithInnerJoin() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntities());
		Query<Object[]> select = select(distinct(from), innerJoin);
		assertEquals("select distinct entity_0, subEntity_1 from Entity entity_0 inner join entity_0.subEntities subEntity_1", select.getQuery());
	}
	
	@Test
	public void testFunctionOnObjectWithoutConstructor()
	{
		Entity from = from(Entity.class);
		groupBy(from.getCode());
		Query<BigDecimal> select = select(sum(from.getBigDecimalField()));
		
		assertEquals("select sum(entity_0.bigDecimalField) from Entity entity_0 group by entity_0.code", select.getQuery()); 
		
	}

}
