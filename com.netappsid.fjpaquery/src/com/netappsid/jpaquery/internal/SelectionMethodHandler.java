package com.netappsid.jpaquery.internal;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyObject;

public class SelectionMethodHandler implements MethodHandler {
    private final DefaultMethodHandler defaultMethodHandler;
    private final List<Tuple<Object, String>> selectedProperties = new ArrayList<Tuple<Object, String>>();

    public SelectionMethodHandler(DefaultMethodHandler defaultMethodHandler) {
	this.defaultMethodHandler = defaultMethodHandler;
    }
    
    public List<Tuple<Object, String>> getSelectedProperties() {
	return selectedProperties;
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
	((ProxyObject) self).setHandler(defaultMethodHandler);

	final BeanInfo beanInfo = Introspector.getBeanInfo(thisMethod.getDeclaringClass());

	for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
	    if (thisMethod.equals(propertyDescriptor.getReadMethod())) {
		selectedProperties.add(new Tuple(self, propertyDescriptor.getName()));
	    }
	}

	return defaultMethodHandler.invoke(self, thisMethod, proceed, args);
    }
}
