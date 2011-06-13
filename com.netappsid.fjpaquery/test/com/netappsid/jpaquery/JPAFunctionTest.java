package com.netappsid.jpaquery;

import static com.netappsid.jpaquery.FJPAQuery.count;
import static com.netappsid.jpaquery.FJPAQuery.from;
import static com.netappsid.jpaquery.FJPAQuery.query;
import static com.netappsid.jpaquery.FJPAQuery.select;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.netappsid.jpaquery.test.bo.Entity;

public class JPAFunctionTest {

	@Test
	public void testCountFunction_defaultCount() {
		Entity from = from(Entity.class);
		select(count(from));
		assertEquals("select count(*) from Entity entity_0", query(from));
	}

	@Test
	public void testCountFunction_withSpecifiedField() {
		Entity from = from(Entity.class);
		select(count(from.getCode()));
		assertEquals("select count(entity_0.code) from Entity entity_0", query(from));
	}

	@Test
	public void testCountFunction_withSpecifiedField_plusOneSelect() {
		Entity from = from(Entity.class);
		select(count(from.getCode()), from.getCode());
		assertEquals("select count(entity_0.code), entity_0.code from Entity entity_0", query(from));
	}

}
