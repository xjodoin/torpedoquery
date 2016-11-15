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
package org.torpedoquery.jpa;
public interface OnGoingComparableCondition<T> extends ValueOnGoingCondition<T> {
	/**
	 * <p>lt.</p>
	 *
	 * @param value a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition lt(T value);

	/**
	 * <p>lt.</p>
	 *
	 * @param value a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition lt(ComparableFunction<T> value);

	/**
	 * <p>lte.</p>
	 *
	 * @param value a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition lte(T value);

	/**
	 * <p>lte.</p>
	 *
	 * @param value a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition lte(ComparableFunction<T> value);

	/**
	 * <p>gt.</p>
	 *
	 * @param value a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition gt(T value);

	/**
	 * <p>gt.</p>
	 *
	 * @param value a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition gt(ComparableFunction<T> value);

	/**
	 * <p>gte.</p>
	 *
	 * @param value a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition gte(T value);

	/**
	 * <p>gte.</p>
	 *
	 * @param value a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition gte(ComparableFunction<T> value);
}
