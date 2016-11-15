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
public interface OnGoingMathOperation<T> {

	/**
	 * <p>plus.</p>
	 *
	 * @param right a T object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	ComparableFunction<T> plus(T right);

	/**
	 * <p>plus.</p>
	 *
	 * @param right a {@link org.torpedoquery.jpa.Function} object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	ComparableFunction<T> plus(Function<T> right);

	/**
	 * <p>subtract.</p>
	 *
	 * @param right a T object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	ComparableFunction<T> subtract(T right);

	/**
	 * <p>subtract.</p>
	 *
	 * @param right a {@link org.torpedoquery.jpa.Function} object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	ComparableFunction<T> subtract(Function<T> right);
	
	/**
	 * <p>multiply.</p>
	 *
	 * @param right a T object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	ComparableFunction<T> multiply(T right);

	/**
	 * <p>multiply.</p>
	 *
	 * @param right a {@link org.torpedoquery.jpa.Function} object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	ComparableFunction<T> multiply(Function<T> right);
	
	/**
	 * <p>divide.</p>
	 *
	 * @param right a T object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	ComparableFunction<T> divide(T right);

	/**
	 * <p>divide.</p>
	 *
	 * @param right a {@link org.torpedoquery.jpa.Function} object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	ComparableFunction<T> divide(Function<T> right);
}
