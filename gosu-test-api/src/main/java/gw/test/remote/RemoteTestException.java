/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test.remote;

import java.io.PrintStream;
import java.io.PrintWriter;

public class RemoteTestException extends RuntimeException {

  private String _className;

  public RemoteTestException(String message, Throwable cause, String className, StackTraceElement[] stes) {
    super(message, cause);
    _className = className;
    setStackTrace(stes);
  }

  @Override
  public String toString() {
    String message = getLocalizedMessage();
    return (message != null) ? (_className + ": " + message) : _className;
  }
}
