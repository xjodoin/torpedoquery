package org.torpedoquery.jpa.internal;


public class TorpedoMagic {

	private static ThreadLocal<Proxy> query = new ThreadLocal<Proxy>();
	
	public static void setQuery(Proxy query) {
		TorpedoMagic.query.set(query);
	}
	
	public static TorpedoMethodHandler getTorpedoMethodHandler() {
		Proxy internalQuery = query.get();
		return internalQuery.getTorpedoMethodHandler();
	}
	
}
