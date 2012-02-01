package org.torpedoquery.jpa.internal;

public class EqualPolymorphicCondition<T> extends PolymorphicCondition<T> {

	public EqualPolymorphicCondition(Selector selector, Class<? extends T> condition) {
		super(selector, condition);
	}

	@Override
	protected String getComparator() {
		return EqualCondition.EQUAL;
	}

}
