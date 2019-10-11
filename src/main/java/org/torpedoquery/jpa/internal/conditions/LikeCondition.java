
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
 *
 * @author xjodoin
 * @version $Id: $Id
 */
package org.torpedoquery.jpa.internal.conditions;

import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.Selector;

public class LikeCondition<T> extends SingleParameterCondition<T> {

	public static enum Type {
		ANY {
			@Override
			public String wrap(String toMatch) {
				return "%" + toMatch + "%";
			}
		},
		STARTSWITH {
			@Override
			public String wrap(String toMatch) {
				return toMatch + "%";
			}
		},
		ENDSWITH {
			@Override
			public String wrap(String toMatch) {
				return "%" + toMatch;
			}
		},
		UNKNOW {
			@Override
			public String wrap(String toMatch) {
				return toMatch;
			}
		};

		public abstract String wrap(String toMatch);
	}

	/**
	 * <p>
	 * Constructor for LikeCondition.
	 * </p>
	 *
	 * @param type     a
	 *                 {@link org.torpedoquery.jpa.internal.conditions.LikeCondition.Type}
	 *                 object.
	 * @param selector a {@link org.torpedoquery.jpa.internal.Selector} object.
	 * @param toMatch  a {@link java.lang.String} object.
	 */
	public LikeCondition(Selector selector, Parameter<T> parameter) {
		super(selector, parameter);
	}

	@Override
	protected String getComparator() {
		return "like";
	}

}
