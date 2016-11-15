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
import java.util.ArrayList;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory.ClassLoaderProvider;

import org.objenesis.ObjenesisHelper;
public class ProxyFactoryFactory {
	private final ClassLoaderProvider classLoaderProvider;
	private final MethodFilter methodFilter;

	/**
	 * <p>Constructor for ProxyFactoryFactory.</p>
	 *
	 * @param classLoaderProvider a {@link javassist.util.proxy.ProxyFactory.ClassLoaderProvider} object.
	 */
	public ProxyFactoryFactory(ClassLoaderProvider classLoaderProvider) {
		this.classLoaderProvider = classLoaderProvider;
		methodFilter = new MethodFilter() {
			@Override
			public boolean isHandled(Method m) {
				return !m.getDeclaringClass().equals(Object.class)
						&& !m.getName().equals("finalize")
						&& !m.getName().equals("equals")
						&& !m.getName().equals("hashCode")
						&& !m.getName().equals("toString");
			}
		};
	}

	/**
	 * You can pass only one Super class
	 *
	 * @param classes a {@link java.lang.Class} object.
	 * @param methodHandler a {@link javassist.util.proxy.MethodHandler} object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	public <T> T createProxy(MethodHandler methodHandler, Class<?>... classes) {

		ArrayList<Class<?>> interfaces = new ArrayList<>();
		Class<?> superClass = null;

		for (Class<?> class1 : classes) {

			if (class1.isInterface()) {
				interfaces.add(class1);
			} else {
				if (superClass != null) {
					throw new IllegalArgumentException(
							"You only can pass one super class other can be interface");
				} else {
					superClass = class1;
				}
			}
		}

		ClassLoaderProvidedProxyFactory classLoaderProvidedProxyFactory = new ClassLoaderProvidedProxyFactory(
				classLoaderProvider);

		if (superClass != null) {
			classLoaderProvidedProxyFactory.setSuperclass(superClass);
		}

		if (!interfaces.isEmpty()) {
			classLoaderProvidedProxyFactory.setInterfaces(interfaces
					.toArray(new Class[0]));
		}

		classLoaderProvidedProxyFactory.setFilter(methodFilter);

		Class proxyClass = classLoaderProvidedProxyFactory.createClass();

		Proxy proxy = (Proxy) ObjenesisHelper.newInstance(proxyClass);
		proxy.setHandler(methodHandler);

		return (T) proxy;
	}
}
