package com.netappsid.jpaquery.internal;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.OnGoingCollectionCondition;
import com.netappsid.jpaquery.OnGoingLikeCondition;
import com.netappsid.jpaquery.OnGoingLogicalCondition;
import com.netappsid.jpaquery.OnGoingNumberCondition;
import com.netappsid.jpaquery.OnGoingStringCondition;
import com.netappsid.jpaquery.Query;

public class WhereClause<T> implements OnGoingNumberCondition<T>, OnGoingStringCondition<T>, OnGoingLikeCondition, OnGoingCollectionCondition<T>, Condition {
	private final QueryBuilder queryBuilder;
	private Selector selector;
	private final LogicalCondition logicalCondition;
	private Condition condition;

	public WhereClause(QueryBuilder queryBuilder, Method method) {
		this.logicalCondition = new LogicalCondition(this);
		this.queryBuilder = queryBuilder;
		this.selector = new SimpleMethodCallSelector(method);
	}

	public WhereClause(LogicalCondition logicalCondition, QueryBuilder queryBuilder, Method method) {
		this.logicalCondition = logicalCondition;
		this.queryBuilder = queryBuilder;
		this.selector = new SimpleMethodCallSelector(method);
	}

	public LogicalCondition getLogicalCondition() {
		return logicalCondition;
	}

	@Override
	public OnGoingLogicalCondition eq(T value) {
		Condition condition = new EqualCondition<T>(selector, queryBuilder.generateParameter(selector, value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition neq(T value) {
		Condition condition = new NotEqualCondition<T>(selector, queryBuilder.generateParameter(selector, value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition lt(T value) {
		Condition condition = new LtCondition<T>(selector, queryBuilder.generateParameter(selector, value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition lte(T value) {
		Condition condition = new LteCondition<T>(selector, queryBuilder.generateParameter(selector, value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition gt(T value) {
		Condition condition = new GtCondition<T>(selector, queryBuilder.generateParameter(selector, value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition gte(T value) {
		Condition condition = new GteCondition<T>(selector, queryBuilder.generateParameter(selector, value));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition isNull() {
		Condition condition = new IsNullCondition(selector);
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition isNotNull() {
		Condition condition = new IsNotNullCondition(selector);
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition in(T... values) {
		return in(Arrays.asList(values));
	}

	@Override
	public OnGoingLogicalCondition in(Collection<T> values) {
		Condition condition = new InCondition<T>(selector, queryBuilder.generateParameter(selector, values));
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition in(Query<T> query) {
		Condition condition = new InSubQueryCondition<T>(selector, query);
		return getOnGoingLogicalCondition(condition);
	}

	@Override
	public OnGoingLogicalCondition notIn(T... values) {
		return getOnGoingLogicalCondition(new NotInCondition<T>(selector, queryBuilder.generateParameter(selector, values)));
	}

	@Override
	public OnGoingLogicalCondition notIn(Collection<T> values) {
		return getOnGoingLogicalCondition(new NotInCondition<T>(selector, queryBuilder.generateParameter(selector, values)));
	}

	@Override
	public OnGoingLogicalCondition notIn(Query<T> subQuery) {
		return getOnGoingLogicalCondition(new NotInSubQueryCondition<T>(selector, subQuery));
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder, AtomicInteger incrementor) {
		if (condition != null) {
			return condition.createQueryFragment(queryBuilder, incrementor);
		} else {
			return "";
		}

	}

	private OnGoingLogicalCondition getOnGoingLogicalCondition(Condition condition) {
		this.condition = condition;
		return logicalCondition;
	}

	@Override
	public List<Parameter> getParameters() {
		if (condition != null) {
			return condition.getParameters();
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public OnGoingLikeCondition like() {
		return this;
	}

	@Override
	public OnGoingLogicalCondition any(String toMatch) {
		return getOnGoingLogicalCondition(new LikeCondition(LikeCondition.Type.ANY, selector, toMatch));
	}

	@Override
	public OnGoingLogicalCondition startsWith(String toMatch) {
		return getOnGoingLogicalCondition(new LikeCondition(LikeCondition.Type.STARTSWITH, selector, toMatch));
	}

	@Override
	public OnGoingLogicalCondition endsWith(String toMatch) {
		return getOnGoingLogicalCondition(new LikeCondition(LikeCondition.Type.ENDSWITH, selector, toMatch));
	}

	@Override
	public OnGoingLogicalCondition isEmpty() {
		return getOnGoingLogicalCondition(new IsEmptyCondition(selector));
	}

	@Override
	public OnGoingLogicalCondition isNotEmpty() {
		return getOnGoingLogicalCondition(new IsNotEmptyCondition(selector));
	}

	@Override
	public OnGoingNumberCondition<Integer> size() {
		selector = new SizeSelector(selector);
		return (OnGoingNumberCondition<Integer>) this;
	}
}
