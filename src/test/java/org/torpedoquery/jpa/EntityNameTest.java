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

import java.util.UUID;

import javax.persistence.Id;

import org.junit.Test;
public class EntityNameTest {

	@javax.persistence.Entity(name = "myEntity")
	public static class EntityWithAnnotationName {
		@Id
		private String id = UUID.randomUUID().toString();

		public String getId() {
			return id;
		}
	}

	@javax.persistence.Entity()
	public static class EntityWithAnnotationWithoutName {
		@Id
		private String id = UUID.randomUUID().toString();

		public String getId() {
			return id;
		}
	}

	/**
	 * <p>test_createQueryWithName.</p>
	 */
	@Test
	public void test_createQueryWithName() {
		final EntityWithAnnotationName entity = from(EntityWithAnnotationName.class);
		org.torpedoquery.jpa.Query<EntityWithAnnotationName> select = select(entity);
		assertEquals("select myEntity_0 from myEntity myEntity_0",
				select.getQuery());
	}
	
	/**
	 * <p>test_createQueryWithoutName.</p>
	 */
	@Test
	public void test_createQueryWithoutName() {
		final EntityWithAnnotationWithoutName entity = from(EntityWithAnnotationWithoutName.class);
		org.torpedoquery.jpa.Query<EntityWithAnnotationWithoutName> select = select(entity);
		assertEquals("select entityWithAnnotationWithoutName_0 from EntityWithAnnotationWithoutName entityWithAnnotationWithoutName_0",
				select.getQuery());
	}
}
