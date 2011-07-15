package com.netappsid.jpaquery.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GroupBy {

	private final List<Selector> groups = new ArrayList<Selector>();

	public String createQueryFragment(StringBuilder builder, QueryBuilder queryBuilder, AtomicInteger incrementor) {

		if (!groups.isEmpty()) {
			Iterator<Selector> iterator = groups.iterator();

			if (builder.length() == 0) {
				builder.append(" group by ").append(iterator.next().createQueryFragment(queryBuilder, incrementor));
			}

			while (iterator.hasNext()) {
				Selector selector = iterator.next();
				builder.append(",").append(selector.createQueryFragment(queryBuilder, incrementor));
			}

			return builder.toString();
		}
		return "";
	}

	public void addGroup(Selector selector) {
		groups.add(selector);
	}
}
