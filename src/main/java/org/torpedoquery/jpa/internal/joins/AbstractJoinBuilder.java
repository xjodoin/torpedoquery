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

public abstract class AbstractJoinBuilder<T> implements JoinBuilder<T> {

	private Class<T> queryClass;
	private TorpedoMethodHandler methodHandler;

	public AbstractJoinBuilder(Class<T> queryClass,TorpedoMethodHandler torpedoMethodHandler) {
		this.queryClass = queryClass;
		methodHandler = torpedoMethodHandler;
	}
	
	@Override
	public T on(Function<T, OnGoingLogicalCondition> onBuilder) {
		T join = TorpedoMagic.getProxyfactoryfactory().createProxy(methodHandler,queryClass, TorpedoProxy.class);
		final QueryBuilder queryBuilder = methodHandler.addQueryBuilder(join, new DefaultQueryBuilder(queryClass));
		LogicalCondition joinCondition = (LogicalCondition) onBuilder.apply(join);
		methodHandler.getRoot().addJoin(createJoin(queryBuilder, joinCondition));
		return join;
	}

	protected abstract Join createJoin(final QueryBuilder queryBuilder, LogicalCondition joinCondition);

}
