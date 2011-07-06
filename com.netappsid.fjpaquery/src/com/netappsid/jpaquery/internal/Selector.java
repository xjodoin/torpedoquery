package com.netappsid.jpaquery.internal;

import java.util.concurrent.atomic.AtomicInteger;

public interface Selector {

	String createQueryFragment(QueryBuilder queryBuilder, AtomicInteger incrementor);

	String getName();

}
