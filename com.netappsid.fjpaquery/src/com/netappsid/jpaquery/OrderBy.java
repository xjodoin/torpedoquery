package com.netappsid.jpaquery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.internal.QueryBuilder;
import com.netappsid.jpaquery.internal.Selector;

public class OrderBy {

	private final List<Selector> orders = new ArrayList<Selector>();

	public void addOrder(Selector selector) {
		orders.add(selector);
	}

	public String createQueryFragment(StringBuilder builder, QueryBuilder queryBuilder, AtomicInteger incrementor) {

		if (!orders.isEmpty()) {
			Iterator<Selector> iterator = orders.iterator();

			if (builder.length() == 0) {
				builder.append(" order by ").append(iterator.next().createQueryFragment(queryBuilder, incrementor));
			}

			while (iterator.hasNext()) {
				Selector selector = iterator.next();
				builder.append(",").append(selector.createQueryFragment(queryBuilder, incrementor));
			}

			return builder.toString();
		}
		return "";
	}
}
