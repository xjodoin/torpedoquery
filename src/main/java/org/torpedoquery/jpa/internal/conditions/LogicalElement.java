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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.internal.Condition;
import org.torpedoquery.jpa.internal.Parameter;
public abstract class LogicalElement implements Condition {

	private final Condition left;
	private final Condition right;

	/**
	 * <p>Constructor for LogicalElement.</p>
	 *
	 * @param left a {@link org.torpedoquery.jpa.internal.Condition} object.
	 * @param right a {@link org.torpedoquery.jpa.internal.Condition} object.
	 */
	public LogicalElement(Condition left, Condition right) {
		this.left = left;
		this.right = right;
	}

	/** {@inheritDoc} */
	@Override
	public List<Parameter> getParameters() {
		List<Parameter> parameters = new ArrayList<>();
		parameters.addAll(left.getParameters());
		parameters.addAll(right.getParameters());
		return parameters;
	}

	/** {@inheritDoc} */
	@Override
	public String createQueryFragment(AtomicInteger incrementor) {

		return left.createQueryFragment(incrementor) + getCondition() + right.createQueryFragment(incrementor);
	}

	/**
	 * <p>getCondition.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected abstract String getCondition();

}
