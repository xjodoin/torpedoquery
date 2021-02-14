package org.torpedoquery.jpa.internal.joins;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.JoinBuilder;
import org.torpedoquery.jpa.OnGoingLogicalCondition;
import org.torpedoquery.jpa.internal.Join;
import org.torpedoquery.jpa.internal.TorpedoMagic;
import org.torpedoquery.jpa.internal.TorpedoProxy;
import org.torpedoquery.jpa.internal.conditions.LogicalCondition;
import org.torpedoquery.jpa.internal.query.DefaultQueryBuilder;
import org.torpedoquery.jpa.internal.utils.TorpedoMethodHandler;

import java.util.function.Function;

/**
 * <p>Abstract AbstractJoinFetchBuilder class.</p>
 */
public abstract class AbstractJoinFetchBuilder<T> implements JoinBuilder<T> {

	private Class<T> queryClass;
	private TorpedoMethodHandler methodHandler;

	/**
	 * <p>Constructor for AbstractJoinFetchBuilder.</p>
	 *
	 * @param queryClass a {@link Class} object.
	 * @param torpedoMethodHandler a {@link TorpedoMethodHandler} object.
	 */
	public AbstractJoinFetchBuilder(Class<T> queryClass, TorpedoMethodHandler torpedoMethodHandler) {
		this.queryClass = queryClass;
		methodHandler = torpedoMethodHandler;
	}

	/** {@inheritDoc} */
	@Override
	public T on(Function<T, OnGoingLogicalCondition> onBuilder) {
		T join = TorpedoMagic.getProxyfactoryfactory().createProxy(methodHandler,queryClass, TorpedoProxy.class);
		final QueryBuilder queryBuilder = methodHandler.addQueryBuilder(join, new DefaultQueryBuilder(queryClass));
		LogicalCondition joinCondition = (LogicalCondition) onBuilder.apply(join);
		methodHandler.getRoot().addJoin(createJoinFetch(queryBuilder, joinCondition));
		return join;
	}

	/**
	 * <p>createJoin.</p>
	 *
	 * @param queryBuilder a {@link QueryBuilder} object.
	 * @param joinCondition a {@link LogicalCondition} object.
	 * @return a {@link Join} object.
	 */
	protected abstract Join createJoinFetch(final QueryBuilder queryBuilder, LogicalCondition joinCondition);

}
