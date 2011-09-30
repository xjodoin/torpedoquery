package com.netappsid.jpaquery;

import com.netappsid.jpaquery.internal.Selector;

public interface Function<T> extends Selector<T> {

	Object getProxy();

}
