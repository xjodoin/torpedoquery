package com.netappsid.jpaquery.internal;

import com.netappsid.jpaquery.FJPAQuery;
import com.netappsid.jpaquery.Function;
import com.netappsid.jpaquery.OnGoingCollectionCondition;
import com.netappsid.jpaquery.OnGoingCondition;

public class ConditionHelper {
	public static <T, E extends OnGoingCondition<T>> E createCondition(LogicalCondition condition) {
		E handle = createCondition((Function<T>) null, condition);
		return handle;
	}

	public static <T, E extends OnGoingCondition<T>> E createCondition(Function<T> function, LogicalCondition condition) {
		FJPAMethodHandler fjpaMethodHandler = FJPAQuery.getFJPAMethodHandler();
		WhereClauseHandler<T, E> whereClauseHandler = new WhereClauseHandler<T, E>(function, condition, false);
		E handle = fjpaMethodHandler.handle(whereClauseHandler);
		return handle;
	}

	public static <T> OnGoingCollectionCondition<T> createCollectionCondition(LogicalCondition condition) {
		FJPAMethodHandler fjpaMethodHandler = FJPAQuery.getFJPAMethodHandler();
		WhereClauseCollectionHandler<T> whereClauseCollectionHandler = new WhereClauseCollectionHandler<T>(condition, false);
		OnGoingCollectionCondition<T> handle = fjpaMethodHandler.handle(whereClauseCollectionHandler);
		return handle;
	}
}
