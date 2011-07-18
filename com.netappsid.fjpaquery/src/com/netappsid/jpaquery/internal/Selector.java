package com.netappsid.jpaquery.internal;

import java.util.concurrent.atomic.AtomicInteger;

public interface Selector {

	String createQueryFragment(AtomicInteger incrementor);

	String getName();

}
