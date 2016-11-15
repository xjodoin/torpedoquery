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
package org.torpedoquery.jpa.internal.handlers;

import org.torpedoquery.jpa.Function;
public class SubstringFunctionHandler extends
		BaseFunctionHandler<String, Function<String>> {

	private final int beginIndex;
	private final int endIndex;

	/**
	 * <p>Constructor for SubstringFunctionHandler.</p>
	 *
	 * @param param a {@link java.lang.Object} object.
	 * @param beginIndex a int.
	 * @param endIndex a int.
	 */
	public SubstringFunctionHandler(Object param, int beginIndex, int endIndex) {
		super(param);
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
	}

	/** {@inheritDoc} */
	@Override
	protected String getFunctionFormat() {
		return "substring(%1$s," + beginIndex + "," + endIndex + ")";
	}

}
