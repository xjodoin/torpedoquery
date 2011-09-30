package com.netappsid.jpaquery.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyFactory.ClassLoaderProvider;

public class MultiClassLoaderProvider implements ClassLoaderProvider {

	private class MultiClassLoader extends ClassLoader {
		private final Collection<ClassLoader> classLoaders;

		public MultiClassLoader(Collection<ClassLoader> classLoaders) {
			this.classLoaders = classLoaders;
		}

		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {

			try {
				return MultiClassLoaderProvider.class.getClassLoader().loadClass(name);
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

	@Override
	public ClassLoader get(ProxyFactory factory) {
		Class[] interfaces = factory.getInterfaces();
		Set<ClassLoader> classLoaders = new HashSet<ClassLoader>();

		Class superclass = factory.getSuperclass();
		if (superclass != null && superclass.getClassLoader() != null) {
			classLoaders.add(superclass.getClassLoader());
		}

		if (interfaces != null) {
			for (Class clazz : interfaces) {
				classLoaders.add(clazz.getClassLoader());
			}
		}

		return new MultiClassLoader(classLoaders);
	}

}
