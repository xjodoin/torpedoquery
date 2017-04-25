package org.torpedoquery.jpa.internal.joins;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.conditions.LogicalCondition;
import org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler;

/**
 * <p>InnerJoinBuilder class.</p>
 *
 * @author xjodoin
 * @version $Id: $Id
 */
public class InnerJoinBuilder<T> extends AbstractJoinBuilder<T>{

	private Class<T> queryClass;
	private TorpedoMethodHandler methodHandler;

	/**
	 * <p>Constructor for InnerJoinBuilder.</p>
	 *
	 * @param queryClass a {@link java.lang.Class} object.
	 * @param torpedoMethodHandler a {@link org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler} object.
	 */
	public InnerJoinBuilder(Class<T> queryClass,TorpedoMethodHandler torpedoMethodHandler) {
		super(queryClass, torpedoMethodHandler);
	}
	
	/** {@inheritDoc} */
	@Override
	protected InnerJoin createJoin(QueryBuilder queryBuilder, LogicalCondition joinCondition) {
		return new InnerJoin(queryBuilder, joinCondition);
	}

}
