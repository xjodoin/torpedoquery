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

import java.io.Serializable;
import java.util.Collection;
public interface OnGoingLogicalCondition extends Serializable {
	/**
	 * <p>and.</p>
	 *
	 * @param property a T object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.ValueOnGoingCondition} object.
	 */
	<T> ValueOnGoingCondition<T> and(T property);

	/**
	 * <p>or.</p>
	 *
	 * @param property a T object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.ValueOnGoingCondition} object.
	 */
	<T> ValueOnGoingCondition<T> or(T property);

	/**
	 * <p>and.</p>
	 *
	 * @param property a T object.
	 * @param <V> a V object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingComparableCondition} object.
	 */
	<V, T extends Comparable<V>> OnGoingComparableCondition<V> and(T property);
	
	/**
	 * <p>or.</p>
	 *
	 * @param property a T object.
	 * @param <V> a V object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingComparableCondition} object.
	 */
	<V, T extends Comparable<V>> OnGoingComparableCondition<V> or(T property);

	/**
	 * <p>and.</p>
	 *
	 * @param property a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingComparableCondition} object.
	 */
	<T> OnGoingComparableCondition<T> and(ComparableFunction<T> property);
	
	/**
	 * <p>or.</p>
	 *
	 * @param property a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingComparableCondition} object.
	 */
	<T> OnGoingComparableCondition<T> or(ComparableFunction<T> property);
	
	/**
	 * <p>and.</p>
	 *
	 * @param object a {@link java.util.Collection} object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingCollectionCondition} object.
	 */
	<T> OnGoingCollectionCondition<T> and(Collection<T> object);

	/**
	 * <p>or.</p>
	 *
	 * @param object a {@link java.util.Collection} object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingCollectionCondition} object.
	 */
	<T> OnGoingCollectionCondition<T> or(Collection<T> object);

	/**
	 * <p>and.</p>
	 *
	 * @param property a {@link java.lang.String} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingStringCondition} object.
	 */
	OnGoingStringCondition<String> and(String property);
	
	/**
	 * <p>and.</p>
	 *
	 * @param function a {@link org.torpedoquery.jpa.Function} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingStringCondition} object.
	 */
	OnGoingStringCondition<String> and(Function<String> function);

	/**
	 * <p>or.</p>
	 *
	 * @param property a {@link java.lang.String} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingStringCondition} object.
	 */
	OnGoingStringCondition<String> or(String property);
	
	/**
	 * <p>or.</p>
	 *
	 * @param function a {@link org.torpedoquery.jpa.Function} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingStringCondition} object.
	 */
	OnGoingStringCondition<String> or(Function<String> function);

	/**
	 * <p>and.</p>
	 *
	 * @param condition a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition and(OnGoingLogicalCondition condition);

	/**
	 * <p>or.</p>
	 *
	 * @param condition a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition or(OnGoingLogicalCondition condition);

}
