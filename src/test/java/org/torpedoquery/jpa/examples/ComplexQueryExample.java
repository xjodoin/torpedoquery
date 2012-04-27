package org.torpedoquery.jpa.examples;

import static org.torpedoquery.jpa.Torpedo.*;

import java.util.List;

import javax.persistence.EntityManager;

public class ComplexQueryExample {

	private EntityManager manager;

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
