package com.netappsid.jpaquery.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.Query;

public class QueryBuilder implements Query<Object> {
	private final Class<?> toQuery;
	private final List<Selector> toSelect = new ArrayList<Selector>();
	private final List<Join> joins = new ArrayList<Join>();
	private WhereClause<?> whereClause;

	private String alias;
	private OrderBy orderBy;
	private GroupBy groupBy;

	public QueryBuilder(Class<?> toQuery) {
		this.toQuery = toQuery;
	}

	public String getQuery(AtomicInteger incrementor) {

		String from = " from " + toQuery.getSimpleName() + " " + getAlias(incrementor);
		StringBuilder builder = new StringBuilder();

		appendSelect(builder, incrementor);

		builder.append(from);

		builder.append(getJoins(incrementor));

		builder.append(appendWhereClause(new StringBuilder(), incrementor));

		builder.append(appendOrderBy(new StringBuilder(), incrementor));

		builder.append(appendGroupBy(new StringBuilder(), incrementor));

		return builder.toString().trim();
	}

	@Override
	public String getQuery() {
		return getQuery(new AtomicInteger());
	}

	public String appendOrderBy(StringBuilder builder, AtomicInteger incrementor) {

		if (orderBy != null) {
			orderBy.createQueryFragment(builder, this, incrementor);
		}

		for (Join join : joins) {
			join.appendOrderBy(builder, incrementor);
		}

		return builder.toString();
	}

	public String appendGroupBy(StringBuilder builder, AtomicInteger incrementor) {

		if (groupBy != null) {
			groupBy.createQueryFragment(builder, this, incrementor);
		}

		for (Join join : joins) {
			join.appendGroupBy(builder, incrementor);
		}

		return builder.toString();
	}

	public StringBuilder appendWhereClause(StringBuilder builder, AtomicInteger incrementor) {

		Condition whereClauseCondition = getWhereClause();

		if (whereClauseCondition != null) {
			if (builder.length() == 0) {
				builder.append(" where ").append(whereClauseCondition.createQueryFragment(this, incrementor)).append(" ");
			} else {
				builder.append("and ").append(whereClauseCondition.createQueryFragment(this, incrementor)).append(" ");
			}
		}

		for (Join join : joins) {
			join.appendWhereClause(builder, incrementor);
		}

		return builder;
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

	public Condition getWhereClause() {
		if (whereClause != null) {
			return whereClause.getLogicalCondition() != null ? whereClause.getLogicalCondition() : whereClause;
		}
		return null;
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

		Condition whereClauseCondition = getWhereClause();

		if (whereClauseCondition != null) {
			parameters.addAll(whereClauseCondition.getParameters());
		}

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

	public void addOrder(Selector selector) {
		if (orderBy == null) {
			orderBy = new OrderBy();
		}

		orderBy.addOrder(selector);

	}

	public void addGroupBy(Selector selector) {
		if (groupBy == null) {
			groupBy = new GroupBy();
		}

		groupBy.addGroup(selector);
	}

}
