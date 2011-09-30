package org.torpedoquery.jpa;

import org.torpedoquery.jpa.internal.Selector;

public interface Function<T> extends Selector<T> {

	Object getProxy();

}
