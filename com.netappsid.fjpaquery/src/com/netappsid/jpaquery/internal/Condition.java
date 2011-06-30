package com.netappsid.jpaquery.internal;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface Condition {

	String createQueryFragment(QueryBuilder queryBuilder, AtomicInteger incrementor);

	List<Parameter> getParameters();
}
