package org.torpedoquery.jpa.internal.joins;

import java.util.function.Function;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.JoinBuilder;
import org.torpedoquery.jpa.OnGoingLogicalCondition;
import org.torpedoquery.jpa.internal.Join;
import org.torpedoquery.jpa.internal.TorpedoMagic;
import org.torpedoquery.jpa.internal.TorpedoProxy;
import org.torpedoquery.jpa.internal.conditions.LogicalCondition;
import org.torpedoquery.jpa.internal.query.DefaultQueryBuilder;
import org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler;

/**
 * <p>Abstract AbstractJoinBuilder class.</p>
 *
 * @author xjodoin
 * @version $Id: $Id
 */
public abstract class AbstractJoinBuilder<T> implements JoinBuilder<T> {

	private Class<T> queryClass;
	private TorpedoMethodHandler methodHandler;

	/**
	 * <p>Constructor for AbstractJoinBuilder.</p>
	 *
	 * @param queryClass a {@link java.lang.Class} object.
	 * @param torpedoMethodHandler a {@link org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler} object.
	 */
	public AbstractJoinBuilder(Class<T> queryClass,TorpedoMethodHandler torpedoMethodHandler) {
		this.queryClass = queryClass;
		methodHandler = torpedoMethodHandler;
	}
	
	/** {@inheritDoc} */
	@Override
	public T on(Function<T, OnGoingLogicalCondition> onBuilder) {
		T join = TorpedoMagic.getProxyfactoryfactory().createProxy(methodHandler,queryClass, TorpedoProxy.class);
		final QueryBuilder queryBuilder = methodHandler.addQueryBuilder(join, new DefaultQueryBuilder(queryClass));
		LogicalCondition joinCondition = (LogicalCondition) onBuilder.apply(join);
		methodHandler.getRoot().addJoin(createJoin(queryBuilder, joinCondition));
		return join;
	}

	/**
	 * <p>createJoin.</p>
	 *
	 * @param queryBuilder a {@link org.torpedoquery.core.QueryBuilder} object.
	 * @param joinCondition a {@link org.torpedoquery.jpa.internal.conditions.LogicalCondition} object.
	 * @return a {@link org.torpedoquery.jpa.internal.Join} object.
	 */
	protected abstract Join createJoin(final QueryBuilder queryBuilder, LogicalCondition joinCondition);

}
