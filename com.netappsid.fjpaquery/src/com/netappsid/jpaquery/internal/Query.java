package com.netappsid.jpaquery.internal;

import java.util.Map;

public interface Query {
    String getQuery();

    Map<String, Object> getParameters();

    <T> T handle(QueryHandler<T> handler);
}
