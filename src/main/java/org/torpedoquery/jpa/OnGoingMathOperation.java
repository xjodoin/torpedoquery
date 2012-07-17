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

public interface OnGoingMathOperation<T> {

	ComparableFunction<T> plus(T right);

	ComparableFunction<T> plus(Function<T> right);

	ComparableFunction<T> subtract(T right);

	ComparableFunction<T> subtract(Function<T> right);
	
	ComparableFunction<T> multiply(T right);

	ComparableFunction<T> multiply(Function<T> right);
	
	ComparableFunction<T> divide(T right);

	ComparableFunction<T> divide(Function<T> right);
}
