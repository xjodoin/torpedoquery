package com.netappsid.jpaquery;

import java.util.Map;

public interface Query<T> {

	String getQuery();

	Map<String, Object> getParametersAsMap();

}
