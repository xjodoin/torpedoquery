/**
 *
 *   Copyright 2011 Xavier Jodoin xjodoin@gmail.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.torpedoquery.jpa.internal.conditions;

import org.torpedoquery.jpa.Function;
import org.torpedoquery.jpa.OnGoingCondition;
import org.torpedoquery.jpa.internal.Condition;
import org.torpedoquery.jpa.internal.TorpedoMagic;
import org.torpedoquery.jpa.internal.handlers.WhereClauseHandler;
import org.torpedoquery.jpa.internal.utils.DoNothingQueryConfigurator;
import org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler;

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
	
	
	public static Condition getConditionClause(ConditionBuilder<?> conditionBuilder) {
		if (conditionBuilder != null) {
			return conditionBuilder.getLogicalCondition() != null ? conditionBuilder.getLogicalCondition() : conditionBuilder;
		}
		return null;
	}

}
