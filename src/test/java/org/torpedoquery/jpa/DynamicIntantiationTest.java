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

import static org.junit.Assert.*;
import static org.torpedoquery.jpa.Torpedo.*;

import org.junit.Test;
import org.torpedoquery.jpa.test.bo.Entity;
import org.torpedoquery.jpa.test.bo.ProjectionEntity;
public class DynamicIntantiationTest {

	/**
	 * <p>test.</p>
	 */
	@Test
	public void test() {
		Entity entity = from(Entity.class);

		Query<ProjectionEntity> select = select(dyn(new ProjectionEntity(
				param(entity.getCode()), param(entity.getIntegerField()))));
		assertEquals(
				"select new org.torpedoquery.jpa.test.bo.ProjectionEntity(entity_0.code,entity_0.integerField) from Entity entity_0",
				select.getQuery());
	}

}
