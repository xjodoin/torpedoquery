package org.torpedoquery.jpa.internal;

import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.Function;

public class MathOperationFunction<T> implements Function<T> {

	private final Selector<T> leftOperand;
	private final String operator;
	private final Selector<T> rightOperand;
	private final Proxy proxy;

	public MathOperationFunction(Proxy proxy, Selector<T> leftOperand, String operator, Selector<T> rightOperand) {
		this.proxy = proxy;
		this.leftOperand = leftOperand;
		this.operator = operator;
		this.rightOperand = rightOperand;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return leftOperand.createQueryFragment(incrementor) + " " + operator + " " + rightOperand.createQueryFragment(incrementor);
	}

	@Override
	public Parameter<T> generateParameter(T value) {
		return null;
	}

	@Override
	public Object getProxy() {
		return proxy;
	}

}
