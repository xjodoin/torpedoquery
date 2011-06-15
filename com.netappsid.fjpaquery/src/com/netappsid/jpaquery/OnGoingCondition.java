package com.netappsid.jpaquery;

import java.util.List;

public interface OnGoingCondition<T> {

	// where(entity.getCode()).like("code);
	// where(entity.getCode()).in("code); where(entity.getCode()).notIn("code);
	// where(entity.getCode()).isEmpty(); where(entity.getCode()).isNotEmpty();

	OnGoingLogicalOperation eq(T value);

	OnGoingLogicalOperation neq(T value);

	OnGoingLogicalOperation lt(T value);

	OnGoingLogicalOperation lte(T value);

	OnGoingLogicalOperation gt(T value);

	OnGoingLogicalOperation gte(T value);

	OnGoingLogicalOperation isNull();

	OnGoingLogicalOperation isNotNull();

	OnGoingLogicalOperation in(T... values);

	OnGoingLogicalOperation in(List<T> values);

}
