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

import java.beans.Introspector;
public final class FieldUtils {

	private FieldUtils() {
	}

	/**
	 * <p>getFieldName.</p>
	 *
	 * @param method a {@link org.torpedoquery.jpa.internal.utils.SerializableMethod} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getFieldName(SerializableMethod method) {
		
		String name = method.getName();
		
		if(name.startsWith("get"))
		{
			name = name.substring(3);
		}
		else if(name.startsWith("is"))
		{
			name = name.substring(2);
		}
		
		name = Introspector.decapitalize(name);
			
		return name;
	}
}
