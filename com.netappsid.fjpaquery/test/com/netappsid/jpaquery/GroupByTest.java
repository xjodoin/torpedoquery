package com.netappsid.jpaquery;

import static com.netappsid.jpaquery.FJPAQuery.*;

import org.junit.Assert;
import org.junit.Test;

import com.netappsid.jpaquery.test.bo.Entity;

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
}
