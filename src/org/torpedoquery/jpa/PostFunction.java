package org.torpedoquery.jpa;

public interface PostFunction<E,T> {

	E execute(T value);
	
}
