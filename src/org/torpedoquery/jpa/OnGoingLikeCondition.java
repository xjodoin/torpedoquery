package org.torpedoquery.jpa;

public interface OnGoingLikeCondition {

	OnGoingLogicalCondition any(String toMatch);

	OnGoingLogicalCondition startsWith(String toMatch);

	OnGoingLogicalCondition endsWith(String toMatch);

}
