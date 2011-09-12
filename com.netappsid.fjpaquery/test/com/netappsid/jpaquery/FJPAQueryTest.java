package com.netappsid.jpaquery;

import static com.netappsid.jpaquery.FJPAQuery.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;

import com.netappsid.jpaquery.test.bo.Entity;
import com.netappsid.jpaquery.test.bo.ExtendEntity;
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
	public void test_leftJoin() {
		final Entity entity = from(Entity.class);

		leftJoin(entity.getSubEntity());
		assertEquals("from Entity entity_0 left join entity_0.subEntity subEntity_1", query(entity));
	}

	@Test
	public void test_rightJoin() {
		final Entity entity = from(Entity.class);

		rightJoin(entity.getSubEntity());
		assertEquals("from Entity entity_0 right join entity_0.subEntity subEntity_1", query(entity));
	}

	@Test
	public void test_innerJoin_withSelect() {
		final Entity entity = from(Entity.class);
		final SubEntity subEntity = innerJoin(entity.getSubEntity());

		select(entity.getCode(), subEntity.getName());
		assertEquals("select entity_0.code, subEntity_1.name from Entity entity_0 inner join entity_0.subEntity subEntity_1", query(entity));
	}

	@Test
	public void test_innerJoin_withList() {
		final Entity entity = from(Entity.class);
		final SubEntity subEntity = innerJoin(entity.getSubEntities());

		select(entity.getCode(), subEntity.getName());
		assertEquals("select entity_0.code, subEntity_1.name from Entity entity_0 inner join entity_0.subEntities subEntity_1", query(entity));
	}

	@Test
	public void test_simpleWhere() {
		final Entity entity = from(Entity.class);

		where(entity.getCode()).eq("test");

		assertEquals("from Entity entity_0 where entity_0.code = :code_1", query(entity));
		assertEquals("test", params(entity).get("code_1"));
	}

	@Test
	public void test_isNullWhere() {
		final Entity entity = from(Entity.class);
		where(entity.getCode()).isNull();

		assertEquals("from Entity entity_0 where entity_0.code is null", query(entity));
		assertTrue(params(entity).isEmpty());
	}

	@Test
	public void test_where_primitiveType() {
		final Entity entity = from(Entity.class);
		where(entity.isActive()).eq(true);

		assertEquals("from Entity entity_0 where entity_0.active = :active_1", query(entity));
		assertEquals(true, params(entity).get("active_1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_multipleWhereRestrictionsResultInConjunction() {
		final Entity entity = from(Entity.class);
		where(entity.isActive()).eq(true);
		where(entity.getCode()).isNull();
	}

	@Test
	public void test_singleResult() {
		final EntityManager entityManager = mock(EntityManager.class);
		final Query query = mock(Query.class);
		when(entityManager.createQuery(anyString())).thenReturn(query);

		final Entity entity = from(Entity.class);
		where(entity.getCode()).eq("test");
		com.netappsid.jpaquery.Query<Entity> select = select(entity);
		select.get(entityManager);

		verify(entityManager).createQuery(query(entity));
		verify(query).setParameter(params(entity).keySet().iterator().next(), params(entity).values().iterator().next());
		verify(query).getSingleResult();
	}

	@Test
	public void test_resultList() {
		final EntityManager entityManager = mock(EntityManager.class);
		final Query query = mock(Query.class);
		when(entityManager.createQuery(anyString())).thenReturn(query);

		final Entity entity = from(Entity.class);
		where(entity.getCode()).eq("test");
		com.netappsid.jpaquery.Query<Entity> select = select(entity);
		select.list(entityManager);

		verify(entityManager).createQuery(query(entity));
		verify(query).setParameter(params(entity).keySet().iterator().next(), params(entity).values().iterator().next());
		verify(query).getResultList();
	}

	@Test
	public void test_condition_only_on_join() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntity());

		where(innerJoin.getCode()).eq("test");
		assertEquals("from Entity entity_0 inner join entity_0.subEntity subEntity_1 where subEntity_1.code = :code_2", query(from));
	}

	@Test
	public void test_the_bo() {
		Entity from = from(Entity.class);
		select(from);
		SubEntity innerJoin = innerJoin(from.getSubEntity());
		where(innerJoin.getCode()).eq("test");
		assertEquals("select entity_0 from Entity entity_0 inner join entity_0.subEntity subEntity_1 where subEntity_1.code = :code_2", query(from));
	}

	@Test
	public void test_Join_Select_Come_Before_The_Root() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntities());
		com.netappsid.jpaquery.Query<String[]> select = select(innerJoin.getName(), from.getCode());
		String query = select.getQuery();
		assertEquals("select subEntity_1.name, entity_0.code from Entity entity_0 inner join entity_0.subEntities subEntity_1", query);
	}

	@Test
	public void test_parameters_must_not_be_empty_if_ask_before_string() {
		Entity from = from(Entity.class);
		where(from.getIntegerField()).eq(1);
		com.netappsid.jpaquery.Query<Entity> select = select(from);
		Map<String, Object> parameters = select.getParameters();
		assertEquals(1, parameters.get("integerField_1"));
	}

	@Test
	public void testJoinWith_with_Condition() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntities());
		with(innerJoin.getCode()).eq("test");
		com.netappsid.jpaquery.Query<SubEntity> select = select(innerJoin);
		String query = select.getQuery();
		Map<String, Object> parameters = select.getParameters();
		assertEquals("select subEntity_1 from Entity entity_0 inner join entity_0.subEntities subEntity_1 with subEntity_1.code = :code_2", query);
		assertEquals("test", parameters.get("code_2"));
	}

	@Test
	public void testExtend_specificSubClassField() {
		Entity from = from(Entity.class);
		ExtendEntity extend = extend(from, ExtendEntity.class);
		where(extend.getSpecificField()).eq("test");

		com.netappsid.jpaquery.Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.specificField = :specificField_1", select.getQuery());
		assertEquals("test", select.getParameters().get("specificField_1"));
	}

}
