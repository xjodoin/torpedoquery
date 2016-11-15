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
public interface OnGoingGroupByCondition extends Serializable {

	/**
	 * <p>having.</p>
	 *
	 * @param object a {@link org.torpedoquery.jpa.Function} object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.ValueOnGoingCondition} object.
	 */
	public <T> ValueOnGoingCondition<T> having(Function<T> object);

	/**
	 * <p>having.</p>
	 *
	 * @param object a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingComparableCondition} object.
	 */
	public <T extends Comparable<?>> OnGoingComparableCondition<T> having(ComparableFunction<T> object);

	/**
	 * <p>having.</p>
	 *
	 * @param object a T object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.ValueOnGoingCondition} object.
	 */
	public <T> ValueOnGoingCondition<T> having(T object);

	/**
	 * <p>having.</p>
	 *
	 * @param object a T object.
	 * @param <V> a V object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingComparableCondition} object.
	 */
	public <V, T extends Comparable<V>> OnGoingComparableCondition<V> having(T object);

	/**
	 * <p>having.</p>
	 *
	 * @param object a {@link java.lang.String} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingStringCondition} object.
	 */
	public OnGoingStringCondition<String> having(String object);

	/**
	 * <p>having.</p>
	 *
	 * @param object a {@link java.util.Collection} object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingCollectionCondition} object.
	 */
	public <T> OnGoingCollectionCondition<T> having(Collection<T> object);
	
	/**
	 * <p>having.</p>
	 *
	 * @param condition a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition having(OnGoingLogicalCondition condition);

}
