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
package org.torpedoquery.jpa.internal.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.Selector;



public class OrderBy {

	private final List<Selector> orders = new ArrayList<Selector>();

	public void addOrder(Selector selector) {
		orders.add(selector);
	}

	public String createQueryFragment(StringBuilder builder, QueryBuilder queryBuilder, AtomicInteger incrementor) {

		if (!orders.isEmpty()) {
			Iterator<Selector> iterator = orders.iterator();

			if (builder.length() == 0) {
				builder.append(" order by ").append(iterator.next().createQueryFragment(incrementor));
			}

			while (iterator.hasNext()) {
				Selector selector = iterator.next();
				builder.append(",").append(selector.createQueryFragment(incrementor));
			}

			return builder.toString();
		}
		return "";
	}
}
