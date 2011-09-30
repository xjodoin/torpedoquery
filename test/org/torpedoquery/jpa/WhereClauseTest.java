package com.netappsid.jpaquery;

import static com.netappsid.jpaquery.FJPAQuery.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.Map;

import org.junit.Test;

import com.netappsid.jpaquery.test.bo.Entity;
import com.netappsid.jpaquery.test.bo.SubEntity;

public class WhereClauseTest {

	@Test
	public void test_eq() {
		Entity from = from(Entity.class);
		where(from.getCode()).eq("test");

		assertEquals("from Entity entity_0 where entity_0.code = :code_1", query(from));

	}

	@Test
	public void test_neq() {
		Entity from = from(Entity.class);
		where(from.getCode()).neq("test");

		assertEquals("from Entity entity_0 where entity_0.code <> :code_1", query(from));

	}

	@Test
	public void test_lt() {
		Entity from = from(Entity.class);
		where(from.getIntegerField()).lt(2);

		assertEquals("from Entity entity_0 where entity_0.integerField < :integerField_1", query(from));

	}

	@Test
	public void test_lte() {
		Entity from = from(Entity.class);
		where(from.getIntegerField()).lte(2);

		assertEquals("from Entity entity_0 where entity_0.integerField <= :integerField_1", query(from));

	}

	@Test
	public void test_gt() {
		Entity from = from(Entity.class);
		where(from.getIntegerField()).gt(2);

		assertEquals("from Entity entity_0 where entity_0.integerField > :integerField_1", query(from));

	}

	@Test
	public void test_gte() {
		Entity from = from(Entity.class);
		where(from.getIntegerField()).gte(2);

		assertEquals("from Entity entity_0 where entity_0.integerField >= :integerField_1", query(from));

	}

	@Test
	public void test_gte_primitive() {
		Entity from = from(Entity.class);
		where(from.getPrimitiveInt()).gte(2);

		assertEquals("from Entity entity_0 where entity_0.primitiveInt >= :primitiveInt_1", query(from));

	}

	@Test
	public void test_isNull() {
		Entity from = from(Entity.class);
		where(from.getCode()).isNull();

		assertEquals("from Entity entity_0 where entity_0.code is null", query(from));

	}

	@Test
	public void test_isNotNull() {
		Entity from = from(Entity.class);
		where(from.getCode()).isNotNull();

		assertEquals("from Entity entity_0 where entity_0.code is not null", query(from));

	}

	@Test
	public void test_in_values() {
		Entity from = from(Entity.class);
		where(from.getPrimitiveInt()).in(3, 4);

		assertEquals("from Entity entity_0 where entity_0.primitiveInt in ( :primitiveInt_1 )", query(from));

	}

	@Test
	public void test_in_subSelect() {
		Entity subSelect = from(Entity.class);

		Entity from = from(Entity.class);
		where(from.getCode()).in(select(subSelect.getCode()));

		assertEquals("from Entity entity_0 where entity_0.code in ( select entity_1.code from Entity entity_1 )", query(from));

	}

	@Test
	public void test_notIn_values() {
		Entity from = from(Entity.class);
		where(from.getPrimitiveInt()).notIn(3, 4);

		assertEquals("from Entity entity_0 where entity_0.primitiveInt not in ( :primitiveInt_1 )", query(from));

	}

	@Test
	public void test_notIn_subSelect() {
		Entity subSelect = from(Entity.class);

		Entity from = from(Entity.class);
		where(from.getCode()).notIn(select(subSelect.getCode()));

		assertEquals("from Entity entity_0 where entity_0.code not in ( select entity_1.code from Entity entity_1 )", query(from));

	}

	@Test
	public void test_in_subSelect_with_params() {
		Entity subSelect = from(Entity.class);
		where(subSelect.getCode()).eq("subquery");
		Entity from = from(Entity.class);
		where(from.getCode()).in(select(subSelect.getCode()));

		assertEquals("from Entity entity_0 where entity_0.code in ( select entity_1.code from Entity entity_1 where entity_1.code = :code_2 )", query(from));
		Map<String, Object> params = params(from);
		assertEquals(1, params.size());
		assertEquals("subquery", params.get("code_2"));

	}

	@Test
	public void test_And_WhereClause() {
		Entity from = from(Entity.class);
		where(from.getName()).eq("test").and(from.getPrimitiveInt()).gt(10);

		assertEquals("from Entity entity_0 where entity_0.name = :name_1 and entity_0.primitiveInt > :primitiveInt_2", query(from));
	}

