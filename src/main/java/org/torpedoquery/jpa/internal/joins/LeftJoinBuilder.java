package org.torpedoquery.jpa.internal.joins;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.internal.Join;
import org.torpedoquery.jpa.internal.conditions.LogicalCondition;
import org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler;

/**
 * <p>LeftJoinBuilder class.</p>
 *
 * @author xjodoin
 * @version $Id: $Id
 */
public class LeftJoinBuilder<T> extends AbstractJoinBuilder<T>{

	private Class<T> queryClass;
	private TorpedoMethodHandler methodHandler;

	/**
	 * <p>Constructor for LeftJoinBuilder.</p>
	 *
	 * @param queryClass a {@link java.lang.Class} object.
	 * @param torpedoMethodHandler a {@link org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler} object.
	 */
	public LeftJoinBuilder(Class<T> queryClass,TorpedoMethodHandler torpedoMethodHandler) {
		super(queryClass, torpedoMethodHandler);
	}
	
	/** {@inheritDoc} */
	@Override
	protected Join createJoin(QueryBuilder queryBuilder, LogicalCondition joinCondition) {
		return new LeftJoin(queryBuilder, joinCondition);
	}

}
