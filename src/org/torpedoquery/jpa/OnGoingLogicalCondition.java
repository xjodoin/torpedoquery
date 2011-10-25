/**
 *
 *   Copyright 2011 Xavier Jodoin xjodoin@gmail.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.torpedoquery.jpa;

import java.util.Collection;

public interface OnGoingLogicalCondition {
	<T> ValueOnGoingCondition<T> and(T property);

	<T> ValueOnGoingCondition<T> or(T property);

	<V, T extends Comparable<V>> OnGoingComparableCondition<V> and(T property);
	
	<V, T extends Comparable<V>> OnGoingComparableCondition<V> or(T property);

	<T> OnGoingComparableCondition<T> and(ComparableFunction<T> property);
	
	<T> OnGoingComparableCondition<T> or(ComparableFunction<T> property);
	
	<T> OnGoingCollectionCondition<T> and(Collection<T> object);

	<T> OnGoingCollectionCondition<T> or(Collection<T> object);

	OnGoingStringCondition<String> and(String property);

	OnGoingStringCondition<String> or(String property);

	OnGoingLogicalCondition and(OnGoingLogicalCondition condition);

	OnGoingLogicalCondition or(OnGoingLogicalCondition condition);

}
