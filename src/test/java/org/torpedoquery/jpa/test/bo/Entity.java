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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
public class Entity extends AbstractEntity {
	private Date dateField;
	
	/**
	 * <p>getId.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getId(){
		return null;
	}

	/**
	 * <p>getCode.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCode() {
		return null;
	}

	/**
	 * <p>getName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return null;
	}

	/**
	 * <p>isActive.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isActive() {
		return false;
	}

	/**
	 * <p>getSubEntity.</p>
	 *
	 * @return a {@link org.torpedoquery.jpa.test.bo.SubEntity} object.
	 */
	public SubEntity getSubEntity() {
		return null;
	}

	/**
	 * <p>getSubEntities.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<SubEntity> getSubEntities() {
		return null;
	}

	/**
	 * <p>getIntegerField.</p>
	 *
	 * @return a {@link java.lang.Integer} object.
	 */
	public Integer getIntegerField() {
		return null;
	}

	/**
	 * <p>getPrimitiveInt.</p>
	 *
	 * @return a int.
	 */
	public int getPrimitiveInt() {
		return 0;
	}
	
	/**
	 * <p>getPrimitiveLong.</p>
	 *
	 * @return a long.
	 */
	public long getPrimitiveLong() {
		return 1L;
	}
	
	/**
	 * <p>getInterface.</p>
	 *
	 * @return a {@link java.io.Serializable} object.
	 */
	public Serializable getInterface() {
		return null;
	}

	/**
	 * <p>Getter for the field <code>dateField</code>.</p>
	 *
	 * @return a {@link java.util.Date} object.
	 */
	public Date getDateField() {
		return dateField;
	}

	/**
	 * <p>getSubEntityMap.</p>
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String, SubEntity> getSubEntityMap() {
		return null;
	}

	public List<String> getValueCollection() {
		return null;
	}

	/**
	 * <p>getBigDecimalField.</p>
	 *
	 * @return a {@link java.math.BigDecimal} object.
	 */
	public BigDecimal getBigDecimalField() {
		return null;
	}

	/**
	 * <p>getBigDecimalField2.</p>
	 *
	 * @return a {@link java.math.BigDecimal} object.
	 */
	public BigDecimal getBigDecimalField2() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public ExtendEntity getAbstractEntity() {
		return null;
	}

	/**
	 * <p>getSmallChar.</p>
	 *
	 * @return a char.
	 */
	public char getSmallChar() {
		return 's';
	}
}
