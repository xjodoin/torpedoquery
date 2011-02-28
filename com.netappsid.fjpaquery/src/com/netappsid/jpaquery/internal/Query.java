package com.netappsid.jpaquery.internal;

import java.util.Map;

public interface Query {
	String getQuery(Object proxy);

	Map<String, Object> getParameters(Object proxy);

	<T> T handle(QueryHandler<T> handler);
}
