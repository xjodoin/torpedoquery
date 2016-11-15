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

import java.util.Collection;
public interface ValueOnGoingCondition<T> extends OnGoingCondition<T> {

	/**
	 * <p>eq.</p>
	 *
	 * @param value a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition eq(T value);
	
	/**
	 * This method is use to create polymorphic condition
	 * ex: select myObject from MyObject myObject where myObject.class = ExtendMyObject
	 *
	 * @param value a {@link java.lang.Class} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition eq(Class<? extends T> value);
	
	/**
	 * <p>eq.</p>
	 *
	 * @param value a {@link org.torpedoquery.jpa.Function} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition eq(Function<T> value);

	/**
	 * <p>neq.</p>
	 *
	 * @param value a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition neq(T value);
	
	/**
	 * This method is use to create polymorphic condition
	 * ex: select myObject from MyObject myObject where myObject.class != ExtendMyObject
	 *
	 * @param value a {@link java.lang.Class} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition neq(Class<? extends T> value);

	/**
	 * <p>neq.</p>
	 *
	 * @param value a {@link org.torpedoquery.jpa.Function} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition neq(Function<T> value);
	
	/**
	 * <p>isNull.</p>
	 *
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition isNull();

	/**
	 * <p>isNotNull.</p>
	 *
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition isNotNull();

	/**
	 * <p>in.</p>
	 *
	 * @param values a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition in(T... values);

	/**
	 * <p>in.</p>
	 *
	 * @param values a {@link java.util.Collection} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition in(Collection<T> values);

	/**
	 * <p>in.</p>
	 *
	 * @param subQuery a {@link org.torpedoquery.jpa.Query} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition in(Query<T> subQuery);

	/**
	 * <p>notIn.</p>
	 *
	 * @param values a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition notIn(T... values);

	/**
	 * <p>notIn.</p>
	 *
	 * @param values a {@link java.util.Collection} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition notIn(Collection<T> values);

	/**
	 * <p>notIn.</p>
	 *
	 * @param subQuery a {@link org.torpedoquery.jpa.Query} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition notIn(Query<T> subQuery);
	
	/**
	 * <p>between.</p>
	 *
	 * @param from a T object.
	 * @param to a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition between(T from,T to);
	
	/**
	 * <p>between.</p>
	 *
	 * @param from a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 * @param to a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition between(ComparableFunction<T> from,ComparableFunction<T> to);
	
	/**
	 * <p>notBetween.</p>
	 *
	 * @param from a T object.
	 * @param to a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition notBetween(T from,T to);

	/**
	 * <p>notBetween.</p>
	 *
	 * @param from a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 * @param to a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingLogicalCondition} object.
	 */
	OnGoingLogicalCondition notBetween(ComparableFunction<T> from,ComparableFunction<T> to);
}
