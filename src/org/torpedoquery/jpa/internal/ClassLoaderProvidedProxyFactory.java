package org.torpedoquery.jpa.internal;

import javassist.util.proxy.ProxyFactory;

public class ClassLoaderProvidedProxyFactory extends ProxyFactory {
	private final ClassLoaderProvider classLoaderProvider;

	public ClassLoaderProvidedProxyFactory(ClassLoaderProvider classLoaderProvider) {
		this.classLoaderProvider = classLoaderProvider;
	}

	@Override
	protected ClassLoader getClassLoader() {
		return classLoaderProvider.get(this);
	}
}
