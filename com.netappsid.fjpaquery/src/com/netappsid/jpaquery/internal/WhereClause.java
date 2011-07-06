package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.OnGoingCondition;
import com.netappsid.jpaquery.OnGoingLikeCondition;
import com.netappsid.jpaquery.OnGoingLogicalCondition;
import com.netappsid.jpaquery.Query;

public class WhereClause<T> implements OnGoingCondition<T>, OnGoingLikeCondition {
	private final QueryBuilder queryBuilder;
	private final Method method;
	private LogicalCondition logicalCondition;

	public WhereClause(QueryBuilder queryBuilder, Method method) {
		this.queryBuilder = queryBuilder;
		this.method = method;
	}

	@Override
	public OnGoingLogicalCondition eq(T value) {
		Condition condition = new EqualCondition<T>(new SimpleMethodCallSelector(method), queryBuilder.generateParameter(method, value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition neq(T value) {
		Condition condition = new NotEqualCondition<T>(new SimpleMethodCallSelector(method), queryBuilder.generateParameter(method, value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition lt(T value) {
		Condition condition = new LtCondition<T>(new SimpleMethodCallSelector(method), queryBuilder.generateParameter(method, value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition lte(T value) {
		Condition condition = new LteCondition<T>(new SimpleMethodCallSelector(method), queryBuilder.generateParameter(method, value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition gt(T value) {
		Condition condition = new GtCondition<T>(new SimpleMethodCallSelector(method), queryBuilder.generateParameter(method, value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition gte(T value) {
		Condition condition = new GteCondition<T>(new SimpleMethodCallSelector(method), queryBuilder.generateParameter(method, value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition isNull() {
		Condition condition = new IsNullCondition(new SimpleMethodCallSelector(method));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition isNotNull() {
		Condition condition = new IsNotNullCondition(new SimpleMethodCallSelector(method));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition in(T... values) {
		return in(Arrays.asList(values));
	}

	@Override
	public OnGoingLogicalCondition in(Collection<T> values) {
		Condition condition = new InCondition<T>(new SimpleMethodCallSelector(method), queryBuilder.generateParameter(method, values));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition in(Query<T> query) {
		Condition condition = new InSubQueryCondition<T>(new SimpleMethodCallSelector(method), query);
		return getOnGoingLogicalCondition(condition);
	}

	public String createQueryFragment(QueryBuilder queryBuilder, AtomicInteger incrementor) {
		return logicalCondition.createQueryFragment(queryBuilder, incrementor);
	}

	private OnGoingLogicalCondition getOnGoingLogicalCondition(Condition condition) {
		logicalCondition = new LogicalCondition(condition);
		return logicalCondition;
	}

	public List<Parameter> getParameters() {
		return logicalCondition.getParameters();
	}

	public boolean hasCondition() {
		return logicalCondition != null;
	}

	@Override
	public OnGoingLikeCondition like() {
		return this;
	}

	@Override
	public OnGoingLogicalCondition any(String toMatch) {
		return getOnGoingLogicalCondition(new LikeCondition(LikeCondition.Type.ANY, new SimpleMethodCallSelector(method), toMatch));
	}

	@Override
	public OnGoingLogicalCondition startsWith(String toMatch) {
		return getOnGoingLogicalCondition(new LikeCondition(LikeCondition.Type.STARTSWITH, new SimpleMethodCallSelector(method), toMatch));
	}

	@Override
	public OnGoingLogicalCondition endsWith(String toMatch) {
		return getOnGoingLogicalCondition(new LikeCondition(LikeCondition.Type.ENDSWITH, new SimpleMethodCallSelector(method), toMatch));
	}
}
