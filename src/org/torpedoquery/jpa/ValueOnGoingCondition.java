package org.torpedoquery.jpa;

import java.util.Collection;

public interface ValueOnGoingCondition<T> extends OnGoingCondition<T> {

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

}
