package com.netappsid.jpaquery;

import static com.netappsid.jpaquery.FJPAQuery.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.netappsid.jpaquery.test.bo.Entity;
import com.netappsid.jpaquery.test.bo.SubEntity;

public class FJPAQueryTest {
    @Test
    public void test_createQuery() {
	final Entity entity = from(Entity.class);

	assertEquals("from Entity entity_0", query(entity));
    }

    @Test
    public void test_selectField() {
	final Entity entity = from(Entity.class);
	
	select(entity.getCode());
	assertEquals("select entity_0.code from Entity entity_0", query(entity));
    }

    @Test
    public void test_selectMultipleFields() {
	final Entity entity = from(Entity.class);

	select(entity.getCode(), entity.getName());
	assertEquals("select entity_0.code, entity_0.name from Entity entity_0", query(entity));
    }

    @Test
    public void test_selectMultipleFieldsFromDifferentEntities() {
	final Entity entity = from(Entity.class);
	final SubEntity subEntity = innerJoin(entity.getSubEntity());

	select(entity.getCode(), subEntity.getCode());
	assertEquals("select entity_0.code, subEntity_1.code from Entity entity_0 inner join entity_0.subEntity subEntity_1", query(entity));
    }
    
    @Test
    public void test_select_multipleFields_keepsOrder() {
	final Entity entity = from(Entity.class);
	final SubEntity subEntity = innerJoin(entity.getSubEntity());

	select(subEntity.getCode(), entity.getCode());
	assertEquals("select subEntity_1.code, entity_0.code from Entity entity_0 inner join entity_0.subEntity subEntity_1", query(entity));
    }

    @Test
    public void test_innerJoin() {
	final Entity entity = from(Entity.class);
	
	innerJoin(entity.getSubEntity());
	assertEquals("from Entity entity_0 inner join entity_0.subEntity subEntity_1", query(entity));
    }

    @Test
    public void test_innerJoin_withSelect() {
	final Entity entity = from(Entity.class);
	final SubEntity subEntity = innerJoin(entity.getSubEntity());
	
	select(entity.getCode(), subEntity.getName());
	assertEquals("select entity_0.code, subEntity_1.name from Entity entity_0 inner join entity_0.subEntity subEntity_1", query(entity));
    }

    @Test
    public void test_simpleWhere() {
	final Entity entity = from(Entity.class);
	
	where(entity.getCode()).eq("test");
	
	assertEquals("from Entity entity_0 where entity_0.code = :code_1", query(entity));
	assertEquals("test", params(entity).get("code_1"));
    }
    
    /**
     * where(entity.getCode()).neq("code");
     * where(entity.getCode()).lt("code);
     * where(entity.getCode()).lte("code);
     * where(entity.getCode()).gt("code);
     * where(entity.getCode()).gte("code);
     * where(entity.getCode()).like("code);
     * where(entity.getCode()).in("code);
     * where(entity.getCode()).notIn("code);
     * where(entity.getCode()).isNull();
     * where(entity.getCode()).isNotNull();
     * where(entity.getCode()).isEmpty();
     * where(entity.getCode()).isNotEmpty();
     * where(entity.getCode()).memberOf(Entity.class);
     * where(entity.getCode()).notMemberOf(Entity.class);
     * ===
     * select(coalesce(entity.getCode(), entity.getName()));
     * ===
     * select(parent.getChildren(0).getCode());
     * ===
     * from Entity entity
     *   where (entity.code = :code and entity.name = :name) or entity.code = :code2
     * 
     * final Grouping grouping = group(entity.getCode()).eq("code").and(entity.getName()).eq("name");
     * where(grouping).or(entity.getCode()).eq("code2");
     * 
     * OR
     * 
     * where(group(entity.getCode()).eq("code").and(entity.getName()).eq("name")).or(entity.getCode()).eq("code2");
     */
}
