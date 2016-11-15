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
package org.torpedoquery.jpa.examples;

import static org.torpedoquery.jpa.Torpedo.from;
import static org.torpedoquery.jpa.Torpedo.innerJoin;
import static org.torpedoquery.jpa.Torpedo.select;
import static org.torpedoquery.jpa.Torpedo.with;

import java.util.List;

import javax.persistence.EntityManager;
public class ComplexQueryExample {

	private EntityManager manager;

	/**
	 * <p>findUsers.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<User> findUsers() {
		User from = from(User.class);
		City city = innerJoin(from.getCity());
		with(city.getCode()).in("one", "two").or(city.getCode()).notIn("three", "four");
		District district = innerJoin(city.getDistrict());
		with(district.getCode()).notIn("exclude1", "exclude2");
		State state = innerJoin(district.getState());
		with(state.getCode()).eq("AP").or(state.getCode()).eq("GUJ").or(state.getCode()).eq("KTK");
		with(state.getCountry().getCode()).eq("india");

		return select(from).list(manager);
	}
}
