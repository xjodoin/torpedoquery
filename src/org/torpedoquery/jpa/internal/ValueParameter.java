/**
 *
 *   Copyright 2011 Xavier Jodoin xjodoin@gmail.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.torpedoquery.jpa.internal;

import java.util.concurrent.atomic.AtomicInteger;

public class ValueParameter<T> implements Parameter<T> {

	private final String fieldName;
	private final T value;
	private String name;

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
	@Override
	public String generate(AtomicInteger incrementor) {

		if (name == null) {
			name = fieldName + "_" + incrementor.getAndIncrement();
		}
		return ":" + name;
	}

	public String getName() {
		return name;
	}

	public T getValue() {
		return value;
	}

}
