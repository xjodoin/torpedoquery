package com.netappsid.jpaquery.internal;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.Query;

public interface InternalQuery extends Query {
	String getQuery(Object proxy);

	List<Parameter> getParameters(Object proxy);

	<T> T handle(QueryHandler<T> handler);

	String getQuery(Object proxy, AtomicInteger incrementor);

	Map<String, Object> getParametersAsMap(Object proxy);

	FJPAMethodHandler getFJPAMethodHandler();
}
