package com.netappsid.jpaquery.internal;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class QueryBuilder {
	private final Class<?> toQuery;
	private final List<Selector> toSelect = new ArrayList<Selector>();
	private final List<Join> joins = new ArrayList<Join>();
	private final List<WhereClause<?>> whereClauses = new ArrayList<WhereClause<?>>();

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

		builder.append(appendWhereClause(new StringBuilder(), incrementor));

		return builder.toString().trim();
	}

	public StringBuilder appendWhereClause(StringBuilder builder, AtomicInteger incrementor) {

		for (WhereClause<?> clause : whereClauses) {
			if (builder.length() == 0) {
				builder.append(" where ").append(clause.createQueryFragment(this, incrementor)).append(" ");
			} else {
				builder.append("and ").append(clause.createQueryFragment(this, incrementor)).append(" ");
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

	public String getFieldName(Method method) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(toQuery);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				if (propertyDescriptor.getReadMethod() != null && propertyDescriptor.getReadMethod().equals(method)) {
					return propertyDescriptor.getName();
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return null;
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

	public void addWhereClause(WhereClause<?> whereClause) {
		whereClauses.add(whereClause);
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

		for (WhereClause whereClause : whereClauses) {
			parameters.addAll(whereClause.getParameters());
		}

		for (Join join : joins) {
			parameters.addAll(join.getParams());
		}

		return parameters;
	}

	public <T> Parameter<T> generateParameter(Method method, T value) {
		return new Parameter<T>(getFieldName(method), value);
	}

	public <T> Parameter<List<T>> generateParameter(Method method, List<T> value) {
		return new Parameter<List<T>>(getFieldName(method), value);
	}

}
