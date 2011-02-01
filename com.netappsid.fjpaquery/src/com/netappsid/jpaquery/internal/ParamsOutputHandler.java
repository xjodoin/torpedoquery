package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.util.Map;


public class ParamsOutputHandler implements QueryHandler<Map<String, Object>> {

	@Override
	public Map<String, Object> handleCall(QueryBuilder queryImpl,
			Method thisMethod) {
		return queryImpl.getParams();
	}

}
