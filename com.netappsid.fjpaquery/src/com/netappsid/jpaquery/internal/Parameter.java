package com.netappsid.jpaquery.internal;

import java.util.concurrent.atomic.AtomicInteger;

public class Parameter<T> {

	private final String fieldName;
	private final T value;
	private String name;

	public Parameter(String fieldName, T value) {
		this.fieldName = fieldName;
		this.value = value;
	}

	public String generate(AtomicInteger incrementor) {

		if (name == null) {
			name = fieldName + "_" + incrementor.getAndIncrement();
		}
		return name;
	}

	public String getName() {
		return name;
	}

	public T getValue() {
		return value;
	}

}
