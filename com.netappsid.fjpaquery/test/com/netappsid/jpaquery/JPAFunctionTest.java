package com.netappsid.jpaquery;

import static com.netappsid.jpaquery.FJPAQuery.*;
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
	
	@Test
	public void testCountFunction_withSpecifiedField_plusOneSelect_inverse() {
		Entity from = from(Entity.class);
		Function count = count(from.getCode());
		select(from.getCode(),count);
		assertEquals("select entity_0.code, count(entity_0.code) from Entity entity_0", query(from));
	}
	
	@Test
	public void testSumFunction()
	{
		Entity from = from(Entity.class);
		select(sum(from.getIntegerField()));
		assertEquals("select sum(entity_0.integerField) from Entity entity_0", query(from));
	}
	
	@Test
	public void testMinFunction()
	{
		Entity from = from(Entity.class);
		select(min(from.getIntegerField()));
		assertEquals("select min(entity_0.integerField) from Entity entity_0", query(from));
	}
	
	@Test
	public void testMaxFunction()
	{
		Entity from = from(Entity.class);
		select(max(from.getIntegerField()));
		assertEquals("select max(entity_0.integerField) from Entity entity_0", query(from));
	}
	
	@Test
	public void testAvgFunction()
	{
		Entity from = from(Entity.class);
		select(avg(from.getIntegerField()));
		assertEquals("select avg(entity_0.integerField) from Entity entity_0", query(from));
	}
	

}
