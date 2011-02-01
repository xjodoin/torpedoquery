package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;

public interface QueryHandler<T> {

	T handleCall(QueryBuilder queryImpl, Method thisMethod);

}
