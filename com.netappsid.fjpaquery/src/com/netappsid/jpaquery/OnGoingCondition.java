package com.netappsid.jpaquery;

import java.util.Collection;

public interface OnGoingCondition<T> {

	// TODO
	// Spliter les interface par type genre number
	// where(entity.getCode()).isEmpty(); where(entity.getCode()).isNotEmpty();

	OnGoingLogicalCondition eq(T value);

	OnGoingLogicalCondition neq(T value);

	OnGoingLogicalCondition isNull();

	OnGoingLogicalCondition isNotNull();

	OnGoingLogicalCondition in(T... values);

	OnGoingLogicalCondition in(Collection<T> values);

	OnGoingLogicalCondition in(Query<T> subQuery);

	OnGoingLogicalCondition notIn(T... values);

	OnGoingLogicalCondition notIn(Collection<T> values);

	OnGoingLogicalCondition notIn(Query<T> subQuery);

	OnGoingLikeCondition like();

}
