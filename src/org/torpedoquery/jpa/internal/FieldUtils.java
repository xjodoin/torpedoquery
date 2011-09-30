package org.torpedoquery.jpa.internal;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class FieldUtils {
	public static String getFieldName(Method method) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(method.getDeclaringClass());
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
}
