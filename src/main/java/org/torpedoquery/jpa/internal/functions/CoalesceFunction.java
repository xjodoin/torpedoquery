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
package org.torpedoquery.jpa.internal.functions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.ComparableFunction;
import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.Proxy;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.query.SelectorParameter;

public class CoalesceFunction<T> implements ComparableFunction<T> {

	private final List<Selector> selectors = new ArrayList<Selector>();
	private Proxy proxy;

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {

		StringBuffer stringBuffer = new StringBuffer();
		Iterator<Selector> iterator = selectors.iterator();
		Selector first = iterator.next();
		stringBuffer.append("coalesce(").append(first.createQueryFragment(incrementor));

		while (iterator.hasNext()) {
			Selector selector = iterator.next();
			stringBuffer.append(",").append(selector.createQueryFragment(incrementor));
		}

		stringBuffer.append(")");

		return stringBuffer.toString();
	}

	@Override
	public Object getProxy() {
		return proxy;
	}

	public void addSelector(Selector selector) {
		selectors.add(selector);
	}

	public void setQuery(Proxy proxy) {
		this.proxy = proxy;

	}

	@Override
	public Parameter<T> generateParameter(T value) {
		return new SelectorParameter<T>(this);
	}

}
