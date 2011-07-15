package com.netappsid.jpaquery.internal;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.FJPAQuery;
import com.netappsid.jpaquery.OnGoingCondition;
import com.netappsid.jpaquery.OnGoingLogicalCondition;
import com.netappsid.jpaquery.OnGoingNumberCondition;
import com.netappsid.jpaquery.OnGoingStringCondition;

public class LogicalCondition implements OnGoingLogicalCondition, Condition {

	private Condition condition;

	public LogicalCondition(Condition condition) {
		this.condition = condition;
	}

	@Override
	public <T1> OnGoingCondition<T1> and(T1 property) {

		OnGoingCondition<T1> right = this.<T1, OnGoingCondition<T1>> createCondition();
		condition = new AndCondition(condition, (Condition) right);
		return right;
	}

	@Override
	public <T1> OnGoingCondition<T1> or(T1 property) {
		OnGoingCondition<T1> right = this.<T1, OnGoingCondition<T1>> createCondition();
		condition = new OrCondition(condition, (Condition) right);
		return right;
	}

	@Override
	public <T1 extends Number> OnGoingNumberCondition<T1> and(T1 property) {
		OnGoingNumberCondition<T1> right = this.<T1, OnGoingNumberCondition<T1>> createCondition();
		condition = new AndCondition(condition, (Condition) right);
		return right;
	}

	@Override
	public <T1 extends Number> OnGoingNumberCondition<T1> or(T1 property) {
		OnGoingNumberCondition<T1> right = this.<T1, OnGoingNumberCondition<T1>> createCondition();
		condition = new OrCondition(condition, (Condition) right);
		return right;
	}

	private <T, E extends OnGoingCondition<T>> E createCondition() {
		FJPAMethodHandler fjpaMethodHandler = FJPAQuery.getFJPAMethodHandler();
		WhereClauseHandler<T, E> whereClauseHandler = new WhereClauseHandler<T, E>(this, false);
		E handle = fjpaMethodHandler.handle(whereClauseHandler);
		return handle;
	}

	@Override
	public List<Parameter> getParameters() {
		return condition.getParameters();
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder, AtomicInteger incrementor) {
		return condition.createQueryFragment(queryBuilder, incrementor);
	}

	@Override
	public OnGoingLogicalCondition and(OnGoingLogicalCondition condition) {
		this.condition = new AndCondition(this.condition, new GroupingCondition((Condition) condition));
		return this;
	}

	@Override
	public OnGoingLogicalCondition or(OnGoingLogicalCondition condition) {
		this.condition = new OrCondition(this.condition, new GroupingCondition((Condition) condition));
		return this;
	}

	@Override
	public OnGoingStringCondition<String> and(String property) {
		OnGoingStringCondition<String> right = this.<String, OnGoingStringCondition<String>> createCondition();
		condition = new AndCondition(condition, (Condition) right);
		return right;
	}

	@Override
	public OnGoingStringCondition<String> or(String property) {
		OnGoingStringCondition<String> right = this.<String, OnGoingStringCondition<String>> createCondition();
		condition = new OrCondition(condition, (Condition) right);
		return right;
	}

}
