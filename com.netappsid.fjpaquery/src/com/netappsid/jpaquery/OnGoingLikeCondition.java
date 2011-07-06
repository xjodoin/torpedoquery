package com.netappsid.jpaquery;

public interface OnGoingLikeCondition {

	OnGoingLogicalCondition any(String toMatch);

	OnGoingLogicalCondition startsWith(String toMatch);

	OnGoingLogicalCondition endsWith(String toMatch);

}
