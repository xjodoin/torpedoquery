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

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.torpedoquery.jpa.internal.TorpedoMagic;
import org.torpedoquery.jpa.internal.TorpedoProxy;
import org.torpedoquery.jpa.internal.query.DefaultQueryBuilder;
import org.torpedoquery.jpa.test.bo.Entity;
import org.torpedoquery.jpa.test.bo.ExtendEntity;
public class ProxyFactoryFactoryTest {

	/**
	 * <p>test_dont_track_finalize.</p>
	 *
	 * @throws java.lang.NoSuchMethodException if any.
	 * @throws java.lang.SecurityException if any.
	 * @throws java.lang.IllegalAccessException if any.
	 * @throws java.lang.IllegalArgumentException if any.
	 * @throws java.lang.reflect.InvocationTargetException if any.
	 */
	@Test
	public void test_dont_track_finalize() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		ProxyFactoryFactory proxyFactoryFactory = new ProxyFactoryFactory(new MultiClassLoaderProvider());
		TorpedoMethodHandler torpedoMethodHandler = new TorpedoMethodHandler(new DefaultQueryBuilder<Entity>(Entity.class));
		Entity createProxy = proxyFactoryFactory.createProxy(torpedoMethodHandler, Entity.class, TorpedoProxy.class);
		Method method = Object.class.getDeclaredMethod("finalize");
		method.setAccessible(true);
		method.invoke(createProxy);

		assertTrue(torpedoMethodHandler.getMethods().isEmpty());
	}

	/**
	 * <p>test_dont_track_finalize_when_extends.</p>
	 *
	 * @throws java.lang.NoSuchMethodException if any.
	 * @throws java.lang.SecurityException if any.
	 * @throws java.lang.IllegalAccessException if any.
	 * @throws java.lang.IllegalArgumentException if any.
	 * @throws java.lang.reflect.InvocationTargetException if any.
	 */
	@Test
	public void test_dont_track_finalize_when_extends() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		ProxyFactoryFactory proxyFactoryFactory = TorpedoMagic.getProxyfactoryfactory();
		TorpedoMethodHandler torpedoMethodHandler = new TorpedoMethodHandler(new DefaultQueryBuilder<ExtendEntity>(ExtendEntity.class));
		ExtendEntity createProxy = proxyFactoryFactory.createProxy(torpedoMethodHandler, ExtendEntity.class, TorpedoProxy.class);
		Method method = Object.class.getDeclaredMethod("finalize");
		method.setAccessible(true);
		method.invoke(createProxy);

		assertTrue(torpedoMethodHandler.getMethods().isEmpty());
	}
	
	/**
	 * <p>test_factoryMustUseClassCache.</p>
	 */
	@Test
	public void test_factoryMustUseClassCache()
	{
		ProxyFactoryFactory proxyFactoryFactory = TorpedoMagic.getProxyfactoryfactory();
		TorpedoMethodHandler torpedoMethodHandler = new TorpedoMethodHandler(new DefaultQueryBuilder<Entity>(Entity.class));
		Entity createProxy = proxyFactoryFactory.createProxy(torpedoMethodHandler, Entity.class, TorpedoProxy.class);
		
		Entity createProxy2 = proxyFactoryFactory.createProxy(torpedoMethodHandler, Entity.class, TorpedoProxy.class);
		
		assertSame(createProxy.getClass(), createProxy2.getClass());
	}
	

}
