package org.torpedoquery.jpa.internal;

public class AndCondition extends LogicalElement {

	public AndCondition(Condition left, Condition right) {
		super(left, right);
	}

	@Override
	protected String getCondition() {
		return " and ";
	}

}
