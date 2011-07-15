package com.netappsid.jpaquery;

public interface PostFunction<E,T> {

	E execute(T value);
	
}
