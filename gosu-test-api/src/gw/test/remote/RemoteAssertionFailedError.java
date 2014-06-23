/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test.remote;

import junit.framework.AssertionFailedError;

import java.io.PrintStream;
import java.io.PrintWriter;

public class RemoteAssertionFailedError extends AssertionFailedError {

  private String _className;

  public RemoteAssertionFailedError(String message, String className, StackTraceElement[] stes) {
    super(message);
    _className = className;
    setStackTrace(stes);
  }

  @Override
  public String toString() {
    String message = getLocalizedMessage();
    return (message != null) ? (_className + ": " + message) : _className;
  }
}
