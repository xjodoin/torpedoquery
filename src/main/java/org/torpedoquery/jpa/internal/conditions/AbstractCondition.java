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
package org.torpedoquery.jpa.internal.conditions;

import java.util.List;

import org.torpedoquery.jpa.internal.Condition;
import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.Selector;
public abstract class AbstractCondition<T> implements Condition {
	private final Selector selector;
	private final List<Parameter> parameters;

	/**
	 * <p>Constructor for AbstractCondition.</p>
	 *
	 * @param selector a {@link org.torpedoquery.jpa.internal.Selector} object.
	 * @param parameters a {@link java.util.List} object.
	 */
	public AbstractCondition(Selector selector, List<Parameter> parameters) {
		this.selector = selector;
		this.parameters = parameters;
	}

	/**
	 * <p>Getter for the field <code>selector</code>.</p>
	 *
	 * @return a {@link org.torpedoquery.jpa.internal.Selector} object.
	 */
	public Selector getSelector() {
		return selector;
	}

	/** {@inheritDoc} */
	@Override
	public List<Parameter> getParameters() {
		return parameters;
	}

}
