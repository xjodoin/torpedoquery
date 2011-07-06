package com.netappsid.jpaquery.internal;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LikeCondition implements Condition {

	public static enum Type {
		ANY {
			@Override
			public String wrap(String toMatch) {
				return "%" + toMatch + "%";
			}
		},
		STARTSWITH {
			@Override
			public String wrap(String toMatch) {
				return toMatch + "%";
			}
		},
		ENDSWITH {
			@Override
			public String wrap(String toMatch) {
				return "%" + toMatch;
			}
		};

		public abstract String wrap(String toMatch);
	}

	private final String toMatch;
	private final Type type;
	private final Selector selector;

	public LikeCondition(Type type, Selector selector, String toMatch) {
		this.type = type;
		this.selector = selector;
		this.toMatch = toMatch;
	}

	@Override
	public String createQueryFragment(QueryBuilder queryBuilder, AtomicInteger incrementor) {
		return selector.createQueryFragment(queryBuilder, incrementor) + " like '" + type.wrap(toMatch) + "' ";
	}

	@Override
	public List<Parameter> getParameters() {
		return Collections.emptyList();
	}

}
