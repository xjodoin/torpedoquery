package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.OnGoingCondition;
import com.netappsid.jpaquery.OnGoingLogicalOperation;
import com.netappsid.jpaquery.Query;

public class WhereClause<T> implements OnGoingCondition<T>, OnGoingLogicalOperation {
	private Condition condition;
	private final QueryBuilder queryBuilder;
	private final Method method;

	public WhereClause(QueryBuilder queryBuilder, Method method) {
		this.queryBuilder = queryBuilder;
		this.method = method;
	}

	@Override
	public OnGoingLogicalOperation eq(T value) {
		condition = new EqualCondition<T>(new SimpleMethodCallSelector(method), queryBuilder.generateParameter(method, value));
		return this;
	}

	@Override
	public OnGoingLogicalOperation neq(T value) {
		condition = new NotEqualCondition<T>(new SimpleMethodCallSelector(method), queryBuilder.generateParameter(method, value));
		return this;
	}

	@Override
	public OnGoingLogicalOperation lt(T value) {
		condition = new LtCondition<T>(new SimpleMethodCallSelector(method), queryBuilder.generateParameter(method, value));
		return this;
	}

	@Override
	public OnGoingLogicalOperation lte(T value) {
		condition = new LteCondition<T>(new SimpleMethodCallSelector(method), queryBuilder.generateParameter(method, value));
		return this;
	}

	@Override
	public OnGoingLogicalOperation gt(T value) {
		condition = new GtCondition<T>(new SimpleMethodCallSelector(method), queryBuilder.generateParameter(method, value));
		return this;
	}

	@Override
	public OnGoingLogicalOperation gte(T value) {
		condition = new GteCondition<T>(new SimpleMethodCallSelector(method), queryBuilder.generateParameter(method, value));
		return this;
	}

	@Override
	public OnGoingLogicalOperation isNull() {
		condition = new IsNullCondition(new SimpleMethodCallSelector(method));
		return this;
	}

	@Override
	public OnGoingLogicalOperation isNotNull() {
		condition = new IsNotNullCondition(new SimpleMethodCallSelector(method));
		return this;
	}

	@Override
	public OnGoingLogicalOperation in(T... values) {
		return in(Arrays.asList(values));
	}

	@Override
	public OnGoingLogicalOperation in(Collection<T> values) {
		condition = new InCondition<T>(new SimpleMethodCallSelector(method), queryBuilder.generateParameter(method, values));
		return this;
	}

	@Override
	public OnGoingLogicalOperation in(Query<T> query) {
		condition = new InSubQueryCondition<T>(new SimpleMethodCallSelector(method), query);
		return this;
	}

	public String createQueryFragment(QueryBuilder queryBuilder, AtomicInteger incrementor) {
		return condition.createQueryFragment(queryBuilder, incrementor);
	}

	@Override
	public <T1> OnGoingCondition<T1> and(T1 property) {
		return (OnGoingCondition<T1>) this;
	}

	@Override
	public <T1> OnGoingCondition<T1> or(T1 property) {
		return (OnGoingCondition<T1>) this;
	}

	public List<Parameter> getParameters() {
		return condition.getParameters();
	}

}
