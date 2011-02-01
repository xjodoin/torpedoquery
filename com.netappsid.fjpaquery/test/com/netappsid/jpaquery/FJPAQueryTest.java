package com.netappsid.jpaquery;

import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;
import static com.netappsid.jpaquery.FJPAQuery.*;
import com.netappsid.jpaquery.test.bo.Entity;
import com.netappsid.jpaquery.test.bo.SubEntity;

public class FJPAQueryTest {

	@Test
	public void test_createQuery()
	{
		Entity entity = from(Entity.class);
		
		assertEquals("from Entity entity_0", query(entity));
		
	}
	
	@Test
	public void test_selectField()
	{
		Entity entity = from(Entity.class);
		select(entity.getCode());
		
		assertEquals("select entity_0.code from Entity entity_0", query(entity));
		
	}
	
	@Test
	public void test_innerJoin()
	{
		Entity entity = from(Entity.class);
		select(entity.getCode());
		SubEntity subEntity = innerJoin(entity.getSubEntity());
		
		assertEquals("select entity_0.code from Entity entity_0 inner join entity_0.subEntity subEntity_1", query(entity));
		
	}
	
	
	@Test
	public void test_innerJoin_withSelect()
	{
		Entity entity = from(Entity.class);
		SubEntity subEntity = innerJoin(entity.getSubEntity());
		select(entity.getCode());
		select(subEntity.getName());
		
		assertEquals("select entity_0.code , subEntity_1.name from Entity entity_0 inner join entity_0.subEntity subEntity_1", query(entity));
		
	}
	
	@Test
	public void test_simpleWhere()
	{
		Entity entity = from(Entity.class);
		where(entity.getCode()).eq("test");
		
		assertEquals("from Entity entity_0 where entity_0.code = :code_1 ", query(entity));
		Map<String, Object> params = params(entity);
		assertTrue(params.containsKey("code_1"));
		assertTrue(params.get("code_1").equals("test"));
	}

	
}
