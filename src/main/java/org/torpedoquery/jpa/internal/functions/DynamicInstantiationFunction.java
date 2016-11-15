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
package org.torpedoquery.jpa.internal.functions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.ComparableFunction;
import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.TorpedoProxy;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.query.SelectorParameter;
public class DynamicInstantiationFunction<T> implements ComparableFunction<T> {

	private final List<Selector> selectors = new ArrayList<>();
	private final T object;
	private TorpedoProxy proxy;

	/**
	 * <p>Constructor for DynamicInstantiationFunction.</p>
	 *
	 * @param object a T object.
	 */
	public DynamicInstantiationFunction(T object) {
		this.object = object;
	}
	
	/** {@inheritDoc} */
	@Override
	public String createQueryFragment(AtomicInteger incrementor) {

		StringBuilder stringBuilder = new StringBuilder();
		Iterator<Selector> iterator = selectors.iterator();
		Selector first = iterator.next();
		stringBuilder.append("new "+object.getClass().getName()+"(").append(first.createQueryFragment(incrementor));

		while (iterator.hasNext()) {
			Selector selector = iterator.next();
			stringBuilder.append(',').append(selector.createQueryFragment(incrementor));
		}

		stringBuilder.append(')');

		return stringBuilder.toString();
	}

	/** {@inheritDoc} */
	@Override
	public Object getProxy() {
		return proxy;
	}

	/**
	 * <p>addSelector.</p>
	 *
	 * @param selector a {@link org.torpedoquery.jpa.internal.Selector} object.
	 */
	public void addSelector(Selector selector) {
		selectors.add(selector);
	}

	/**
	 * <p>setQuery.</p>
	 *
	 * @param proxy a {@link org.torpedoquery.jpa.internal.TorpedoProxy} object.
	 */
	public void setQuery(TorpedoProxy proxy) {
		this.proxy = proxy;

	}

	/** {@inheritDoc} */
	@Override
	public Parameter<T> generateParameter(T value) {
		return new SelectorParameter<>(this);
	}

}
