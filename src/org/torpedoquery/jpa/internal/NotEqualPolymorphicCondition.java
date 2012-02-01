package org.torpedoquery.jpa.internal;

public class NotEqualPolymorphicCondition<T> extends PolymorphicCondition<T> {

	public NotEqualPolymorphicCondition(Selector selector,
			Class<? extends T> condition) {
		super(selector, condition);
	}

	@Override
	protected String getComparator() {
		return NotEqualCondition.NOT_EQUAL;
	}

}
