package com.netappsid.jpaquery;

import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import com.netappsid.jpaquery.internal.DefaultMethodHandler;
import com.netappsid.jpaquery.internal.SelectionMethodHandler;
import com.netappsid.jpaquery.internal.Tuple;

public class JPAQuery<T> {
    private final DefaultMethodHandler defaultMethodHandler = new DefaultMethodHandler();
    private final SelectionMethodHandler selectionMethodHandler = new SelectionMethodHandler(defaultMethodHandler);
    private final Class aliasType;

    public JPAQuery(Class<T> fromType) {
	this.aliasType = fromType;
    }

    public T getAlias() {
	try {
	    final ProxyFactory proxyFactory = new ProxyFactory();

	    proxyFactory.setSuperclass(aliasType);
	    return (T) proxyFactory.create(null, null, null);
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public <A> A getAlias(A alias) {
	return alias;
    }

    public <A> A select(A alias) {
	((ProxyObject) alias).setHandler(selectionMethodHandler);
	return alias;
    }

    public String getQuery() {
	final StringBuilder builder = new StringBuilder();

	for (int i = 0; i < selectionMethodHandler.getSelectedProperties().size(); i++) {
	    final Tuple<Object, String> selectedProperty = selectionMethodHandler.getSelectedProperties().get(i);
	    final String separator = i < selectionMethodHandler.getSelectedProperties().size() - 1 ? ", " : " ";
	    builder.append(getAliasName()).append(".").append(selectedProperty._2).append(separator);
	}

	if (builder.length() != 0) {
	    builder.insert(0, "select ");
	}

	builder.append("from ").append(getEntityName()).append(" as ").append(getAliasName());

	return builder.toString();
    }
    
    private String getAliasName() {
	final String entityName = getEntityName();
	return new String(new char[] { entityName.charAt(0) }).toLowerCase() + entityName.substring(1);
    }
    
    private String getEntityName() {
	return aliasType.getSimpleName();
    }
}
