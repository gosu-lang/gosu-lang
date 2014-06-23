/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

public class RuntimeExceptionWithNoStacktrace extends RuntimeException
{
  public RuntimeExceptionWithNoStacktrace(Throwable cause) {
    super(cause);
  }

  public RuntimeExceptionWithNoStacktrace(String message, Throwable cause) {
    super(message, cause);
  }

  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}
