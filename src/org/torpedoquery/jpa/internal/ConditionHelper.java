package org.torpedoquery.jpa.internal;

import org.torpedoquery.jpa.Function;
import org.torpedoquery.jpa.OnGoingCondition;

public class ConditionHelper {
	public static <T, E extends OnGoingCondition<T>> E createCondition(LogicalCondition condition) {
		E handle = ConditionHelper.<T, E> createCondition(null, condition);
		return handle;
	}

	public static <T, E extends OnGoingCondition<T>> E createCondition(Function<T> function, LogicalCondition condition) {
		TorpedoMethodHandler fjpaMethodHandler = TorpedoMagic.getTorpedoMethodHandler();
		WhereClauseHandler<T, E> whereClauseHandler = new WhereClauseHandler<T, E>(function, condition, new DoNothingQueryConfigurator<T>());
		E handle = fjpaMethodHandler.handle(whereClauseHandler);
		return handle;
	}

}
