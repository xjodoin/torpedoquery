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

public interface ValueOnGoingCondition<T> extends OnGoingCondition<T> {

	OnGoingLogicalCondition eq(T value);
	
	OnGoingLogicalCondition eq(Class<? extends T> value);
	
	OnGoingLogicalCondition eq(Function<T> value);

	OnGoingLogicalCondition neq(T value);
	
	OnGoingLogicalCondition neq(Class<? extends T> value);

	OnGoingLogicalCondition neq(Function<T> value);
	
	OnGoingLogicalCondition isNull();

	OnGoingLogicalCondition isNotNull();

	OnGoingLogicalCondition in(T... values);

	OnGoingLogicalCondition in(Collection<T> values);

	OnGoingLogicalCondition in(Query<T> subQuery);

	OnGoingLogicalCondition notIn(T... values);

	OnGoingLogicalCondition notIn(Collection<T> values);

	OnGoingLogicalCondition notIn(Query<T> subQuery);

}
