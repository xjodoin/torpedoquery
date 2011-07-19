package com.netappsid.jpaquery.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.Function;
import com.netappsid.jpaquery.NumberFunction;
import com.netappsid.jpaquery.OnGoingCollectionCondition;
import com.netappsid.jpaquery.OnGoingCondition;
import com.netappsid.jpaquery.OnGoingGroupByCondition;
import com.netappsid.jpaquery.OnGoingNumberCondition;
import com.netappsid.jpaquery.OnGoingStringCondition;

public class GroupBy implements OnGoingGroupByCondition {

	private final List<Selector> groups = new ArrayList<Selector>();
	private Condition havingCondition;

	public String createQueryFragment(StringBuilder builder, AtomicInteger incrementor) {

		if (!groups.isEmpty()) {
			Iterator<Selector> iterator = groups.iterator();

			if (builder.length() == 0) {
				builder.append(" group by ").append(iterator.next().createQueryFragment(incrementor));
			}

			while (iterator.hasNext()) {
				Selector selector = iterator.next();
				builder.append(",").append(selector.createQueryFragment(incrementor));
			}

			if (havingCondition != null) {
				builder.append(" having ").append(havingCondition.createQueryFragment(incrementor));
			}

			return builder.toString();
		}
		return "";
	}

	public void addGroup(Selector selector) {
		groups.add(selector);
	}

	@Override
	public <T> OnGoingCondition<T> having(T object) {
		OnGoingCondition<T> createCondition = ConditionHelper.<T, OnGoingCondition<T>> createCondition(null);
		havingCondition = (Condition) createCondition;
		return createCondition;
	}

	@Override
	public <T extends Number> OnGoingNumberCondition<T, T> having(T object) {
		OnGoingNumberCondition<T, T> createCondition = ConditionHelper.<T, OnGoingNumberCondition<T, T>> createCondition(null);
		havingCondition = (Condition) createCondition;
		return createCondition;
	}

	@Override
	public OnGoingStringCondition<String> having(String object) {
		OnGoingStringCondition<String> createCondition = ConditionHelper.<String, OnGoingStringCondition<String>> createCondition(null);
		havingCondition = (Condition) createCondition;
		return createCondition;
	}

	@Override
	public <T> OnGoingCollectionCondition<T> having(Collection<T> object) {
		OnGoingCollectionCondition<T> createCollectionCondition = ConditionHelper.createCollectionCondition(null);
		havingCondition = (Condition) createCollectionCondition;
		return createCollectionCondition;
	}

	@Override
	public <T> OnGoingCondition<T> having(Function<T> function) {
		OnGoingCondition<T> createCondition = ConditionHelper.<T, OnGoingCondition<T>> createCondition(function, null);
		havingCondition = (Condition) createCondition;
		return createCondition;
	}

	@Override
	public <T extends Number> OnGoingNumberCondition<T, T> having(NumberFunction<T> function) {
		OnGoingNumberCondition<T, T> createCondition = ConditionHelper.<T, OnGoingNumberCondition<T, T>> createCondition(function, null);
		havingCondition = (Condition) createCondition;
		return createCondition;
	}
}
