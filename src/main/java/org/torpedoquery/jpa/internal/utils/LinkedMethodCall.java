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

import org.torpedoquery.jpa.internal.MethodCall;
import org.torpedoquery.jpa.internal.TorpedoProxy;
public class LinkedMethodCall implements MethodCall {

	private final MethodCall previous;
	private final MethodCall current;

	/**
	 * <p>Constructor for LinkedMethodCall.</p>
	 *
	 * @param previous a {@link org.torpedoquery.jpa.internal.MethodCall} object.
	 * @param current a {@link org.torpedoquery.jpa.internal.MethodCall} object.
	 */
	public LinkedMethodCall(MethodCall previous, MethodCall current) {
		this.previous = previous;
		this.current = current;
	}

	/** {@inheritDoc} */
	@Override
	public TorpedoProxy getProxy() {
		return previous.getProxy();
	}

	/** {@inheritDoc} */
	@Override
	public SerializableMethod getMethod() {
		return current.getMethod();
	}

	/** {@inheritDoc} */
	@Override
	public String getFullPath() {
		return previous.getFullPath() + "." + current.getFullPath();
	}

	/** {@inheritDoc} */
	@Override
	public String getParamName() {
		return current.getParamName();
	}

}
