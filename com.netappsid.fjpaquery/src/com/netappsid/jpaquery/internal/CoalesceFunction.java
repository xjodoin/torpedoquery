package com.netappsid.jpaquery.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.netappsid.jpaquery.ComparableFunction;

public class CoalesceFunction<T> implements ComparableFunction<T> {

	private final List<Selector> selectors = new ArrayList<Selector>();
	private Proxy proxy;

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {

		StringBuffer stringBuffer = new StringBuffer();
		Iterator<Selector> iterator = selectors.iterator();
		Selector first = iterator.next();
		stringBuffer.append("coalesce(").append(first.createQueryFragment(incrementor));

		while (iterator.hasNext()) {
			Selector selector = iterator.next();
			stringBuffer.append(",").append(selector.createQueryFragment(incrementor));
		}

		stringBuffer.append(")");

		return stringBuffer.toString();
	}

	@Override
	public Object getProxy() {
		return proxy;
	}

	public void addSelector(Selector selector) {
		selectors.add(selector);
	}

	public void setQuery(Proxy proxy) {
		this.proxy = proxy;

	}

	@Override
	public Parameter<T> generateParameter(T value) {
		return new SelectorParameter<T>(this);
	}

}
