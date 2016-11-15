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

import org.torpedoquery.jpa.internal.Condition;
public class AndCondition extends LogicalElement {

	/**
	 * <p>Constructor for AndCondition.</p>
	 *
	 * @param left a {@link org.torpedoquery.jpa.internal.Condition} object.
	 * @param right a {@link org.torpedoquery.jpa.internal.Condition} object.
	 */
	public AndCondition(Condition left, Condition right) {
		super(left, right);
	}

	/** {@inheritDoc} */
	@Override
	protected String getCondition() {
		return " and ";
	}

}
