package com.netappsid.jpaquery.internal;

import java.util.List;
import java.util.Map;


public interface QueryHandler<T> {
    T handleCall(Map<Object, QueryBuilder> proxyQueryBuilders, List<MethodCall> methods);
}
