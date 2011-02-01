package com.netappsid.jpaquery.internal;


public interface Query {

	String getQuery();

	<T> T handle(QueryHandler<T> handler);

}
