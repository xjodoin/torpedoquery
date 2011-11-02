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
package org.torpedoquery.jpa.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;

import javassist.util.proxy.MethodHandler;

import org.torpedoquery.jpa.Query;

public class TorpedoMethodHandler implements MethodHandler, Proxy
{
	private final Map<Object, QueryBuilder<?>> proxyQueryBuilders = new IdentityHashMap<Object, QueryBuilder<?>>();
	private final Deque<MethodCall> methods = new LinkedList<MethodCall>();
	private final QueryBuilder<?> root;
	private final ProxyFactoryFactory proxyfactoryfactory;

	public TorpedoMethodHandler(QueryBuilder<?> root, ProxyFactoryFactory proxyfactoryfactory)
	{
		this.root = root;
		this.proxyfactoryfactory = proxyfactoryfactory;
	}

	public QueryBuilder<?> addQueryBuilder(Object proxy, QueryBuilder<?> queryBuilder)
	{
		proxyQueryBuilders.put(proxy, queryBuilder);
		return queryBuilder;
	}

	@Override
	public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable
	{
		if (thisMethod.getDeclaringClass().equals(Proxy.class))
		{
			try
			{
				return thisMethod.invoke(this, args);
			}
			catch (InvocationTargetException e)
			{
				throw e.getTargetException();
			}
		}

		methods.addFirst(new SimpleMethodCall((Proxy) self, thisMethod));
		TorpedoMagic.setQuery((Proxy) self);

		final Class returnType = thisMethod.getReturnType();

		if (returnType.isPrimitive())
		{
			return returnType.isAssignableFrom(boolean.class) ? false : 0;
		}
		else if (!Modifier.isFinal(returnType.getModifiers()))
		{
			final Object proxy = createLinkedProxy(returnType);
			return proxy;
		}
		else
		{
			return null;
		}
	}

	private Object createLinkedProxy(final Class returnType) throws NoSuchMethodException, InstantiationException, IllegalAccessException,
			InvocationTargetException
	{
		MethodHandler mh = new MethodHandler()
		{

			@Override
			public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable
			{
				MethodCall pollFirst = methods.pollFirst();
				LinkedMethodCall linkedMethodCall = new LinkedMethodCall(pollFirst, new SimpleMethodCall(pollFirst.getProxy(), thisMethod));
				methods.addFirst(linkedMethodCall);
				Class<?> returnType2 = thisMethod.getReturnType();

				if (returnType2.isPrimitive())
				{
					return returnType2.isAssignableFrom(boolean.class) ? false : 0;
				}
				else if (!Modifier.isFinal(returnType2.getModifiers()))
				{
					final Object proxy = createLinkedProxy(returnType2);
					return proxy;
				}
				else
				{
					return null;
				}
			}
		};


		return proxyfactoryfactory.createProxy(mh,returnType);
	}

	public <T> T handle(QueryHandler<T> handler)
	{
		final T result = handler.handleCall(proxyQueryBuilders, methods);
		return result;
	}

	public QueryBuilder<?> getQueryBuilder(Object proxy)
	{
		return proxyQueryBuilders.get(proxy);
	}

	public <T extends Query> T getRoot()
	{
		return (T) root;
	}

	@Override
	public TorpedoMethodHandler getTorpedoMethodHandler()
	{
		return this;
	}

	public Deque<MethodCall> getMethods()
	{
		return methods;
	}

}