	@Test
	public void test_Or_WhereClause() {
		Entity from = from(Entity.class);
		where(from.getName()).eq("test").or(from.getPrimitiveInt()).gt(10);

		assertEquals("from Entity entity_0 where entity_0.name = :name_1 or entity_0.primitiveInt > :primitiveInt_2", query(from));
	}

	@Test
	public void test_like_any() {
		Entity from = from(Entity.class);
		where(from.getCode()).like().any("test");

		assertEquals("from Entity entity_0 where entity_0.code like '%test%'", query(from));
	}

	@Test
	public void test_like_startsWith() {
		Entity from = from(Entity.class);
		where(from.getCode()).like().startsWith("test");

		assertEquals("from Entity entity_0 where entity_0.code like 'test%'", query(from));
	}

	@Test
	public void test_like_endsWith() {
		Entity from = from(Entity.class);
		where(from.getCode()).like().endsWith("test");

		assertEquals("from Entity entity_0 where entity_0.code like '%test'", query(from));
	}

	@Test
	public void test_is_empty() {
		Entity from = from(Entity.class);
		where(from.getSubEntities()).isEmpty();

		assertEquals("from Entity entity_0 where entity_0.subEntities is empty", query(from));
	}

	@Test
	public void test_is_not_empty() {
		Entity from = from(Entity.class);
		where(from.getSubEntities()).isNotEmpty();

		assertEquals("from Entity entity_0 where entity_0.subEntities is not empty", query(from));
	}

	@Test
	public void test_size() {
		Entity from = from(Entity.class);
		where(from.getSubEntities()).size().gt(2);

		assertEquals("from Entity entity_0 where entity_0.subEntities.size > :subEntities_1", query(from));
	}

	@Test
	public void test_where_with_condition_and() {
		Entity from = from(Entity.class);
		OnGoingLogicalCondition condition = condition(from.getCode()).eq("test").or(from.getCode()).eq("test2");
		where(from.getName()).eq("test").and(condition);

		assertEquals("from Entity entity_0 where entity_0.name = :name_1 and ( entity_0.code = :code_2 or entity_0.code = :code_3 )", query(from));
	}

	@Test
	public void test_where_with_condition_or() {
		Entity from = from(Entity.class);
		OnGoingLogicalCondition condition = condition(from.getCode()).eq("test").or(from.getCode()).eq("test2");
		where(from.getName()).eq("test").or(condition);

		assertEquals("from Entity entity_0 where entity_0.name = :name_1 or ( entity_0.code = :code_2 or entity_0.code = :code_3 )", query(from));
	}

	@Test
	public void test_where_with_condition_or_inline() {
		Entity from = from(Entity.class);
		where(from.getName()).eq("test").or(condition(from.getCode()).eq("test").or(from.getCode()).eq("test2"));

		assertEquals("from Entity entity_0 where entity_0.name = :name_1 or ( entity_0.code = :code_2 or entity_0.code = :code_3 )", query(from));
	}

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

	@Test
	public void test_acceptConditionIntoCondition() {
		Entity from = from(Entity.class);
		OnGoingLogicalCondition onGoingLogicalCondition = condition(from.getName()).eq("test").or(from.getName()).eq("test2");
		OnGoingLogicalCondition eq = condition(onGoingLogicalCondition).and(from.getCode()).eq("mycode");
		where(eq);
		Query<Entity> select = select(from);
		String query = select.getQuery();
		assertEquals("select entity_0 from Entity entity_0 where ( ( entity_0.name = :name_1 or entity_0.name = :name_2 ) and entity_0.code = :code_3 )", query);
	}

	@Test
	public void test_where_three_conditions() {
		Entity from = from(Entity.class);
		where(from.getName()).eq("test").and(from.getIntegerField()).gt(2).and(from.getCode()).eq("test");

		assertEquals("from Entity entity_0 where entity_0.name = :name_1 and entity_0.integerField > :integerField_2 and entity_0.code = :code_3", query(from));
	}

	@Test
	public void testDateFieldAccessToRelationalCondition() {
		Entity from = from(Entity.class);
		where(from.getDateField()).gt(new Date());
		Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 where entity_0.dateField > :dateField_1", select.getQuery());
	}

	@Test
	public void acceptConditionInWhere() {
		Entity from = from(Entity.class);
		OnGoingLogicalCondition conditon = condition(from.getCode()).eq("test").and(from.getPrimitiveInt()).gt(3);
		where(conditon);
		Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 where ( entity_0.code = :code_1 and entity_0.primitiveInt > :primitiveInt_2 )", select.getQuery());

	}

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
}
