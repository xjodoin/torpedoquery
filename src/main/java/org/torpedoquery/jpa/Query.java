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
package org.torpedoquery.jpa;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

/**
 * 
 * Query is the result of the Torpedo.select() You can retreive the data with
 * methods get and list or access to HQL String with getQuery and the related
 * parameters with getParameters()
 * 
 */
public interface Query<T> {

	String getQuery();

	Map<String, Object> getParameters();

	/**
	 * 
	 * Retrieve the query data and apply a transformation function on each
	 * elements
	 * 
	 * @param entityManager
	 * @param function
	 * @return
	 */
	<E> List<E> map(EntityManager entityManager, PostFunction<E, T> function);

	/**
	 * 
	 * Use only when your query is suppose to return only one element
	 * 
	 * @param entityManager
	 * @return
	 */
	T get(EntityManager entityManager);

	/**
	 * 
	 * Execute and return your query data
	 * 
	 * @param entityManager
	 * @return
	 */
	List<T> list(EntityManager entityManager);

	/**
	 * 
	 * Set the position of the first result to retrieve.
	 * 
	 * @param startPosition
	 *            - position of the first result, numbered from 0
	 * @return the same query instance
	 */
	Query<T> setFirstResult(int startPosition);

	/**
	 * 
	 * Set the maximum number of results to retrieve.
	 * 
	 * @param maxResult
	 *            - maximum number of results to retrieve
	 * @return the same query instance
	 */
	Query<T> setMaxResults(int maxResult);

}
