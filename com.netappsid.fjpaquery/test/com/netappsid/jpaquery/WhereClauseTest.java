package com.netappsid.jpaquery;

import static com.netappsid.jpaquery.FJPAQuery.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.netappsid.jpaquery.test.bo.Entity;

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
		where(from.getCode()).lt("test");

		assertEquals("from Entity entity_0 where entity_0.code < :code_1", query(from));

	}

	@Test
	public void test_lte() {
		Entity from = from(Entity.class);
		where(from.getCode()).lte("test");

		assertEquals("from Entity entity_0 where entity_0.code <= :code_1", query(from));

	}

	@Test
	public void test_gt() {
		Entity from = from(Entity.class);
		where(from.getCode()).gt("test");

		assertEquals("from Entity entity_0 where entity_0.code > :code_1", query(from));

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

}
