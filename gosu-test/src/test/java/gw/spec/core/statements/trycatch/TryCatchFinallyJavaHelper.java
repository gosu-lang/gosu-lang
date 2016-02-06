/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.statements.trycatch;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 20, 2009
 * Time: 9:57:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class TryCatchFinallyJavaHelper {

  public static void throwRuntimeException() {
    throw new RuntimeException("java");
  }

  public static void throwOutOfMemoryError() {
    throw new OutOfMemoryError("java");
  }

  public static void throwException() throws Exception {
    throw new Exception("java");
  }
}
