package org.torpedoquery.jpa.internal;

import java.util.Deque;
import java.util.Map;

import org.torpedoquery.jpa.Function;
import org.torpedoquery.jpa.OnGoingMathOperation;

public class MathOperationHandler<T> implements QueryHandler<OnGoingMathOperation<T>>, OnGoingMathOperation<T> {

	private Selector<T> leftSelector;
	private Proxy proxy;

	public MathOperationHandler(Function<T> left) {
		leftSelector = left;
		if (left != null) {
			proxy = (Proxy) left.getProxy();
		}
	}

	@Override
	public OnGoingMathOperation<T> handleCall(Map<Object, QueryBuilder<?>> proxyQueryBuilders, Deque<MethodCall> methods) {

		if (leftSelector == null) {
			MethodCall methodCall = methods.pollFirst();
			proxy = methodCall.getProxy();
			leftSelector = new SimpleMethodCallSelector(proxyQueryBuilders.get(methodCall.getProxy()), methodCall);
		}

		return this;
	}

	@Override
	public Function<T> plus(T right) {

		SimpleMethodCallSelector rightSelector = handleMethodCall();
		return createFunction(rightSelector, "+");
	}

	private SimpleMethodCallSelector handleMethodCall() {
		TorpedoMethodHandler torpedoMethodHandler = TorpedoMagic.getTorpedoMethodHandler();
		MethodCall methodCall = torpedoMethodHandler.getMethods().getFirst();
		SimpleMethodCallSelector rightSelector = new SimpleMethodCallSelector(torpedoMethodHandler.getQueryBuilder(methodCall.getProxy()), methodCall);
		return rightSelector;
	}

	@Override
	public Function<T> plus(Function<T> right) {
		return createFunction(right,"+");
	}

	private MathOperationFunction<T> createFunction(Selector<T> right,String operator) {
		return new MathOperationFunction<T>(proxy, leftSelector, operator, right);
	}

	@Override
	public Function<T> subtract(T right) {
		SimpleMethodCallSelector rightSelector = handleMethodCall();
		return createFunction(rightSelector, "-");
	}

	@Override
	public Function<T> subtract(Function<T> right) {
		return createFunction(right,"-");
	}

	@Override
	public Function<T> multiply(T right) {
		SimpleMethodCallSelector rightSelector = handleMethodCall();
		return createFunction(rightSelector, "*");
	}

	@Override
	public Function<T> multiply(Function<T> right) {
		return createFunction(right,"*");
	}

	@Override
	public Function<T> divide(T right) {
		SimpleMethodCallSelector rightSelector = handleMethodCall();
		return createFunction(rightSelector, "/");
	}

	@Override
	public Function<T> divide(Function<T> right) {
		return createFunction(right,"/");
	}

}
