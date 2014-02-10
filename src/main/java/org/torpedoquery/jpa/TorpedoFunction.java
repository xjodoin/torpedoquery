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
package org.torpedoquery.jpa;

import static org.torpedoquery.jpa.internal.TorpedoMagic.getTorpedoMethodHandler;
import static org.torpedoquery.jpa.internal.TorpedoMagic.setQuery;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.TorpedoProxy;
import org.torpedoquery.jpa.internal.functions.CoalesceFunction;
import org.torpedoquery.jpa.internal.handlers.ArrayCallHandler;
import org.torpedoquery.jpa.internal.handlers.AscFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.AvgFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.ComparableConstantFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.ConstantFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.CustomFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.DescFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.DistinctFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.IndexFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.MathOperationHandler;
import org.torpedoquery.jpa.internal.handlers.MaxFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.MinFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.SubstringFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.SumFunctionHandler;
import org.torpedoquery.jpa.internal.handlers.ValueHandler;

public class TorpedoFunction {

	// JPA Functions
	public static Function<Long> count(Object object) {
		return function("count", Long.class, object);
	}

	public static <V, T extends Comparable<V>> ComparableFunction<V> sum(
			T number) {
		return getTorpedoMethodHandler().handle(
				new SumFunctionHandler<V>(number));
	}

	public static <V, T extends Comparable<V>> ComparableFunction<V> sum(
			Function<T> number) {
		return getTorpedoMethodHandler().handle(
				new SumFunctionHandler<V>(number));
	}

	public static <V, T extends Comparable<V>> ComparableFunction<V> min(
			T number) {
		return getTorpedoMethodHandler().handle(
				new MinFunctionHandler<V>(number));
	}

	public static <V, T extends Comparable<V>> ComparableFunction<V> min(
			Function<T> number) {
		return getTorpedoMethodHandler().handle(
				new MinFunctionHandler<V>(number));
	}

	public static <V, T extends Comparable<V>> ComparableFunction<V> max(
			T number) {
		return getTorpedoMethodHandler().handle(
				new MaxFunctionHandler<V>(number));
	}

	public static <V, T extends Comparable<V>> ComparableFunction<V> max(
			Function<T> number) {
		return getTorpedoMethodHandler().handle(
				new MaxFunctionHandler<V>(number));
	}

	public static <V, T extends Comparable<V>> ComparableFunction<V> avg(
			T number) {
		return getTorpedoMethodHandler().handle(
				new AvgFunctionHandler<V>(number));
	}

	public static <V, T extends Comparable<V>> ComparableFunction<V> avg(
			Function<T> number) {
		return getTorpedoMethodHandler().handle(
				new AvgFunctionHandler<V>(number));
	}

	public static <T, E extends Function<T>> E coalesce(E... values) {
		CoalesceFunction<E> coalesceFunction = getCoalesceFunction(values);
		return (E) coalesceFunction;
	}

	public static <T> Function<T> coalesce(T... values) {
		final CoalesceFunction<T> coalesceFunction = getCoalesceFunction(values);
		return coalesceFunction;
	}

	private static <T> CoalesceFunction<T> getCoalesceFunction(T... values) {
		final CoalesceFunction coalesceFunction = new CoalesceFunction();
		getTorpedoMethodHandler().handle(
				new ArrayCallHandler(new ValueHandler<Void>() {
					@Override
					public Void handle(TorpedoProxy proxy,
							QueryBuilder queryBuilder, Selector selector) {
						coalesceFunction.setQuery(proxy);
						coalesceFunction.addSelector(selector);
						return null;
					}
				}, values));
		return coalesceFunction;
	}

	public static <T> Function<T> distinct(T object) {
		if (object instanceof TorpedoProxy) {
			setQuery((TorpedoProxy) object);
		}
		return getTorpedoMethodHandler().handle(
				new DistinctFunctionHandler<T>(object));
	}

	public static <T> Function<T> constant(T constant) {
		return getTorpedoMethodHandler().handle(
				new ConstantFunctionHandler<T>(constant));
	}

	public static <V, T extends Comparable<V>> ComparableFunction<T> constant(
			T constant) {
		return getTorpedoMethodHandler().handle(
				new ComparableConstantFunctionHandler<T>(constant));
	}

	public static <T> ComparableFunction<Integer> index(T object) {
		if (object instanceof TorpedoProxy) {
			setQuery((TorpedoProxy) object);
		}
		return getTorpedoMethodHandler().handle(
				new IndexFunctionHandler(object));
	}

	/**
	 * Use this method to call functions witch are not supported natively by
	 * Torpedo
	 *
	 * @return your custom function
	 */
	public static <T> Function<T> function(String name, Class<T> returnType,
			Object value) {
		return getTorpedoMethodHandler().handle(
				new CustomFunctionHandler<T>(name, value));
	}

	public static <V, T extends Comparable<V>> ComparableFunction<V> comparableFunction(
			String name, Class<V> returnType, Object value) {
		return getTorpedoMethodHandler().handle(
				new CustomFunctionHandler<V>(name, value));
	}

	// orderBy function
	public static <T> Function<T> asc(T object) {
		return getTorpedoMethodHandler().handle(new AscFunctionHandler<T>());
	}

	public static <T> Function<T> desc(T object) {
		return getTorpedoMethodHandler().handle(new DescFunctionHandler<T>());
	}

	// math operation
	public static <T> OnGoingMathOperation<T> operation(T left) {
		return getTorpedoMethodHandler().handle(
				new MathOperationHandler<T>(null));
	}

	public static <T> OnGoingMathOperation<T> operation(Function<T> left) {
		return getTorpedoMethodHandler().handle(
				new MathOperationHandler<T>(left));
	}

	// string functions
	// substring(), trim(), lower(), upper(), length()

	public static Function<String> trim(String field) {
		return function("trim", String.class, field);
	}

	public static Function<String> trim(Function<String> function) {
		return function("trim", String.class, function);
	}

	public static Function<String> lower(String field) {
		return function("lower", String.class, field);
	}

	public static Function<String> lower(Function<String> function) {
		return function("lower", String.class, function);
	}

	public static Function<String> upper(String field) {
		return function("upper", String.class, field);
	}

	public static Function<String> upper(Function<String> function) {
		return function("upper", String.class, function);
	}

	public static ComparableFunction<Integer> length(String field) {
		return comparableFunction("length", Integer.class, field);
	}

	public static ComparableFunction<Integer> length(Function<String> function) {
		return comparableFunction("length", Integer.class, function);
	}

	public static Function<String> substring(String param, int beginIndex,
			int endIndex) {
		return getTorpedoMethodHandler().handle(
				new SubstringFunctionHandler(param, beginIndex, endIndex));
	}

	public static Function<String> substring(Function<String> param,
			int beginIndex, int endIndex) {
		return getTorpedoMethodHandler().handle(
				new SubstringFunctionHandler(param, beginIndex, endIndex));
	}

}
