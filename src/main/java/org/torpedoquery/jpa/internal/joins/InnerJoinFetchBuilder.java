package org.torpedoquery.jpa.internal.joins;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.conditions.LogicalCondition;
import org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler;

/**
 * <p>InnerJoinFetchBuilder class.</p>
 */
public class InnerJoinFetchBuilder<T> extends AbstractJoinFetchBuilder<T>{

	/**
	 * <p>Constructor for InnerJoinFetchBuilder.</p>
	 *
	 * @param queryClass a {@link Class} object.
	 * @param torpedoMethodHandler a {@link TorpedoMethodHandler} object.
	 */
	public InnerJoinFetchBuilder(Class<T> queryClass, TorpedoMethodHandler torpedoMethodHandler) {
		super(queryClass, torpedoMethodHandler);
	}
	
	/** {@inheritDoc} */
	@Override
	protected InnerJoinFetch createJoinFetch(QueryBuilder queryBuilder, LogicalCondition joinCondition) {
		return new InnerJoinFetch(queryBuilder, joinCondition);
	}

}
