package org.torpedoquery.jpa.internal.joins;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.Join;
import org.torpedoquery.jpa.internal.conditions.LogicalCondition;
import org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler;

/**
 * <p>LeftJoinFetchBuilder class.</p>
 */
public class LeftJoinFetchBuilder<T> extends AbstractJoinFetchBuilder<T>{

	/**
	 * <p>Constructor for LeftJoinFetchBuilder.</p>
	 *
	 * @param queryClass a {@link Class} object.
	 * @param torpedoMethodHandler a {@link TorpedoMethodHandler} object.
	 */
	public LeftJoinFetchBuilder(Class<T> queryClass, TorpedoMethodHandler torpedoMethodHandler) {
		super(queryClass, torpedoMethodHandler);
	}
	
	/** {@inheritDoc} */
	@Override
	protected Join createJoinFetch(QueryBuilder queryBuilder, LogicalCondition joinCondition) {
		return new LeftJoinFetch(queryBuilder, joinCondition);
	}

}
