package org.torpedoquery.jpa.internal;

public class OrCondition extends LogicalElement {

	public OrCondition(Condition left, Condition right) {
		super(left, right);
	}

	@Override
	protected String getCondition() {
		return " or ";
	}
}
