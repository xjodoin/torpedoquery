package org.torpedoquery.jpa.internal;

import java.util.concurrent.atomic.AtomicInteger;

public interface Selector<T> {

	String createQueryFragment(AtomicInteger incrementor);

	Parameter<T> generateParameter(T value);

}
