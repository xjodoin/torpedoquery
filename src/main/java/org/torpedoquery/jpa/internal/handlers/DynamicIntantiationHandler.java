/**
 *   Copyright Xavier Jodoin xjodoin@torpedoquery.org
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
package org.torpedoquery.jpa.internal.handlers;

import java.util.Deque;
import java.util.Map;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.ComparableFunction;
import org.torpedoquery.jpa.Function;
import org.torpedoquery.jpa.OnGoingMathOperation;
import org.torpedoquery.jpa.internal.MethodCall;
import org.torpedoquery.jpa.internal.TorpedoProxy;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.TorpedoMagic;
import org.torpedoquery.jpa.internal.functions.MathOperationFunction;
import org.torpedoquery.jpa.internal.selectors.SimpleMethodCallSelector;
import org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler;

public class DynamicIntantiationHandler<T> extends AbstractCallHandler<T> implements QueryHandler<Function<T>> {


	private T value;

	public DynamicIntantiationHandler(T value) {
		this.value = value;
		
	}

	@Override
	public Function<T> handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders,
			Deque<MethodCall> methods) {
		
		return null;
	}


}
