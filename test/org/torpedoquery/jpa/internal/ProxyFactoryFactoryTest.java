package org.torpedoquery.jpa.internal;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.torpedoquery.jpa.test.bo.Entity;
import org.torpedoquery.jpa.test.bo.ExtendEntity;

public class ProxyFactoryFactoryTest {

	@Test
	public void test_dont_track_finalize() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		ProxyFactoryFactory proxyFactoryFactory = new ProxyFactoryFactory(new MultiClassLoaderProvider());
		TorpedoMethodHandler torpedoMethodHandler = new TorpedoMethodHandler(new QueryBuilder<Entity>(Entity.class), proxyFactoryFactory);
		Entity createProxy = proxyFactoryFactory.createProxy(torpedoMethodHandler, Entity.class, Proxy.class);
		Method method = Object.class.getDeclaredMethod("finalize");
		method.setAccessible(true);
		method.invoke(createProxy);

		assertTrue(torpedoMethodHandler.getMethods().isEmpty());
	}

	@Test
	public void test_dont_track_finalize_when_extends() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		ProxyFactoryFactory proxyFactoryFactory = new ProxyFactoryFactory(new MultiClassLoaderProvider());
		TorpedoMethodHandler torpedoMethodHandler = new TorpedoMethodHandler(new QueryBuilder<ExtendEntity>(ExtendEntity.class), proxyFactoryFactory);
		ExtendEntity createProxy = proxyFactoryFactory.createProxy(torpedoMethodHandler, ExtendEntity.class, Proxy.class);
		Method method = Object.class.getDeclaredMethod("finalize");
		method.setAccessible(true);
		method.invoke(createProxy);

		assertTrue(torpedoMethodHandler.getMethods().isEmpty());
	}
	
	@Test
	public void test_factoryMustUseClassCache()
	{
		ProxyFactoryFactory proxyFactoryFactory = new ProxyFactoryFactory(new MultiClassLoaderProvider());
		TorpedoMethodHandler torpedoMethodHandler = new TorpedoMethodHandler(new QueryBuilder<Entity>(Entity.class), proxyFactoryFactory);
		Entity createProxy = proxyFactoryFactory.createProxy(torpedoMethodHandler, Entity.class, Proxy.class);
		
		Entity createProxy2 = proxyFactoryFactory.createProxy(torpedoMethodHandler, Entity.class, Proxy.class);
		
		assertSame(createProxy.getClass(), createProxy2.getClass());
	}
	

}
