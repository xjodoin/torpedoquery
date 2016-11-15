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

import static org.torpedoquery.jpa.internal.TorpedoMagic.getTorpedoMethodHandler;
import static org.torpedoquery.jpa.internal.TorpedoMagic.setQuery;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.TorpedoProxy;
import org.torpedoquery.jpa.internal.functions.CoalesceFunction;
import org.torpedoquery.jpa.internal.functions.DynamicInstantiationFunction;
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
import org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler;
public class TorpedoFunction {

	// JPA Functions
	/**
	 * <p>count.</p>
	 *
	 * @param object a {@link java.lang.Object} object.
	 * @return a {@link org.torpedoquery.jpa.Function} object.
	 */
	public static Function<Long> count(Object object) {
		return function("count", Long.class, object);
	}

	/**
	 * <p>sum.</p>
	 *
	 * @param number a T object.
	 * @param <V> a V object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	public static <V, T extends Comparable<V>> ComparableFunction<V> sum(
			T number) {
		return getTorpedoMethodHandler().handle(
				new SumFunctionHandler<V>(number));
	}

	/**
	 * <p>sum.</p>
	 *
	 * @param number a {@link org.torpedoquery.jpa.Function} object.
	 * @param <V> a V object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	public static <V, T extends Comparable<V>> ComparableFunction<V> sum(
			Function<T> number) {
		return getTorpedoMethodHandler().handle(
				new SumFunctionHandler<V>(number));
	}

	/**
	 * <p>min.</p>
	 *
	 * @param number a T object.
	 * @param <V> a V object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	public static <V, T extends Comparable<V>> ComparableFunction<V> min(
			T number) {
		return getTorpedoMethodHandler().handle(
				new MinFunctionHandler<V>(number));
	}

	/**
	 * <p>min.</p>
	 *
	 * @param number a {@link org.torpedoquery.jpa.Function} object.
	 * @param <V> a V object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	public static <V, T extends Comparable<V>> ComparableFunction<V> min(
			Function<T> number) {
		return getTorpedoMethodHandler().handle(
				new MinFunctionHandler<V>(number));
	}

	/**
	 * <p>max.</p>
	 *
	 * @param number a T object.
	 * @param <V> a V object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	public static <V, T extends Comparable<V>> ComparableFunction<V> max(
			T number) {
		return getTorpedoMethodHandler().handle(
				new MaxFunctionHandler<V>(number));
	}

	/**
	 * <p>max.</p>
	 *
	 * @param number a {@link org.torpedoquery.jpa.Function} object.
	 * @param <V> a V object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	public static <V, T extends Comparable<V>> ComparableFunction<V> max(
			Function<T> number) {
		return getTorpedoMethodHandler().handle(
				new MaxFunctionHandler<V>(number));
	}

	/**
	 * <p>avg.</p>
	 *
	 * @param number a T object.
	 * @param <V> a V object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	public static <V, T extends Comparable<V>> ComparableFunction<V> avg(
			T number) {
		return getTorpedoMethodHandler().handle(
				new AvgFunctionHandler<V>(number));
	}

	/**
	 * <p>avg.</p>
	 *
	 * @param number a {@link org.torpedoquery.jpa.Function} object.
	 * @param <V> a V object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	public static <V, T extends Comparable<V>> ComparableFunction<V> avg(
			Function<T> number) {
		return getTorpedoMethodHandler().handle(
				new AvgFunctionHandler<V>(number));
	}

	/**
	 * <p>coalesce.</p>
	 *
	 * @param values a E object.
	 * @param <T> a T object.
	 * @return a E object.
	 */
	public static <T, E extends Function<T>> E coalesce(E... values) {
		CoalesceFunction<E> coalesceFunction = getCoalesceFunction(values);
		return (E) coalesceFunction;
	}

