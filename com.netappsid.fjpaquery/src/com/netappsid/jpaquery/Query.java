package com.netappsid.jpaquery;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

public interface Query<T> {

	String getQuery();

	Map<String, Object> getParameters();
	
	<E> List<E> map(EntityManager entityManager,PostFunction<E,T> function);

	T get(EntityManager entityManager);

	List<T> list(EntityManager entityManager);

}
