/**
 * Copyright (C) 2011 Xavier Jodoin (xavier@jodoin.me)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.torpedoquery.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.torpedoquery.jpa.Torpedo.*;
import static org.torpedoquery.jpa.Torpedo.extend;
import static org.torpedoquery.jpa.Torpedo.from;
import static org.torpedoquery.jpa.Torpedo.innerJoin;
import static org.torpedoquery.jpa.Torpedo.leftJoin;
import static org.torpedoquery.jpa.Torpedo.rightJoin;
import static org.torpedoquery.jpa.Torpedo.select;
import static org.torpedoquery.jpa.Torpedo.where;
import static org.torpedoquery.jpa.Torpedo.with;

import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;
import org.torpedoquery.jpa.test.bo.Entity;
import org.torpedoquery.jpa.test.bo.ExtendEntity;
import org.torpedoquery.jpa.test.bo.SubEntity;

public class TorpedoTest {
	/**
	 * <p>
	 * test_createQuery.
	 * </p>
	 */
	@Test
	public void test_createQuery() {
		final Entity entity = from(Entity.class);
		org.torpedoquery.jpa.Query<Entity> select = select(entity);
		assertEquals("select entity_0 from Entity entity_0", select.getQuery());
	}

	/**
	 * <p>
	 * test_createQuery.
	 * </p>
	 */
	@Test
	public void test_createFreezeQuery() {
		final Entity entity = from(Entity.class);
		org.torpedoquery.jpa.Query<Entity> select = select(entity).freeze();
		org.torpedoquery.jpa.Query<String> select2 = select(entity.getName());
		assertEquals("select entity_0 from Entity entity_0", select.getQuery());
		assertEquals("select entity_0.name from Entity entity_0", select2.getQuery());
	}

	@Test
	public void test_createQueryTwiceWithDifferentCondition() {
		final Entity entity = from(Entity.class);
		org.torpedoquery.jpa.Query<Entity> select = select(entity).freeze();
		where(entity.getIntegerField()).eq(10);
		org.torpedoquery.jpa.Query<String> select2 = select(entity.getName());
		assertEquals("select entity_0 from Entity entity_0", select.getQuery());
		assertEquals("select entity_0.name from Entity entity_0 where entity_0.integerField = :integerField_1",
				select2.getQuery());
		assertNull(select.getParameters().get("integerField_1"));
		assertEquals(10, select2.getParameters().get("integerField_1"));
	}
	
	@Test
	public void test_createFeeezeWithPrimitive() {
		final Entity entity = from(Entity.class);
		where(entity.getPrimitiveInt()).eq(10);
		org.torpedoquery.jpa.Query<Entity> select = select(entity).freeze();
		
		assertEquals("select entity_0 from Entity entity_0 where entity_0.primitiveInt = :primitiveInt_1",
				select.getQuery());
		assertEquals(10, select.getParameters().get("primitiveInt_1"));
	}
	
	@Test
	public void test_createFeeezeWithPrimitiveLong() {
		final Entity entity = from(Entity.class);
		where(entity.getPrimitiveLong()).eq(10L);
		org.torpedoquery.jpa.Query<Entity> select = select(entity).freeze();
		
		assertEquals("select entity_0 from Entity entity_0 where entity_0.primitiveLong = :primitiveLong_1",
				select.getQuery());
		assertEquals(10L, select.getParameters().get("primitiveLong_1"));
	}
	
	@Test
	public void test_createFeeezeWithCountFunction() {
		final Entity entity = from(Entity.class);
		where(entity.getPrimitiveInt()).eq(10);
		org.torpedoquery.jpa.Query<Long> select = select(count(entity)).freeze();
		
		assertEquals("select count(entity_0) from Entity entity_0 where entity_0.primitiveInt = :primitiveInt_1",
				select.getQuery());
		assertEquals(10, select.getParameters().get("primitiveInt_1"));
	}
	
	@Test
	public void test_createFeeezeAndCondition() {
		final Entity entity = from(Entity.class);
		where(entity.getPrimitiveInt()).eq(10).and(entity.getIntegerField()).eq(20);
		org.torpedoquery.jpa.Query<Long> select = select(count(entity)).freeze();
		
		assertEquals("select count(entity_0) from Entity entity_0 where entity_0.primitiveInt = :primitiveInt_1 and entity_0.integerField = :integerField_2",
				select.getQuery());
		assertEquals(10, select.getParameters().get("primitiveInt_1"));
	}

	/**
	 * <p>
	 * test_selectField.
	 * </p>
	 */
	@Test
	public void test_selectField() {
		final Entity entity = from(Entity.class);
		org.torpedoquery.jpa.Query<String> select = select(entity.getCode());
		assertEquals("select entity_0.code from Entity entity_0", select.getQuery());
	}

	/**
	 * <p>
	 * test_selectMultipleFields.
	 * </p>
	 */
	@Test
	public void test_selectMultipleFields() {
		final Entity entity = from(Entity.class);

		org.torpedoquery.jpa.Query<Object[]> select = select(entity.getCode(), entity.getName());
		assertEquals("select entity_0.code, entity_0.name from Entity entity_0", select.getQuery());
	}

	/**
	 * <p>
	 * test_selectMultipleFieldsFromDifferentEntities.
	 * </p>
	 */
	@Test
	public void test_selectMultipleFieldsFromDifferentEntities() {
		final Entity entity = from(Entity.class);
		final SubEntity subEntity = innerJoin(entity.getSubEntity());

		org.torpedoquery.jpa.Query<Object[]> select = select(entity.getCode(), subEntity.getCode());
		assertEquals(
				"select entity_0.code, subEntity_1.code from Entity entity_0 inner join entity_0.subEntity subEntity_1",
				select.getQuery());
	}

	/**
	 * <p>
	 * test_select_multipleFields_keepsOrder.
	 * </p>
	 */
	@Test
	public void test_select_multipleFields_keepsOrder() {
		final Entity entity = from(Entity.class);
		final SubEntity subEntity = innerJoin(entity.getSubEntity());

		org.torpedoquery.jpa.Query<Object[]> select = select(subEntity.getCode(), entity.getCode());
		assertEquals(
				"select subEntity_1.code, entity_0.code from Entity entity_0 inner join entity_0.subEntity subEntity_1",
				select.getQuery());
	}

	/**
	 * <p>
	 * test_innerJoin.
	 * </p>
	 */
	@Test
	public void test_innerJoin() {
		final Entity entity = from(Entity.class);

		innerJoin(entity.getSubEntity());
		org.torpedoquery.jpa.Query<Entity> select = select(entity);

		assertEquals("select entity_0 from Entity entity_0 inner join entity_0.subEntity subEntity_1",
				select.getQuery());
	}

	/**
	 * <p>
	 * test_leftJoin.
	 * </p>
	 */
	@Test
	public void test_leftJoin() {
		final Entity entity = from(Entity.class);

		leftJoin(entity.getSubEntity());
		org.torpedoquery.jpa.Query<Entity> select = select(entity);

		assertEquals("select entity_0 from Entity entity_0 left join entity_0.subEntity subEntity_1",
				select.getQuery());
	}

	/**
	 * <p>
	 * test_rightJoin.
	 * </p>
	 */
	@Test
	public void test_rightJoin() {
		final Entity entity = from(Entity.class);

		rightJoin(entity.getSubEntity());
		org.torpedoquery.jpa.Query<Entity> select = select(entity);

		assertEquals("select entity_0 from Entity entity_0 right join entity_0.subEntity subEntity_1",
				select.getQuery());
	}

	/**
	 * <p>
	 * test_innerJoin_withSelect.
	 * </p>
	 */
	@Test
	public void test_innerJoin_withSelect() {
		final Entity entity = from(Entity.class);
		final SubEntity subEntity = innerJoin(entity.getSubEntity());

		org.torpedoquery.jpa.Query<Object[]> select = select(entity.getCode(), subEntity.getName());

		assertEquals(
				"select entity_0.code, subEntity_1.name from Entity entity_0 inner join entity_0.subEntity subEntity_1",
				select.getQuery());
	}

	/**
	 * <p>
	 * test_innerJoin_withList.
	 * </p>
	 */
	@Test
	public void test_innerJoin_withList() {
		final Entity entity = from(Entity.class);
		final SubEntity subEntity = innerJoin(entity.getSubEntities());

		org.torpedoquery.jpa.Query<Object[]> select = select(entity.getCode(), subEntity.getName());
		assertEquals(
				"select entity_0.code, subEntity_1.name from Entity entity_0 inner join entity_0.subEntities subEntity_1",
				select.getQuery());
	}

	/**
	 * <p>
	 * test_simpleWhere.
	 * </p>
	 */
	@Test
	public void test_simpleWhere() {
		final Entity entity = from(Entity.class);

		where(entity.getCode()).eq("test");

		org.torpedoquery.jpa.Query<Entity> select = select(entity);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.code = :code_1", select.getQuery());
		assertEquals("test", select.getParameters().get("code_1"));
	}

	/**
	 * <p>
	 * test_isNullWhere.
	 * </p>
	 */
	@Test
	public void test_isNullWhere() {
		final Entity entity = from(Entity.class);
		where(entity.getCode()).isNull();
		org.torpedoquery.jpa.Query<Entity> select = select(entity);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.code is null", select.getQuery());
		assertTrue(select.getParameters().isEmpty());
	}

	/**
	 * <p>
	 * test_where_primitiveType.
	 * </p>
	 */
	@Test
	public void test_where_primitiveType() {
		final Entity entity = from(Entity.class);
		where(entity.isActive()).eq(true);
		org.torpedoquery.jpa.Query<Entity> select = select(entity);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.active = :active_1", select.getQuery());
		assertEquals(true, select.getParameters().get("active_1"));
	}

	/**
	 * <p>
	 * test_multipleWhereRestrictionsResultInConjunction.
	 * </p>
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_multipleWhereRestrictionsResultInConjunction() {
		final Entity entity = from(Entity.class);
		where(entity.isActive()).eq(true);
		where(entity.getCode()).isNull();
	}

	/**
	 * <p>
	 * test_singleResult.
	 * </p>
	 */
	@Test
	public void test_singleResult() {
		final EntityManager entityManager = mock(EntityManager.class);
		final Query query = mock(Query.class);
		when(entityManager.createQuery(anyString())).thenReturn(query);

		final Entity entity = from(Entity.class);
		where(entity.getCode()).eq("test");
		org.torpedoquery.jpa.Query<Entity> select = select(entity);
		select.get(entityManager);

		verify(entityManager).createQuery(select.getQuery());
		Entry<String, Object> next = select.getParameters().entrySet().iterator().next();
		verify(query).setParameter(next.getKey(), next.getValue());
		verify(query).getSingleResult();
	}

	/**
	 * <p>
	 * test_resultList.
	 * </p>
	 */
	@Test
	public void test_resultList() {
		final EntityManager entityManager = mock(EntityManager.class);
		final Query query = mock(Query.class);
		when(entityManager.createQuery(anyString())).thenReturn(query);

		final Entity entity = from(Entity.class);
		where(entity.getCode()).eq("test");
		org.torpedoquery.jpa.Query<Entity> select = select(entity);
		select.list(entityManager);

		verify(entityManager).createQuery(select.getQuery());
		Entry<String, Object> next = select.getParameters().entrySet().iterator().next();
		verify(query).setParameter(next.getKey(), next.getValue());
		verify(query).getResultList();
	}

	/**
	 * <p>
	 * test_condition_only_on_join.
	 * </p>
	 */
	@Test
	public void test_condition_only_on_join() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntity());

		where(innerJoin.getCode()).eq("test");
		org.torpedoquery.jpa.Query<Entity> select = select(from);
		assertEquals(
				"select entity_0 from Entity entity_0 inner join entity_0.subEntity subEntity_1 where subEntity_1.code = :code_2",
				select.getQuery());
	}

	/**
	 * <p>
	 * test_the_bo.
	 * </p>
	 */
	@Test
	public void test_the_bo() {
		Entity from = from(Entity.class);
		org.torpedoquery.jpa.Query<Entity> select = select(from);
		SubEntity innerJoin = innerJoin(from.getSubEntity());
		where(innerJoin.getCode()).eq("test");
		assertEquals(
				"select entity_0 from Entity entity_0 inner join entity_0.subEntity subEntity_1 where subEntity_1.code = :code_2",
				select.getQuery());
	}

	/**
	 * <p>
	 * test_Join_Select_Come_Before_The_Root.
	 * </p>
	 */
	@Test
	public void test_Join_Select_Come_Before_The_Root() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntities());
		org.torpedoquery.jpa.Query<Object[]> select = select(innerJoin.getName(), from.getCode());
		String query = select.getQuery();
		assertEquals(
				"select subEntity_1.name, entity_0.code from Entity entity_0 inner join entity_0.subEntities subEntity_1",
				query);
	}

	/**
	 * <p>
	 * test_parameters_must_not_be_empty_if_ask_before_string.
	 * </p>
	 */
	@Test
	public void test_parameters_must_not_be_empty_if_ask_before_string() {
		Entity from = from(Entity.class);
		where(from.getIntegerField()).eq(1);
		org.torpedoquery.jpa.Query<Entity> select = select(from);
		Map<String, Object> parameters = select.getParameters();
		assertEquals(1, parameters.get("integerField_1"));
	}

	/**
	 * <p>
	 * testJoinWith_with_Condition.
	 * </p>
	 */
	@Test
	public void testJoinWith_with_Condition() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntities());
		with(innerJoin.getCode()).eq("test");
		org.torpedoquery.jpa.Query<SubEntity> select = select(innerJoin);
		String query = select.getQuery();
		Map<String, Object> parameters = select.getParameters();
		assertEquals(
				"select subEntity_1 from Entity entity_0 inner join entity_0.subEntities subEntity_1 with subEntity_1.code = :code_2",
				query);
		assertEquals("test", parameters.get("code_2"));
	}

	/**
	 * <p>
	 * testJoinWith_with_ConditionGroupping.
	 * </p>
	 */
	@Test
	public void testJoinWith_with_ConditionGroupping() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntities());
		OnGoingLogicalCondition withCondition = condition(innerJoin.getCode()).eq("test").or(innerJoin.getCode())
				.eq("test2");
		with(withCondition);
		org.torpedoquery.jpa.Query<SubEntity> select = select(innerJoin);
		String query = select.getQuery();
		assertEquals(
				"select subEntity_1 from Entity entity_0 inner join entity_0.subEntities subEntity_1 with ( subEntity_1.code = :code_2 or subEntity_1.code = :code_3 )",
				query);
	}

	/**
	 * <p>
	 * testExtend_specificSubClassField.
	 * </p>
	 */
	@Test
	public void testExtend_specificSubClassField() {
		Entity from = from(Entity.class);
		ExtendEntity extend = extend(from, ExtendEntity.class);
		where(extend.getSpecificField()).eq("test");

		org.torpedoquery.jpa.Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.specificField = :specificField_1",
				select.getQuery());
		assertEquals("test", select.getParameters().get("specificField_1"));
	}

	/**
	 * <p>
	 * testSelectWithChainedMethodCall.
	 * </p>
	 */
	@Test
	public void testSelectWithChainedMethodCall() {
		Entity from = from(Entity.class);
		org.torpedoquery.jpa.Query<String> select = select(from.getSubEntity().getCode());
		assertEquals("select entity_0.subEntity.code from Entity entity_0", select.getQuery());
	}

	/**
	 * <p>
	 * testWhereWithChainedMethodCall.
	 * </p>
	 */
	@Test
	public void testWhereWithChainedMethodCall() {
		Entity from = from(Entity.class);
		where(from.getSubEntity().getCode()).eq("test");
		org.torpedoquery.jpa.Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 where entity_0.subEntity.code = :code_1", select.getQuery());
		assertEquals("test", select.getParameters().get("code_1"));
	}

	/**
	 * <p>
	 * testJoinOnMap.
	 * </p>
	 */
	@Test
	public void testJoinOnMap() {
		Entity from = from(Entity.class);
		SubEntity innerJoin = innerJoin(from.getSubEntityMap());
		String query = select(innerJoin).getQuery();
		assertEquals("select subEntity_1 from Entity entity_0 inner join entity_0.subEntityMap subEntity_1", query);
	}

	/**
	 * <p>
	 * test_innerJoinOnOverrideMethod.
	 * </p>
	 */
	@Test
	public void test_innerJoinOnOverrideMethod() {
		ExtendEntity extendEntity = from(ExtendEntity.class);
		SubEntity innerJoin = innerJoin(extendEntity.getSubEntity());
		org.torpedoquery.jpa.Query<SubEntity> select = select(innerJoin);
		String query = select.getQuery();
		assertEquals(
				"select subEntity_1 from ExtendEntity extendEntity_0 inner join extendEntity_0.subEntity subEntity_1",
				query);
	}

	/**
	 * <p>
	 * testTwoJoinOnSameProperty.
	 * </p>
	 */
	@Test
	public void testTwoJoinOnSameProperty() {
		Entity from = from(Entity.class);
		innerJoin(from.getSubEntity());
		leftJoin(from.getSubEntity());

		org.torpedoquery.jpa.Query<Entity> select = select(from);
		assertEquals(
				"select entity_0 from Entity entity_0 inner join entity_0.subEntity subEntity_1 left join entity_0.subEntity subEntity_2",
				select.getQuery());
	}
}
