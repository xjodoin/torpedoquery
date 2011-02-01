package com.netappsid.jpaquery;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class JPAQueryTest {
    private JPAQuery<Entity> query;
    
    @Before
    public void before() {
	this.query = new JPAQuery<Entity>(Entity.class);
    }
    
    @Test
    public void simpleQuery_NoSelector() {
	assertEquals("from Entity as entity", query.getQuery());
    }
    
    @Test
    public void simpleSelectionQuery_1Selector() {
	final Entity entity = query.getAlias();
	
	query.select(entity).getCode();
	assertEquals("select entity.code from Entity as entity", query.getQuery());
    }
    
    @Test
    public void simpleSelectionQuery_2Selectors() {
	final Entity entity = query.getAlias();
	
	query.select(entity).getCode();
	query.select(entity).getName();
	assertEquals("select entity.code, entity.name from Entity as entity", query.getQuery());
    }
    
    @Test
    public void simpleJoinQuery_1InnerJoin() {
	final Entity entity = query.getAlias();
	final SubEntity subEntity = query.getAlias(entity).getSubEntity();
	
	query.select(subEntity).getCode();
	assertEquals("select subEntity.code from Entity as entity inner join entity.subEntity as subEntity", query.getQuery());
    }
    
    @Test
    public void simpleJoinQuery_KeepsSelectionCallOrder() {
	final Entity entity = query.getAlias();
	final SubEntity subEntity = query.getAlias(entity).getSubEntity();
	
	query.select(entity).getName();
	query.select(subEntity).getCode();
	query.select(entity).getCode();
	assertEquals("select entity.name, subEntity.code, entity.code from Entity as entity inner join entity.subEntity as subEntity", query.getQuery());
    }
    
    public static class Entity {
	public String getCode() {
	    return null;
	}
	
	public String getName() {
	    return null;
	}
	
	public SubEntity getSubEntity() {
	    return null;
	}
    }
    
    public static class SubEntity {
	public String getCode() {
	    return null;
	}
	
	public String getName() {
	    return null;
	}
    }
}
