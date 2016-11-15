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
import static org.torpedoquery.jpa.Torpedo.condition;
import static org.torpedoquery.jpa.Torpedo.from;
import static org.torpedoquery.jpa.Torpedo.innerJoin;
import static org.torpedoquery.jpa.Torpedo.select;
import static org.torpedoquery.jpa.Torpedo.where;
import static org.torpedoquery.jpa.TorpedoFunction.avg;
import static org.torpedoquery.jpa.TorpedoFunction.max;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.torpedoquery.jpa.test.bo.Entity;
import org.torpedoquery.jpa.test.bo.SubEntity;

public class WhereClauseTest {

	/**
	 * <p>test_eq.</p>
	 */
	@Test
	public void test_eq() {
		Entity from = from(Entity.class);
		where(from.getCode()).eq("test");
		Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 where entity_0.code = :code_1", select.getQuery());

	}

	/**
	 * <p>test_neq.</p>
	 */
	@Test
	public void test_neq() {
		Entity from = from(Entity.class);
		where(from.getCode()).neq("test");
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.code <> :code_1", select.getQuery());

	}

	/**
	 * <p>test_lt.</p>
	 */
	@Test
	public void test_lt() {
		Entity from = from(Entity.class);
		where(from.getIntegerField()).lt(2);
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.integerField < :integerField_1",
				select.getQuery());

	}

	/**
	 * <p>test_lte.</p>
	 */
	@Test
	public void test_lte() {
		Entity from = from(Entity.class);
		where(from.getIntegerField()).lte(2);
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.integerField <= :integerField_1",
				select.getQuery());

	}

	/**
	 * <p>test_gt.</p>
	 */
	@Test
	public void test_gt() {
		Entity from = from(Entity.class);
		where(from.getIntegerField()).gt(2);
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.integerField > :integerField_1",
				select.getQuery());

	}

	/**
	 * <p>test_gte.</p>
	 */
	@Test
	public void test_gte() {
		Entity from = from(Entity.class);
		where(from.getIntegerField()).gte(2);
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.integerField >= :integerField_1",
				select.getQuery());

	}

	/**
	 * <p>test_gte_primitive.</p>
	 */
	@Test
	public void test_gte_primitive() {
		Entity from = from(Entity.class);
		where(from.getPrimitiveInt()).gte(2);
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.primitiveInt >= :primitiveInt_1",
				select.getQuery());

	}

	/**
	 * <p>test_isNull.</p>
	 */
	@Test
	public void test_isNull() {
		Entity from = from(Entity.class);
		where(from.getCode()).isNull();
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.code is null", select.getQuery());

	}

	/**
	 * <p>test_isNotNull.</p>
	 */
	@Test
	public void test_isNotNull() {
		Entity from = from(Entity.class);
		where(from.getCode()).isNotNull();
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.code is not null", select.getQuery());

	}

	/**
	 * <p>test_in_values.</p>
	 */
	@Test
	public void test_in_values() {
		Entity from = from(Entity.class);
		where(from.getPrimitiveInt()).in(3, 4);
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.primitiveInt in ( :primitiveInt_1 )",
				select.getQuery());

	}

	/**
	 * <p>test_in_subSelect.</p>
	 */
	@Test
	public void test_in_subSelect() {
		Entity subSelect = from(Entity.class);

		Entity from = from(Entity.class);
		where(from.getCode()).in(select(subSelect.getCode()));
		Query<Entity> select = select(from);

		assertEquals(
				"select entity_0 from Entity entity_0 where entity_0.code in ( select entity_1.code from Entity entity_1 )",
				select.getQuery());

	}

	/**
	 * <p>test_notIn_values.</p>
	 */
	@Test
	public void test_notIn_values() {
		Entity from = from(Entity.class);
		where(from.getPrimitiveInt()).notIn(3, 4);
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.primitiveInt not in ( :primitiveInt_1 )",
				select.getQuery());

	}

	/**
	 * <p>test_notIn_subSelect.</p>
	 */
	@Test
	public void test_notIn_subSelect() {
		Entity subSelect = from(Entity.class);

		Entity from = from(Entity.class);
		where(from.getCode()).notIn(select(subSelect.getCode()));
		Query<Entity> select = select(from);

		assertEquals(
				"select entity_0 from Entity entity_0 where entity_0.code not in ( select entity_1.code from Entity entity_1 )",
				select.getQuery());

	}

	/**
	 * <p>test_in_subSelect_with_params.</p>
	 */
	@Test
	public void test_in_subSelect_with_params() {
		Entity subSelect = from(Entity.class);
		where(subSelect.getCode()).eq("subquery");
		Entity from = from(Entity.class);
		where(from.getCode()).in(select(subSelect.getCode()));
		Query<Entity> select = select(from);
		assertEquals(
				"select entity_0 from Entity entity_0 where entity_0.code in ( select entity_1.code from Entity entity_1 where entity_1.code = :code_2 )",
				select.getQuery());
		Map<String, Object> params = select.getParameters();
		assertEquals(1, params.size());
		assertEquals("subquery", params.get("code_2"));

	}

	/**
	 * <p>test_subSelect_with_gt.</p>
	 */
	@Test
	public void test_subSelect_with_gt() {
		Entity subSelect = from(Entity.class);

		Entity from = from(Entity.class);
		where(from.getIntegerField()).gt(select((avg(subSelect.getIntegerField()))));
		Query<Entity> select = select(from);

		assertEquals(
				"select entity_0 from Entity entity_0 where entity_0.integerField > ( select avg(entity_1.integerField) from Entity entity_1 )",
				select.getQuery());

	}

	/**
	 * <p>test_subSelect_with_gt_and_params.</p>
	 */
	@Test
	public void test_subSelect_with_gt_and_params() {
		Entity subSelect = from(Entity.class);
		where(subSelect.getCode()).eq("toto");

		Entity from = from(Entity.class);
		where(from.getIntegerField()).gt(select(avg(subSelect.getIntegerField())));
		Query<Entity> select = select(from);

		assertEquals(
				"select entity_0 from Entity entity_0 where entity_0.integerField > ( select avg(entity_1.integerField) from Entity entity_1 where entity_1.code = :code_2 )",
				select.getQuery());
		assertEquals("toto", select.getParameters().get("code_2"));

	}

	/**
	 * <p>test_subSelect_in_select_clause.</p>
	 */
	@Test
	public void test_subSelect_in_select_clause() {
		Entity subSelect = from(Entity.class);

		Entity from = from(Entity.class);
		Query<Integer> select2 = select(max(subSelect.getIntegerField()));
		Query<Object[]> select = select(from.getName(), select2);

		assertEquals(
				"select entity_0.name, ( select max(entity_1.integerField) from Entity entity_1 ) from Entity entity_0",
				select.getQuery());

	}

	/**
	 * <p>test_subSelect_with_comparable_where_clause.</p>
	 */
	@Test
	public void test_subSelect_with_comparable_where_clause() {
		Entity subSelect = from(Entity.class);

		Entity from = from(Entity.class);
		where(from.getIntegerField()).gt(select(avg(subSelect.getIntegerField())));
		Query<String> select = select(from.getName());

		assertEquals(
				"select entity_0.name from Entity entity_0 where entity_0.integerField > ( select avg(entity_1.integerField) from Entity entity_1 )",
				select.getQuery());

	}

	/**
	 * <p>test_And_WhereClause.</p>
	 */
	@Test
	public void test_And_WhereClause() {
		Entity from = from(Entity.class);
		where(from.getName()).eq("test").and(from.getPrimitiveInt()).gt(10);
		Query<Entity> select = select(from);

		assertEquals(
				"select entity_0 from Entity entity_0 where entity_0.name = :name_1 and entity_0.primitiveInt > :primitiveInt_2",
				select.getQuery());
	}

	/**
	 * <p>test_Or_WhereClause.</p>
	 */
	@Test
	public void test_Or_WhereClause() {
		Entity from = from(Entity.class);
		where(from.getName()).eq("test").or(from.getPrimitiveInt()).gt(10);
		Query<Entity> select = select(from);

		assertEquals(
				"select entity_0 from Entity entity_0 where entity_0.name = :name_1 or entity_0.primitiveInt > :primitiveInt_2",
				select.getQuery());
	}

	/**
	 * <p>test_like_any.</p>
	 */
	@Test
	public void test_like_any() {
		Entity from = from(Entity.class);
		where(from.getCode()).like().any("test");
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.code like '%test%'", select.getQuery());
	}
	
	/**
	 * <p>test_like_any.</p>
	 */
	@Test
	public void test_notLike_any() {
		Entity from = from(Entity.class);
		where(from.getCode()).notLike().any("test");
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.code not like '%test%'", select.getQuery());
	}

	/**
	 * <p>test_like_startsWith.</p>
	 */
	@Test
	public void test_like_startsWith() {
		Entity from = from(Entity.class);
		where(from.getCode()).like().startsWith("test");
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.code like 'test%'", select.getQuery());
	}
	
	@Test
	public void test_notLike_startsWith() {
		Entity from = from(Entity.class);
		where(from.getCode()).notLike().startsWith("test");
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.code not like 'test%'", select.getQuery());
	}

	/**
	 * <p>test_like_endsWith.</p>
	 */
	@Test
	public void test_like_endsWith() {
		Entity from = from(Entity.class);
		where(from.getCode()).like().endsWith("test");
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.code like '%test'", select.getQuery());
	}
	
	/**
	 * <p>test_like_endsWith.</p>
	 */
	@Test
	public void test_notLike_endsWith() {
		Entity from = from(Entity.class);
		where(from.getCode()).notLike().endsWith("test");
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.code not like '%test'", select.getQuery());
	}

	/**
	 * <p>test_is_empty.</p>
	 */
	@Test
	public void test_is_empty() {
		Entity from = from(Entity.class);
		where(from.getSubEntities()).isEmpty();
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.subEntities is empty", select.getQuery());
	}

	/**
	 * <p>test_is_not_empty.</p>
	 */
	@Test
	public void test_is_not_empty() {
		Entity from = from(Entity.class);
		where(from.getSubEntities()).isNotEmpty();
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.subEntities is not empty", select.getQuery());
	}

	/**
	 * <p>test_size.</p>
	 */
	@Test
	public void test_size() {
		Entity from = from(Entity.class);
		where(from.getSubEntities()).size().gt(2);
		Query<Entity> select = select(from);

		assertEquals("select entity_0 from Entity entity_0 where entity_0.subEntities.size > :subEntities_1",
				select.getQuery());
	}

	/**
	 * <p>test_member_of.</p>
	 */
	@Test
	public void test_member_of() {
		Entity fromEntity = from(Entity.class);
		where(fromEntity.getValueCollection()).memberOf("VALUE");
		Query<Entity> query = select(fromEntity);

		assertEquals("select entity_0 from Entity entity_0 where :valueCollection_1 member of entity_0.valueCollection", query.getQuery());
	}

	/**
	 * <p>test_where_with_condition_and.</p>
	 */
	@Test
	public void test_where_with_condition_and() {
		Entity from = from(Entity.class);
		OnGoingLogicalCondition condition = condition(from.getCode()).eq("test").or(from.getCode()).eq("test2");
		where(from.getName()).eq("test").and(condition);
		Query<Entity> select = select(from);

		assertEquals(
				"select entity_0 from Entity entity_0 where entity_0.name = :name_1 and ( entity_0.code = :code_2 or entity_0.code = :code_3 )",
				select.getQuery());
	}

	/**
	 * <p>test_where_with_condition_or.</p>
	 */
	@Test
	public void test_where_with_condition_or() {
		Entity from = from(Entity.class);
		OnGoingLogicalCondition condition = condition(from.getCode()).eq("test").or(from.getCode()).eq("test2");
		where(from.getName()).eq("test").or(condition);
		Query<Entity> select = select(from);

		assertEquals(
				"select entity_0 from Entity entity_0 where entity_0.name = :name_1 or ( entity_0.code = :code_2 or entity_0.code = :code_3 )",
				select.getQuery());
	}

	/**
	 * <p>test_where_with_condition_or_inline.</p>
	 */
	@Test
	public void test_where_with_condition_or_inline() {
		Entity from = from(Entity.class);
		where(from.getName()).eq("test").or(condition(from.getCode()).eq("test").or(from.getCode()).eq("test2"));
		Query<Entity> select = select(from);

		assertEquals(
				"select entity_0 from Entity entity_0 where entity_0.name = :name_1 or ( entity_0.code = :code_2 or entity_0.code = :code_3 )",
				select.getQuery());
	}

	/**
	 * <p>test_condition_with_root_and_inner.</p>
	 */
	@Test
	public void test_condition_with_root_and_inner() {

		Entity entity = from(Entity.class);
		SubEntity subEntity = innerJoin(entity.getSubEntities());
		OnGoingLogicalCondition condition = condition(entity.getCode()).eq("test1").or(subEntity.getCode()).eq("test2");
		where(entity.getIntegerField()).gt(10).and(condition);

		Query<Entity> select = select(entity);
		String query = select.getQuery();
		assertEquals(
				"select entity_0 from Entity entity_0 inner join entity_0.subEntities subEntity_1 where entity_0.integerField > :integerField_2 and ( entity_0.code = :code_3 or subEntity_1.code = :code_4 )",
				query);
	}

	/**
	 * <p>test_acceptConditionIntoCondition.</p>
	 */
	@Test
	public void test_acceptConditionIntoCondition() {
		Entity from = from(Entity.class);
		OnGoingLogicalCondition onGoingLogicalCondition = condition(from.getName()).eq("test").or(from.getName())
				.eq("test2");
		OnGoingLogicalCondition eq = condition(onGoingLogicalCondition).and(from.getCode()).eq("mycode");
		where(eq);
		Query<Entity> select = select(from);
		String query = select.getQuery();
		assertEquals(
				"select entity_0 from Entity entity_0 where ( ( entity_0.name = :name_1 or entity_0.name = :name_2 ) and entity_0.code = :code_3 )",
				query);
	}

	/**
	 * <p>test_where_three_conditions.</p>
	 */
	@Test
	public void test_where_three_conditions() {
		Entity from = from(Entity.class);
		where(from.getName()).eq("test").and(from.getIntegerField()).gt(2).and(from.getCode()).eq("test");
		Query<Entity> select = select(from);

		assertEquals(
				"select entity_0 from Entity entity_0 where entity_0.name = :name_1 and entity_0.integerField > :integerField_2 and entity_0.code = :code_3",
				select.getQuery());
	}

	/**
	 * <p>testDateFieldAccessToRelationalCondition.</p>
	 */
	@Test
	public void testDateFieldAccessToRelationalCondition() {
		Entity from = from(Entity.class);
		where(from.getDateField()).gt(new Date());
		Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 where entity_0.dateField > :dateField_1", select.getQuery());
	}

	/**
	 * <p>acceptConditionInWhere.</p>
	 */
	@Test
	public void acceptConditionInWhere() {
		Entity from = from(Entity.class);
		OnGoingLogicalCondition conditon = condition(from.getCode()).eq("test").and(from.getPrimitiveInt()).gt(3);
		where(conditon);
		Query<Entity> select = select(from);
		assertEquals(
				"select entity_0 from Entity entity_0 where ( entity_0.code = :code_1 and entity_0.primitiveInt > :primitiveInt_2 )",
				select.getQuery());

	}

	/**
	 * <p>acceptConditionInWhere_plusExternalCondition.</p>
	 */
	@Test
	public void acceptConditionInWhere_plusExternalCondition() {
		Entity from = from(Entity.class);
		OnGoingLogicalCondition conditon = condition(from.getCode()).eq("test").and(from.getPrimitiveInt()).gt(3);
		where(conditon).and(from.getName()).isNotNull();
		Query<Entity> select = select(from);
		assertEquals(
				"select entity_0 from Entity entity_0 where ( entity_0.code = :code_1 and entity_0.primitiveInt > :primitiveInt_2 ) and entity_0.name is not null",
				select.getQuery());

	}

	/**
	 * <p>conditionOnRelatedField.</p>
	 */
	@Test
	public void conditionOnRelatedField() {
		Entity from = from(Entity.class);
		where(from.getCode()).eq(from.getName());
		Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 where entity_0.code = entity_0.name", select.getQuery());
	}

	/**
	 * <p>chainMethodCallIntoWhere.</p>
	 */
	@Test
	public void chainMethodCallIntoWhere() {
		Entity from = from(Entity.class);
		where(from.getSubEntity().getName()).eq("test");
		Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 where entity_0.subEntity.name = :name_1", select.getQuery());
	}

	/**
	 * <p>chainMethodCallOnAbstractMethodOveride.</p>
	 */
	@Test
	public void chainMethodCallOnAbstractMethodOveride() {
		Entity from = from(Entity.class);
		where(from.getAbstractEntity().getName()).eq("test");
		Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 where entity_0.abstractEntity.name = :name_1",
				select.getQuery());
	}

	/**
	 * <p>testSmallCharBug16.</p>
	 */
	@Test
	public void testSmallCharBug16() {
		Entity from = from(Entity.class);
		where(from.getSmallChar()).eq('c');
		Query<Entity> select2 = select(from);
		assertEquals("select entity_0 from Entity entity_0 where entity_0.smallChar = :smallChar_1",
				select2.getQuery());
	}

	/**
	 * <p>testGetTheConditionQuery.</p>
	 */
	@Test
	public void testGetTheConditionQuery() {
		Entity from = from(Entity.class);
		where(from.getSmallChar()).eq('c');
		Query<String> select2 = select(from.getName());

		OnGoingLogicalCondition condition = select2.condition().get();
		condition.and(from.getId()).eq("test");

		assertEquals(
				"select entity_0.name from Entity entity_0 where entity_0.smallChar = :smallChar_1 and entity_0.id = :id_2",
				select2.getQuery());
	}

	/**
	 * <p>testGetEmptyConditionQuery.</p>
	 */
	@Test
	public void testGetEmptyConditionQuery() {
		Entity from = from(Entity.class);
		Query<String> select2 = select(from.getName());

		Optional<OnGoingLogicalCondition> condition = select2.condition();

		if (condition.isPresent()) {
			condition.get().and(from.getSmallChar()).eq('c');
		} else {
			where(from.getSmallChar()).eq('c');
		}

		assertEquals("select entity_0.name from Entity entity_0 where entity_0.smallChar = :smallChar_1",
				select2.getQuery());
	}

}
