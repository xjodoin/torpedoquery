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

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyFactory.ClassLoaderProvider;
public class MultiClassLoaderProvider implements ClassLoaderProvider {

	private static final Map<Set<ClassLoader>, ClassLoader> classLoaderCache = new ConcurrentHashMap<>();
	
	private class MultiClassLoader extends ClassLoader {
		private final Collection<ClassLoader> classLoaders;

		public MultiClassLoader(Collection<ClassLoader> classLoaders) {
			this.classLoaders = classLoaders;
		}

		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {

			try {
				return Thread.currentThread().getContextClassLoader().loadClass(name);
			} catch (ClassNotFoundException e1) {
			}

			for (ClassLoader classLoader : classLoaders) {
				try {
					Class<?> loadClass = classLoader.loadClass(name);
					if (loadClass != null) {
						return loadClass;
					}
				} catch (ClassNotFoundException e) {
				}
			}
			throw new ClassNotFoundException(name);
		}
	}

	/** {@inheritDoc} */
	@Override
	public ClassLoader get(ProxyFactory factory) {
		Class[] interfaces = factory.getInterfaces();
		Set<ClassLoader> classLoaders = new HashSet<>();

		Class superclass = factory.getSuperclass();
		if (superclass != null && superclass.getClassLoader() != null) {
			classLoaders.add(superclass.getClassLoader());
		}

		if (interfaces != null) {
			for (Class clazz : interfaces) {
				classLoaders.add(clazz.getClassLoader());
			}
		}
		
		ClassLoader multiClassLoader;
		
		if(classLoaderCache.containsKey(classLoaders))
		{
			multiClassLoader = classLoaderCache.get(classLoaders);
		}
		else
		{
			multiClassLoader = new MultiClassLoader(classLoaders);
			classLoaderCache.put(classLoaders, multiClassLoader);
		}

		return multiClassLoader;
	}

}
