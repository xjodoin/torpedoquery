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
package org.torpedoquery.jpa.internal.joins;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.conditions.LogicalCondition;
public class LeftJoin extends AbstractJoin {

	/**
	 * <p>Constructor for LeftJoin.</p>
	 *
	 * @param join a {@link org.torpedoquery.core.QueryBuilder} object.
	 * @param fieldName a {@link java.lang.String} object.
	 */
	public LeftJoin(QueryBuilder join, String fieldName) {
		super(join, fieldName);
	}

	public LeftJoin(QueryBuilder join, LogicalCondition joinCondition) {
		super(join, joinCondition);
	}

	/** {@inheritDoc} */
	@Override
	public String getJoinType() {
		return "left";
	}

}
