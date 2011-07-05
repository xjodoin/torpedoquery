package com.netappsid.jpaquery.internal;

import java.util.Deque;
import java.util.Map;

public interface QueryHandler<T> {
	T handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, Deque<MethodCall> methods);
}
