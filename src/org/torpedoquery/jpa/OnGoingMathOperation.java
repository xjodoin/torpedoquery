package org.torpedoquery.jpa;

public interface OnGoingMathOperation<T> {

	Function<T> plus(T right);

	Function<T> plus(Function<T> right);

	Function<T> subtract(T right);

	Function<T> subtract(Function<T> right);
	
	Function<T> multiply(T right);

	Function<T> multiply(Function<T> right);
	
	Function<T> divide(T right);

	Function<T> divide(Function<T> right);
}
