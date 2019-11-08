package org.torpedoquery.jpa.internal.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Defaults class.</p>
 *
 * @author xjodoin
 * @version $Id: $Id
 */
public final class Defaults {
	private Defaults() {
	}

	private static final Map<Class<?>, Object> DEFAULTS;

	static {
		// Only add to this map via put(Map, Class<T>, T)
		Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();
		put(map, boolean.class, false);
		put(map, char.class, '\0');
		put(map, byte.class, (byte) 0);
		put(map, short.class, (short) 0);
		put(map, int.class, 0);
		put(map, long.class, 0L);
		put(map, float.class, 0f);
		put(map, double.class, 0d);
		DEFAULTS = Collections.unmodifiableMap(map);
	}

	private static <T> void put(Map<Class<?>, Object> map, Class<T> type, T value) {
		map.put(type, value);
	}

	/**
	 * Returns the default value of {@code type} as defined by JLS --- {@code 0} for
	 * numbers, {@code
	 * false} for {@code boolean} and {@code '\0'} for {@code char}. For
	 * non-primitive types and {@code void}, {@code null} is returned.
	 *
	 * @param type a {@link java.lang.Class} object.
	 * @return a T object.
	 * @param <T> a T object.
	 */
	public static <T> T defaultValue(Class<T> type) {
		@SuppressWarnings("unchecked") // the put method enforces this key-value relationship
		T t = (T) DEFAULTS.get(type);
		return t;
	}
}
