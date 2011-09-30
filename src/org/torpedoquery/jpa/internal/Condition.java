package org.torpedoquery.jpa.internal;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface Condition {

	String createQueryFragment(AtomicInteger incrementor);

	List<Parameter> getParameters();
}
