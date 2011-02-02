package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.util.List;

public interface QueryHandler<T> {
    T handleCall(QueryBuilder queryImpl, List<Method> methods);
}
