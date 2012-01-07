package org.torpedoquery.jpa.internal;
public interface ValueHandler {
		void handle(Proxy proxy, QueryBuilder queryBuilder, Selector selector);
	}