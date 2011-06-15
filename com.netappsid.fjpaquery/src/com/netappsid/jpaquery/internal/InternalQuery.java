package com.netappsid.jpaquery.internal;

import java.util.Map;

import com.netappsid.jpaquery.Query;

public interface InternalQuery extends Query {
	String getQuery(Object proxy);

	Map<String, Object> getParameters(Object proxy);

	<T> T handle(QueryHandler<T> handler);
}
