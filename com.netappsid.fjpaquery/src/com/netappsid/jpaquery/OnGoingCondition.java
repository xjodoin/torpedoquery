package com.netappsid.jpaquery;

public interface OnGoingCondition<T> {
	

	// where(entity.getCode()).like("code);
	// where(entity.getCode()).in("code); where(entity.getCode()).notIn("code);
	// where(entity.getCode()).isNull(); where(entity.getCode()).isNotNull();
	// where(entity.getCode()).isEmpty(); where(entity.getCode()).isNotEmpty();
	
	OnGoingLogicalOperation eq(T value);
	
	OnGoingLogicalOperation neq(T value);
	
	OnGoingLogicalOperation lt(T value);
	
	OnGoingLogicalOperation lte(T value);
	
	OnGoingLogicalOperation gt(T value);
	
	OnGoingLogicalOperation gte(T value);

	OnGoingLogicalOperation isNull();
	
	
}
