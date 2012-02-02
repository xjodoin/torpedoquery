package org.torpedoquery.jpa.internal.conditions;

import org.torpedoquery.jpa.internal.Selector;

public class EqualPolymorphicCondition<T> extends PolymorphicCondition<T> {

	public EqualPolymorphicCondition(Selector selector, Class<? extends T> condition) {
		super(selector, condition);
	}

	@Override
	protected String getComparator() {
		return EqualCondition.EQUAL;
	}

}
