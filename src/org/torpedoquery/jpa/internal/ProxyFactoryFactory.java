package org.torpedoquery.jpa.internal;

import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyFactory.ClassLoaderProvider;

public class ProxyFactoryFactory {
	private final ClassLoaderProvider classLoaderProvider;

	public ProxyFactoryFactory(ClassLoaderProvider classLoaderProvider) {
		this.classLoaderProvider = classLoaderProvider;
	}

	public ProxyFactory getProxyFactory() {
		return new ClassLoaderProvidedProxyFactory(classLoaderProvider);
	}
}
