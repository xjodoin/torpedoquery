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

import static org.torpedoquery.jpa.Torpedo.from;
import static org.torpedoquery.jpa.Torpedo.select;
import static org.torpedoquery.jpa.Torpedo.where;
import static org.torpedoquery.jpa.TorpedoFunction.constant;
import static org.torpedoquery.jpa.TorpedoFunction.operation;
import static org.torpedoquery.jpa.TorpedoFunction.sum;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.torpedoquery.jpa.test.bo.Entity;
public class ArithmeticOperatorTest {

	/**
	 * <p>testPlusOperator.</p>
	 */
	@Test
	public void testPlusOperator() {
		Entity from = from(Entity.class);
		Query<Integer> select = select(operation(from.getIntegerField()).plus(from.getPrimitiveInt()));
		Assert.assertEquals("select entity_0.integerField + entity_0.primitiveInt from Entity entity_0",
				select.getQuery());

	}

	/**
	 * <p>testPlusOperatorWithLeftFunction.</p>
	 */
	@Test
	public void testPlusOperatorWithLeftFunction() {
		// I know it's suppose to be with group by...
		Entity from = from(Entity.class);
		Query<Integer> select = select(operation(sum(from.getIntegerField())).plus(from.getPrimitiveInt()));
		Assert.assertEquals("select sum(entity_0.integerField) + entity_0.primitiveInt from Entity entity_0",
				select.getQuery());

	}

	/**
	 * <p>testPlusOperatorWithRightFunction.</p>
	 */
	@Test
	public void testPlusOperatorWithRightFunction() {
		// I know it's suppose to be with group by...
		Entity from = from(Entity.class);
		Query<Integer> select = select(operation(from.getIntegerField()).plus(sum(from.getPrimitiveInt())));
		Assert.assertEquals("select entity_0.integerField + sum(entity_0.primitiveInt) from Entity entity_0",
				select.getQuery());

	}

	/**
	 * <p>testBetweenBug.</p>
	 */
	@Test
	public void testBetweenBug() {
		Entity from = from(Entity.class);
		where(operation(from.getBigDecimalField()).subtract(from.getBigDecimalField2()))
				.between(constant(BigDecimal.ZERO), constant(BigDecimal.valueOf(Double.MAX_VALUE)));

		org.torpedoquery.jpa.Query<BigDecimal> select = select(
				sum(operation(from.getBigDecimalField()).subtract(from.getBigDecimalField2())));

		Assert.assertEquals(
				"select sum(entity_0.bigDecimalField - entity_0.bigDecimalField2) from Entity entity_0 where entity_0.bigDecimalField - entity_0.bigDecimalField2 between 0 and 1.7976931348623157E+308",
				select.getQuery());
	}

	/**
	 * <p>testSumInOperationAndWhereClause.</p>
	 */
	@Test
	public void testSumInOperationAndWhereClause() {
		Entity from = from(Entity.class);
		where(operation(from.getBigDecimalField()).subtract(from.getBigDecimalField2())).gt(constant(BigDecimal.ZERO));

		org.torpedoquery.jpa.Query<BigDecimal> select = select(
				sum(operation(from.getBigDecimalField()).subtract(from.getBigDecimalField2())));
		Assert.assertEquals(
				"select sum(entity_0.bigDecimalField - entity_0.bigDecimalField2) from Entity entity_0 where entity_0.bigDecimalField - entity_0.bigDecimalField2 > 0",
				select.getQuery());

	}

	/**
	 * <p>testTestSubstractInSubquery.</p>
	 */
	@Test
	public void testTestSubstractInSubquery() {
		final Entity fromEntity = from(Entity.class);
		final Entity fromExistingEntity = from(Entity.class);
		where(fromExistingEntity.getId()).eq("testid");
		where(fromEntity.getIntegerField())
				.eq(select(operation(fromExistingEntity.getIntegerField()).subtract(constant(1))));
		Query<Entity> select = select(fromEntity);

		Assert.assertEquals(
				"select entity_0 from Entity entity_0 where entity_0.integerField = ( select entity_1.integerField - 1 from Entity entity_1 where entity_1.id = :id_2 )",
				select.getQuery());

	}

}
