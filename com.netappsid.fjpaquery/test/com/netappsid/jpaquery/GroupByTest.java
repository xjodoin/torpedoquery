package com.netappsid.jpaquery;

import static com.netappsid.jpaquery.FJPAQuery.*;

import org.junit.Assert;
import org.junit.Test;

import com.netappsid.jpaquery.test.bo.Entity;
import com.netappsid.jpaquery.test.bo.SubEntity;

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
