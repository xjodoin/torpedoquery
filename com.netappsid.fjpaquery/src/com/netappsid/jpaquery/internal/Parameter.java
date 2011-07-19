package com.netappsid.jpaquery.internal;

import java.util.concurrent.atomic.AtomicInteger;

public interface Parameter<T> {

	public String generate(AtomicInteger incrementor);

}