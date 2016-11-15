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
import static org.torpedoquery.jpa.Torpedo.from;
import static org.torpedoquery.jpa.Torpedo.select;
import static org.torpedoquery.jpa.Torpedo.where;
import static org.torpedoquery.jpa.TorpedoFunction.length;
import static org.torpedoquery.jpa.TorpedoFunction.lower;
import static org.torpedoquery.jpa.TorpedoFunction.substring;
import static org.torpedoquery.jpa.TorpedoFunction.trim;
import static org.torpedoquery.jpa.TorpedoFunction.upper;

import org.junit.Test;
import org.torpedoquery.jpa.test.bo.Entity;

/**
 *
 *substring(), trim(), lower(), upper(), length()
 *
 * @author xjodoin
 * @version $Id: $Id
 * @since 2.0.0
 */
public class StringFunctionsTest {

	/**
	 * <p>testTrimFunction.</p>
	 */
	@Test
	public void testTrimFunction() {
		Entity from = from(Entity.class);
		Query<String> select = select(trim(from.getCode()));
		assertEquals("select trim(entity_0.code) from Entity entity_0", select.getQuery());
	}

	/**
	 * <p>testLowerFunction.</p>
	 */
	@Test
	public void testLowerFunction() {
		Entity from = from(Entity.class);
		Query<String> select = select(lower(from.getCode()));
		assertEquals("select lower(entity_0.code) from Entity entity_0", select.getQuery());
	}

	/**
	 * <p>testUpperFunction.</p>
	 */
	@Test
	public void testUpperFunction() {
		Entity from = from(Entity.class);
		Query<String> select = select(upper(from.getCode()));
		assertEquals("select upper(entity_0.code) from Entity entity_0", select.getQuery());
	}
	
	/**
	 * <p>testLengthFunction.</p>
	 */
	@Test
	public void testLengthFunction() {
		Entity from = from(Entity.class);
		Query<Integer> select = select(length(from.getCode()));
		assertEquals("select length(entity_0.code) from Entity entity_0", select.getQuery());
	}
	
	/**
	 * <p>testLengthFunctionWithFunction.</p>
	 */
	@Test
	public void testLengthFunctionWithFunction() {
		Entity from = from(Entity.class);
		Query<Integer> select = select(length(trim(from.getCode())));
		assertEquals("select length(trim(entity_0.code)) from Entity entity_0", select.getQuery());
	}
	
	/**
	 * <p>testWhereWithStringFunction.</p>
	 */
	@Test
	public void testWhereWithStringFunction() {
		Entity from = from(Entity.class);
		where(lower(from.getCode())).like().any("test");
		Query<Entity> select = select(from);
		assertEquals("select entity_0 from Entity entity_0 where lower(entity_0.code) like '%test%'", select.getQuery());
	}
	
	/**
	 * <p>testSubstringFunction.</p>
	 */
	@Test
	public void testSubstringFunction() {
		Entity from = from(Entity.class);
		Query<String> select = select(substring(from.getCode(),2,4));
		assertEquals("select substring(entity_0.code,2,4) from Entity entity_0", select.getQuery());
	}
	
}
