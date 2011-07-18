package com.netappsid.jpaquery.internal;

import java.util.concurrent.atomic.AtomicInteger;

public class SizeSelector implements Selector {

	private final Selector selector;

	public SizeSelector(Selector selector) {
		this.selector = selector;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return selector.createQueryFragment(incrementor) + ".size";
	}

	@Override
	public String getName() {
		return selector.getName();
	}

}
