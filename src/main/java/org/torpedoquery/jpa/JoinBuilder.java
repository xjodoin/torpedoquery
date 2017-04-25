package org.torpedoquery.jpa;

import java.util.function.Function;

/**
 * <p>JoinBuilder interface.</p>
 *
 * @author xjodoin
 * @version $Id: $Id
 */
public interface JoinBuilder<T> {

	/**
	 * <p>on.</p>
	 *
	 * @param onBuilder a {@link java.util.function.Function} object.
	 * @return a T object.
	 */
	T on(Function<T, OnGoingLogicalCondition> onBuilder);
	
}
