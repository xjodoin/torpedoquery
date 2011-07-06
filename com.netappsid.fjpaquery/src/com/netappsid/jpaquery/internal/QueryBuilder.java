package com.netappsid.jpaquery.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class QueryBuilder {
	private final Class<?> toQuery;
	private final List<Selector> toSelect = new ArrayList<Selector>();
	private final List<Join> joins = new ArrayList<Join>();
	private WhereClause<?> whereClause;

	private String alias;

	public QueryBuilder(Class<?> toQuery) {
		this.toQuery = toQuery;
	}

	public String getQuery(AtomicInteger incrementor) {

		String from = " from " + toQuery.getSimpleName() + " " + getAlias(incrementor);
		StringBuilder builder = new StringBuilder();

		appendSelect(builder, incrementor);

		builder.append(from);

		builder.append(getJoins(incrementor));

		if (whereClause != null) {
			builder.append(" where ").append(whereClause.createQueryFragment(this, incrementor));
		}

		return builder.toString().trim();
	}

	public void appendSelect(StringBuilder builder, AtomicInteger incrementor) {
		for (Selector selector : toSelect) {
			if (builder.length() == 0) {
				builder.append("select ").append(selector.createQueryFragment(this, incrementor));
			} else {
				builder.append(", ").append(selector.createQueryFragment(this, incrementor));
			}
		}

		for (Join join : joins) {
			join.appendSelect(builder, incrementor);
		}
	}

	public String getAlias(AtomicInteger incrementor) {
		if (alias == null) {
			final char[] charArray = toQuery.getSimpleName().toCharArray();

			charArray[0] = Character.toLowerCase(charArray[0]);
			alias = new String(charArray) + "_" + incrementor.getAndIncrement();
		}
		return alias;
	}

	public void addSelector(Selector selector) {
		toSelect.add(selector);
	}

	public void addJoin(Join innerJoin) {
		joins.add(innerJoin);
	}

	public boolean hasSubJoin() {
		return !joins.isEmpty();
	}

	public String getJoins(AtomicInteger incrementor) {

		StringBuilder builder = new StringBuilder();

		for (Join join : joins) {
			builder.append(join.getJoin(getAlias(incrementor), incrementor));
		}

		return builder.toString();
	}

	public void setWhereClause(WhereClause<?> whereClause) {

		if (this.whereClause != null) {
			throw new IllegalArgumentException("You cannot have more than one WhereClause by query");
		}

		this.whereClause = whereClause;
	}

	public Map<String, Object> getParametersAsMap() {

		Map<String, Object> params = new HashMap<String, Object>();
		List<Parameter> parameters = getParameters();
		for (Parameter parameter : parameters) {
			params.put(parameter.getName(), parameter.getValue());
		}
		return params;
	}

	public List<Parameter> getParameters() {
		List<Parameter> parameters = new ArrayList<Parameter>();

		parameters.addAll(whereClause.getParameters());

		for (Join join : joins) {
			parameters.addAll(join.getParams());
		}

		return parameters;
	}

	public <T> Parameter<T> generateParameter(Selector selector, T value) {
		return new Parameter<T>(selector.getName(), value);
	}

	public <T> Parameter<List<T>> generateParameter(Selector selector, List<T> value) {
		return new Parameter<List<T>>(selector.getName(), value);
	}

}
