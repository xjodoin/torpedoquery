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
package org.torpedoquery.jpa.internal.handlers;

import org.torpedoquery.jpa.ComparableFunction;
public class CustomFunctionHandler<T> extends
		BaseFunctionHandler<T, ComparableFunction<T>> {

	private final String name;
	private String functionFormat;

	/**
	 * <p>Constructor for CustomFunctionHandler.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 */
	public CustomFunctionHandler(String name, Object value) {
		super(value);
		this.name = name;
	}

	/** {@inheritDoc} */
	@Override
	protected String getFunctionFormat() {
		return functionFormat != null ? functionFormat : name + "(%1$s)";
	}

	/**
	 * <p>Setter for the field <code>functionFormat</code>.</p>
	 *
	 * @param functionFormat a {@link java.lang.String} object.
	 */
	public void setFunctionFormat(String functionFormat) {
		this.functionFormat = functionFormat;
	}

}
