package org.torpedoquery.jpa.test.bo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Entity {
	private Date dateField;

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

	public Date getDateField() {
		return dateField;
	}

	public Map<String, SubEntity> getSubEntityMap() {
		return null;
	}
}
