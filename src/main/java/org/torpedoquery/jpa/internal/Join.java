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
package org.torpedoquery.jpa.internal;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.internal.query.ValueParameter;
public interface Join extends Serializable {

	/**
	 * <p>appendWhereClause.</p>
	 *
	 * @param builder a {@link java.lang.StringBuilder} object.
	 * @param incrementor a {@link java.util.concurrent.atomic.AtomicInteger} object.
	 */
	void appendWhereClause(StringBuilder builder, AtomicInteger incrementor);

	/**
	 * <p>getJoin.</p>
	 *
	 * @param alias a {@link java.lang.String} object.
	 * @param incrementor a {@link java.util.concurrent.atomic.AtomicInteger} object.
	 * @return a {@link java.lang.String} object.
	 */
	String getJoin(String alias, AtomicInteger incrementor);

	/**
	 * <p>getParams.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	List<ValueParameter> getParams();

	/**
	 * <p>appendOrderBy.</p>
	 *
	 * @param builder a {@link java.lang.StringBuilder} object.
	 * @param incrementor a {@link java.util.concurrent.atomic.AtomicInteger} object.
	 */
	void appendOrderBy(StringBuilder builder, AtomicInteger incrementor);

	/**
	 * <p>appendGroupBy.</p>
	 *
	 * @param builder a {@link java.lang.StringBuilder} object.
	 * @param incrementor a {@link java.util.concurrent.atomic.AtomicInteger} object.
	 */
	void appendGroupBy(StringBuilder builder, AtomicInteger incrementor);

}
