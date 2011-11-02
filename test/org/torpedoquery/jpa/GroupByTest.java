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

import static org.torpedoquery.jpa.Torpedo.*;

import org.junit.Assert;
import org.junit.Test;
import org.torpedoquery.jpa.test.bo.Entity;
import org.torpedoquery.jpa.test.bo.SubEntity;

public class GroupByTest {

	@Test
	public void testSingleGroubBy() {
		Entity from = from(Entity.class);
		groupBy(from.getName());
		Query<Object[]> select = select(from.getName(), sum(from.getIntegerField()));
		String query = select.getQuery();

		Assert.assertEquals("select entity_0.name, sum(entity_0.integerField) from Entity entity_0 group by entity_0.name", query);
	}

	@Test
	public void testGroubBy_with_having_clause() {
		Entity from = from(Entity.class);
		groupBy(from.getName()).having(from.getName()).eq("test");
		Query<Object[]> select = select(from.getName(), sum(from.getIntegerField()));
		String query = select.getQuery();

		Assert.assertEquals("select entity_0.name, sum(entity_0.integerField) from Entity entity_0 group by entity_0.name having entity_0.name = :name_1",
				query);
		Assert.assertEquals("test", select.getParameters().get("name_1"));
	}

	@Test
	public void testGroubBy_with_having_with_function() {
		Entity from = from(Entity.class);
		SubEntity subEntity = innerJoin(from.getSubEntities());
		groupBy(from.getName()).having(sum(from.getIntegerField())).lt(sum(subEntity.getNumberField()));
		Query<String> select = select(from.getName());
		String query = select.getQuery();

		Assert.assertEquals(
				"select entity_0.name from Entity entity_0 inner join entity_0.subEntities subEntity_1 group by entity_0.name having sum(entity_0.integerField) < sum(subEntity_1.numberField)",
				query);
	}
}
