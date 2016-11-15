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
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.Condition;
import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.Selector;
public class InSubQueryCondition<T> implements Condition {

	private final Selector selector;
	private final QueryBuilder subQuery;

	/**
	 * <p>Constructor for InSubQueryCondition.</p>
	 *
	 * @param selector a {@link org.torpedoquery.jpa.internal.Selector} object.
	 * @param query a {@link org.torpedoquery.core.QueryBuilder} object.
	 */
	public InSubQueryCondition(Selector selector, QueryBuilder query) {
		this.selector = selector;
		this.subQuery = query;
	}

	/** {@inheritDoc} */
	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		String queryFragment = selector.createQueryFragment(incrementor);

		String subQueryString = subQuery.getQuery(incrementor);
		return queryFragment + " " + getFragment() + " ( " + subQueryString + " ) ";
	}

	/**
	 * <p>getFragment.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected String getFragment() {
		return "in";
	}

	/** {@inheritDoc} */
	@Override
	public List<Parameter> getParameters() {
		return subQuery.getValueParameters();
	}

}
