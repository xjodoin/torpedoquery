/**
 * Copyright (C) 2011 Xavier Jodoin (xavier@jodoin.me)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.torpedoquery.jpa.internal.utils;

import java.lang.reflect.Method;

import org.torpedoquery.jpa.internal.MethodCall;
import org.torpedoquery.jpa.internal.TorpedoProxy;
public class SimpleMethodCall implements MethodCall {
	private final TorpedoProxy proxy;
	private final SerializableMethod method;

	/**
	 * <p>Constructor for SimpleMethodCall.</p>
	 *
	 * @param proxy a {@link org.torpedoquery.jpa.internal.TorpedoProxy} object.
	 * @param method a {@link java.lang.reflect.Method} object.
	 */
	public SimpleMethodCall(TorpedoProxy proxy, Method method) {
		this.proxy = proxy;
		this.method = new SerializableMethod(method);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.torpedoquery.jpa.internal.MethodCallTmp#getProxy()
	 */
	/** {@inheritDoc} */
	@Override
	public TorpedoProxy getProxy() {
		return proxy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.torpedoquery.jpa.internal.MethodCallTmp#getMethod()
	 */
	/** {@inheritDoc} */
	@Override
	public SerializableMethod getMethod() {
		return method;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return method.toString();
	}

	/** {@inheritDoc} */
	@Override
	public String getFullPath() {
		return FieldUtils.getFieldName(method);
	}

	/** {@inheritDoc} */
	@Override
	public String getParamName() {
		return FieldUtils.getFieldName(method);
	}

}
