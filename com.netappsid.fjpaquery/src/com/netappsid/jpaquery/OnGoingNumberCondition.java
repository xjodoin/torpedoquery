package com.netappsid.jpaquery;

public interface OnGoingNumberCondition<T, N extends Number> extends OnGoingCondition<T> {
	OnGoingLogicalCondition lt(N value);

	OnGoingLogicalCondition lt(NumberFunction<N, N> value);

	OnGoingLogicalCondition lte(N value);

	OnGoingLogicalCondition lte(NumberFunction<N, N> value);

	OnGoingLogicalCondition gt(N value);

	OnGoingLogicalCondition gt(NumberFunction<N, N> value);

	OnGoingLogicalCondition gte(N value);

	OnGoingLogicalCondition gte(NumberFunction<N, N> value);
}
