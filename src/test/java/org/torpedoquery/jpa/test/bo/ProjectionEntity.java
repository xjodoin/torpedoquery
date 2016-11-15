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
package org.torpedoquery.jpa.test.bo;
public class ProjectionEntity {

	private String test1;
	private Integer test2;

	/**
	 * <p>Constructor for ProjectionEntity.</p>
	 *
	 * @param test1 a {@link java.lang.String} object.
	 * @param test2 a {@link java.lang.Integer} object.
	 */
	public ProjectionEntity(String test1, Integer test2) {
		this.setTest1(test1);
		this.setTest2(test2);
	}

	/**
	 * <p>Getter for the field <code>test1</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTest1() {
		return test1;
	}

	/**
	 * <p>Setter for the field <code>test1</code>.</p>
	 *
	 * @param test1 a {@link java.lang.String} object.
	 */
	public void setTest1(String test1) {
		this.test1 = test1;
	}

	/**
	 * <p>Getter for the field <code>test2</code>.</p>
	 *
	 * @return a {@link java.lang.Integer} object.
	 */
	public Integer getTest2() {
		return test2;
	}

	/**
	 * <p>Setter for the field <code>test2</code>.</p>
	 *
	 * @param test2 a {@link java.lang.Integer} object.
	 */
	public void setTest2(Integer test2) {
		this.test2 = test2;
	}

}
