/**
 *   Copyright Xavier Jodoin xjodoin@torpedoquery.org
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
package org.torpedoquery.jpa.test.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Entity extends AbstractEntity {
	private Date dateField;
	
	public String getId(){
		return null;
	}

	public String getCode() {
		return null;
	}

	public String getName() {
		return null;
	}

	public boolean isActive() {
		return false;
	}

	public SubEntity getSubEntity() {
		return null;
	}

	public List<SubEntity> getSubEntities() {
		return null;
	}

	public Integer getIntegerField() {
		return null;
	}

	public int getPrimitiveInt() {
		return 0;
	}
	
	public Serializable getInterface() {
		return null;
	}

	public Date getDateField() {
		return dateField;
	}

	public Map<String, SubEntity> getSubEntityMap() {
		return null;
	}

	public BigDecimal getBigDecimalField() {
		return null;
	}

	public BigDecimal getBigDecimalField2() {
		return null;
	}

	@Override
	public ExtendEntity getAbstractEntity() {
		return null;
	}

	public char getSmallChar() {
		return 's';
	}
}
