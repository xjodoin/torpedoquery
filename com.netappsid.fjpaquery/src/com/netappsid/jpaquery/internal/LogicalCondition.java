package com.netappsid.jpaquery.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.FJPAQuery;
import com.netappsid.jpaquery.OnGoingCondition;
import com.netappsid.jpaquery.OnGoingLogicalCondition;
import com.netappsid.jpaquery.OnGoingNumberCondition;

public class LogicalCondition implements OnGoingLogicalCondition {

	private final Condition leftCondition;
	private WhereClause right;
	private String condition;

	public LogicalCondition(Condition leftCondition) {
		this.leftCondition = leftCondition;
	}

	@Override
	public <T1> OnGoingCondition<T1> and(T1 property) {

		condition = " and ";
		return getRight();
	}

	@Override
	public <T1 extends Number> OnGoingNumberCondition<T1> and(T1 property) {
		condition = " and ";
		return getRight();
	}

	@Override
	public <T1> OnGoingCondition<T1> or(T1 property) {
		condition = " or ";
		return getRight();
	}

	@Override
	public <T1 extends Number> OnGoingNumberCondition<T1> or(T1 property) {
		condition = " or ";
		return getRight();
	}

	private <T1> WhereClause<T1> getRight() {
		FJPAMethodHandler fjpaMethodHandler = FJPAQuery.getFJPAMethodHandler();
		WhereClauseHandler<T1, OnGoingCondition<T1>> whereClauseHandler = new WhereClauseHandler<T1, OnGoingCondition<T1>>(false);
		OnGoingCondition<T1> handle = fjpaMethodHandler.handle(whereClauseHandler);
		right = (WhereClause) handle;
		return right;
	}

	public List<Parameter> getParameters() {
		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.addAll(leftCondition.getParameters());
		if (right != null && right.hasCondition()) {
			parameters.addAll(right.getParameters());
		}
		return parameters;
	}

	public String createQueryFragment(QueryBuilder queryBuilder, AtomicInteger incrementor) {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(leftCondition.createQueryFragment(queryBuilder, incrementor));
		if (right != null && right.hasCondition()) {
			stringBuilder.append(condition).append(right.createQueryFragment(queryBuilder, incrementor));
		}
		return stringBuilder.toString();
	}

}
