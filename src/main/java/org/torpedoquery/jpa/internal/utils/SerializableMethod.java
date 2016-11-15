package org.torpedoquery.jpa.internal.utils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import com.google.common.base.Throwables;

public class SerializableMethod implements Serializable {

	private static final long serialVersionUID = 6005610965006048445L;

	private final Class<?> declaringClass;
	private final String methodName;
	private final Class<?>[] parameterTypes;
	private final Class<?> returnType;
	private final Class<?>[] exceptionTypes;
	private final boolean isVarArgs;
	private final boolean isAbstract;

	public SerializableMethod(Method method) {
		declaringClass = method.getDeclaringClass();
		methodName = method.getName();
		parameterTypes = method.getParameterTypes();
		returnType = method.getReturnType();
		exceptionTypes = method.getExceptionTypes();
		isVarArgs = method.isVarArgs();
		isAbstract = (method.getModifiers() & Modifier.ABSTRACT) != 0;
	}

	public String getName() {
		return methodName;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public Class<?>[] getExceptionTypes() {
		return exceptionTypes;
	}

	public boolean isVarArgs() {
		return isVarArgs;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public Method getJavaMethod() {
		try {
			return declaringClass.getDeclaredMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			throw Throwables.propagate(e);
		}
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SerializableMethod other = (SerializableMethod) obj;
		if (declaringClass == null) {
			if (other.declaringClass != null)
				return false;
		} else if (!declaringClass.equals(other.declaringClass))
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		if (!Arrays.equals(parameterTypes, other.parameterTypes))
			return false;
		if (returnType == null) {
			if (other.returnType != null)
				return false;
		} else if (!returnType.equals(other.returnType))
			return false;
		return true;
	}
}