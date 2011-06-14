package com.netappsid.jpaquery;

import org.junit.Test;

import com.netappsid.jpaquery.test.bo.Entity;

import static org.junit.Assert.*;
import static com.netappsid.jpaquery.FJPAQuery.*;


public class WhereClauseTest {

	
//	OnGoingLogicalOperation gt(T value);
//	
//	OnGoingLogicalOperation gte(T value);
//
//	OnGoingLogicalOperation isNull();

	
	@Test
	public void test_eq()
	{
		Entity from = from(Entity.class);
		where(from.getCode()).eq("test");
		
		assertEquals("from Entity entity_1 where entity_1.code = :code_0", query(from));
		
	}
	
	@Test
	public void test_neq()
	{
		Entity from = from(Entity.class);
		where(from.getCode()).neq("test");
		
		assertEquals("from Entity entity_1 where entity_1.code <> :code_0", query(from));
		
	}
	
	@Test
	public void test_lt()
	{
		Entity from = from(Entity.class);
		where(from.getCode()).lt("test");
		
		assertEquals("from Entity entity_1 where entity_1.code < :code_0", query(from));
		
	}
	
	@Test
	public void test_lte()
	{
		Entity from = from(Entity.class);
		where(from.getCode()).lte("test");
		
		assertEquals("from Entity entity_1 where entity_1.code <= :code_0", query(from));
		
	}
	
	@Test
	public void test_gt()
	{
		Entity from = from(Entity.class);
		where(from.getCode()).gt("test");
		
		assertEquals("from Entity entity_1 where entity_1.code > :code_0", query(from));
		
	}
	
	@Test
	public void test_gte()
	{
		Entity from = from(Entity.class);
		where(from.getCode()).gte("test");
		
		assertEquals("from Entity entity_1 where entity_1.code >= :code_0", query(from));
		
	}
	
	@Test
	public void test_isNull()
	{
		Entity from = from(Entity.class);
		where(from.getCode()).isNull();
		
		assertEquals("from Entity entity_0 where entity_0.code is null", query(from));
		
	}
	
}