	/**
	 * <p>coalesce.</p>
	 *
	 * @param values a T object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.Function} object.
	 */
	public static <T> Function<T> coalesce(T... values) {
		return getCoalesceFunction(values);
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
	
	private static <T> DynamicInstantiationFunction<T> getDynamicInstantiationFunction(T val) {
		final DynamicInstantiationFunction<T> dynFunction = new DynamicInstantiationFunction<>(val);
		TorpedoMethodHandler torpedoMethodHandler = getTorpedoMethodHandler();
		Object[] params = torpedoMethodHandler.params();
		torpedoMethodHandler.handle(
				new ArrayCallHandler(new ValueHandler<Void>() {
					@Override
					public Void handle(TorpedoProxy proxy,
							QueryBuilder queryBuilder, Selector selector) {
						dynFunction.setQuery(proxy);
						dynFunction.addSelector(selector);
						return null;
					}
				}, params));
		return dynFunction;
	}
	/**
	 *
	 * Hibernate calls this "dynamic instantiation". JPQL supports some of this feature and calls it a "constructor expression".
	 *
	 * dyn(new ProjectionEntity(
	 *				param(entity.getCode()), param(entity.getIntegerField())
	 *
	 * Important: you need to wrap each constructor parameter with a param() call
	 *
	 * @param object a T object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.Function} object.
	 */
	public static <T> Function<T> dyn(T object) {
		return getDynamicInstantiationFunction(object);
	}

	/**
	 * <p>distinct.</p>
	 *
	 * @param object a T object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.Function} object.
	 */
	public static <T> Function<T> distinct(T object) {
		if (object instanceof TorpedoProxy) {
			setQuery((TorpedoProxy) object);
		}
		return getTorpedoMethodHandler().handle(
				new DistinctFunctionHandler<T>(object));
	}

	/**
	 * <p>constant.</p>
	 *
	 * @param constant a T object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.Function} object.
	 */
	public static <T> Function<T> constant(T constant) {
		return getTorpedoMethodHandler().handle(
				new ConstantFunctionHandler<T>(constant));
	}

	/**
	 * <p>constant.</p>
	 *
	 * @param constant a T object.
	 * @param <V> a V object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	public static <V, T extends Comparable<V>> ComparableFunction<T> constant(
			T constant) {
		return getTorpedoMethodHandler().handle(
				new ComparableConstantFunctionHandler<T>(constant));
	}

	/**
	 * <p>index.</p>
	 *
	 * @param object a T object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
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
	 * @param name a {@link java.lang.String} object.
	 * @param returnType a {@link java.lang.Class} object.
	 * @param value a {@link java.lang.Object} object.
	 * @param <T> a T object.
	 */
	public static <T> Function<T> function(String name, Class<T> returnType,
			Object value) {
		return getTorpedoMethodHandler().handle(
				new CustomFunctionHandler<T>(name, value));
	}

	/**
	 * <p>comparableFunction.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param returnType a {@link java.lang.Class} object.
	 * @param value a {@link java.lang.Object} object.
	 * @param <V> a V object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	public static <V, T extends Comparable<V>> ComparableFunction<V> comparableFunction(
			String name, Class<V> returnType, Object value) {
		return getTorpedoMethodHandler().handle(
				new CustomFunctionHandler<V>(name, value));
	}

	// orderBy function
	/**
	 * <p>asc.</p>
	 *
	 * @param object a T object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.Function} object.
	 */
	public static <T> Function<T> asc(T object) {
		return getTorpedoMethodHandler().handle(new AscFunctionHandler<T>());
	}

	/**
	 * <p>desc.</p>
	 *
	 * @param object a T object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.Function} object.
	 */
	public static <T> Function<T> desc(T object) {
		return getTorpedoMethodHandler().handle(new DescFunctionHandler<T>());
	}

	// math operation
	/**
	 * <p>operation.</p>
	 *
	 * @param left a T object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingMathOperation} object.
	 */
	public static <T> OnGoingMathOperation<T> operation(T left) {
		return getTorpedoMethodHandler().handle(
				new MathOperationHandler<T>(null));
	}

	/**
	 * <p>operation.</p>
	 *
	 * @param left a {@link org.torpedoquery.jpa.Function} object.
	 * @param <T> a T object.
	 * @return a {@link org.torpedoquery.jpa.OnGoingMathOperation} object.
	 */
	public static <T> OnGoingMathOperation<T> operation(Function<T> left) {
		return getTorpedoMethodHandler().handle(
				new MathOperationHandler<T>(left));
	}

	// string functions
	// substring(), trim(), lower(), upper(), length()

	/**
	 * <p>trim.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @return a {@link org.torpedoquery.jpa.Function} object.
	 */
	public static Function<String> trim(String field) {
		return function("trim", String.class, field);
	}

	/**
	 * <p>trim.</p>
	 *
	 * @param function a {@link org.torpedoquery.jpa.Function} object.
	 * @return a {@link org.torpedoquery.jpa.Function} object.
	 */
	public static Function<String> trim(Function<String> function) {
		return function("trim", String.class, function);
	}

	/**
	 * <p>lower.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @return a {@link org.torpedoquery.jpa.Function} object.
	 */
	public static Function<String> lower(String field) {
		return function("lower", String.class, field);
	}

	/**
	 * <p>lower.</p>
	 *
	 * @param function a {@link org.torpedoquery.jpa.Function} object.
	 * @return a {@link org.torpedoquery.jpa.Function} object.
	 */
	public static Function<String> lower(Function<String> function) {
		return function("lower", String.class, function);
	}

	/**
	 * <p>upper.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @return a {@link org.torpedoquery.jpa.Function} object.
	 */
	public static Function<String> upper(String field) {
		return function("upper", String.class, field);
	}

	/**
	 * <p>upper.</p>
	 *
	 * @param function a {@link org.torpedoquery.jpa.Function} object.
	 * @return a {@link org.torpedoquery.jpa.Function} object.
	 */
	public static Function<String> upper(Function<String> function) {
		return function("upper", String.class, function);
	}

	/**
	 * <p>length.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	public static ComparableFunction<Integer> length(String field) {
		return comparableFunction("length", Integer.class, field);
	}

	/**
	 * <p>length.</p>
	 *
	 * @param function a {@link org.torpedoquery.jpa.Function} object.
	 * @return a {@link org.torpedoquery.jpa.ComparableFunction} object.
	 */
	public static ComparableFunction<Integer> length(Function<String> function) {
		return comparableFunction("length", Integer.class, function);
	}

	/**
	 * <p>substring.</p>
	 *
	 * @param param a {@link java.lang.String} object.
	 * @param beginIndex a int.
	 * @param endIndex a int.
	 * @return a {@link org.torpedoquery.jpa.Function} object.
	 */
	public static Function<String> substring(String param, int beginIndex,
			int endIndex) {
		return getTorpedoMethodHandler().handle(
				new SubstringFunctionHandler(param, beginIndex, endIndex));
	}

	/**
	 * <p>substring.</p>
	 *
	 * @param param a {@link org.torpedoquery.jpa.Function} object.
	 * @param beginIndex a int.
	 * @param endIndex a int.
	 * @return a {@link org.torpedoquery.jpa.Function} object.
	 */
	public static Function<String> substring(Function<String> param,
			int beginIndex, int endIndex) {
		return getTorpedoMethodHandler().handle(
				new SubstringFunctionHandler(param, beginIndex, endIndex));
	}
	

}
