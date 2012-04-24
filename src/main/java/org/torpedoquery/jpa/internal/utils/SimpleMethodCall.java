/**
 *
 *   Copyright 2011 Xavier Jodoin xjodoin@gmail.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.torpedoquery.jpa.internal.utils;

import java.lang.reflect.Method;

import org.torpedoquery.jpa.internal.MethodCall;
import org.torpedoquery.jpa.internal.Proxy;

public class SimpleMethodCall implements MethodCall {
	private final Proxy proxy;
	private final Method method;

	public SimpleMethodCall(Proxy proxy, Method method) {
		this.proxy = proxy;
		this.method = method;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.torpedoquery.jpa.internal.MethodCallTmp#getProxy()
	 */
	@Override
	public Proxy getProxy() {
		return proxy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.torpedoquery.jpa.internal.MethodCallTmp#getMethod()
	 */
	@Override
	public Method getMethod() {
		return method;
	}

	@Override
	public String toString() {
		return method.toString();
	}

	@Override
	public String getFullPath() {
		return FieldUtils.getFieldName(method);
	}

	@Override
	public String getParamName() {
		return FieldUtils.getFieldName(method);
	}

}