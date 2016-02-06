/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 1, 2009
 * Time: 4:41:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GosuSpecTestFixture {

  Object newInstance(String typeName);

  Object invokeMethod(Object context, String methodName, Object... args);

  Object invokeStaticMethod(String typeName, String methodName, Object... args);
}
