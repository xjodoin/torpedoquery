package org.torpedoquery.jpa.internal.utils;

import java.io.Serializable;

import javassist.util.proxy.MethodHandler;

public interface SerializableMethodHandler extends MethodHandler, Serializable {

}
