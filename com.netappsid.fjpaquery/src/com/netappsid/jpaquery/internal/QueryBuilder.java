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
    private List<Method> toSelect = new ArrayList<Method>();
    private List<InnerJoin> joins = new ArrayList<InnerJoin>();
    private List<WhereClause<?>> whereClauses = new ArrayList<WhereClause<?>>();

    private String alias;
    private AtomicInteger increment;

    public QueryBuilder(Class<?> toQuery, AtomicInteger increment) {
	this.toQuery = toQuery;
	this.increment = increment;
    }

    public String getQuery() {

	String from = " from " + toQuery.getSimpleName() + " " + getAlias();
	StringBuilder builder = new StringBuilder();

	appendSelect(builder);

	builder.append(from);

	builder.append(getJoins());

	builder.append(appendWhereClause(new StringBuilder()));

	return builder.toString().trim();
    }

    public StringBuilder appendWhereClause(StringBuilder builder) {

	for (WhereClause<?> clause : whereClauses) {
	    if (builder.length() == 0) {
		builder.append(" where ").append(clause.getCondition()).append(" ");
	    } else {
		builder.append(", ").append(clause.getCondition()).append(" ");
	    }
	}

	for (InnerJoin join : joins) {
	    join.appendWhereClause(builder);
	}

	return builder;
    }

    public void appendSelect(StringBuilder builder) {
	for (Method method : toSelect) {
	    if (builder.length() == 0) {
		builder.append("select ").append(getAlias()).append(".").append(getFieldName(method));
	    } else {
		builder.append(", ").append(getAlias()).append(".").append(getFieldName(method));
	    }
	}

	for (InnerJoin join : joins) {
	    join.appendSelect(builder);
	}
    }

    public String getFieldName(Method method) {
	try {
	    BeanInfo beanInfo = Introspector.getBeanInfo(toQuery);
	    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
	    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
		if (propertyDescriptor.getReadMethod() !=null && propertyDescriptor.getReadMethod().equals(method)) {
		    return propertyDescriptor.getName();
		}
	    }
	} catch (IntrospectionException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public String getAlias() {
	if (alias == null) {
	    final char[] charArray = toQuery.getSimpleName().toCharArray();

	    charArray[0] = Character.toLowerCase(charArray[0]);
	    alias = new String(charArray) + "_" + increment.getAndIncrement();
	}
	return alias;
    }

    public void addSelector(Method thisMethod) {
	toSelect.add(thisMethod);
    }

    public void addJoin(InnerJoin innerJoin) {
	joins.add(innerJoin);
    }

    public boolean hasSubJoin() {
	return !joins.isEmpty();
    }

    public String getJoins() {

	StringBuilder builder = new StringBuilder();

	for (InnerJoin join : joins) {
	    builder.append(join.getJoin(getAlias()));
	}

	return builder.toString();
    }

    public AtomicInteger getIncrement() {
	return increment;
    }

    public void addWhereClause(WhereClause<?> whereClause) {
	whereClauses.add(whereClause);
    }

    public String generateVariable(String fieldName) {
	return fieldName + "_" + increment.getAndIncrement();
    }

    public Map<String, Object> getParams() {

	Map<String, Object> params = new HashMap<String, Object>();

	for (WhereClause<?> whereClause : whereClauses) {
	    params.put(whereClause.getVariableName(), whereClause.getValue());
	}

	for (InnerJoin join : joins) {
	    params.putAll(join.getParams());
	}

	return params;
    }
}
