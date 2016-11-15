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
package org.torpedoquery.jpa.internal.query;

import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.internal.Parameter;
public class ValueParameter<T> implements Parameter<T> {

	private final String fieldName;
	private final T value;
	private String name;

	/**
	 * <p>Constructor for ValueParameter.</p>
	 *
	 * @param fieldName a {@link java.lang.String} object.
	 * @param value a T object.
	 */
	public ValueParameter(String fieldName, T value) {
		this.fieldName = fieldName;
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.torpedoquery.jpa.internal.IParameter#generate(java.util.concurrent
	 * .atomic.AtomicInteger)
	 */
	/** {@inheritDoc} */
	@Override
	public String generate(AtomicInteger incrementor) {

		if (name == null) {
			name = fieldName + "_" + incrementor.getAndIncrement();
		}
		return ":" + name;
	}

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return a T object.
	 */
	public T getValue() {
		return value;
	}

}
