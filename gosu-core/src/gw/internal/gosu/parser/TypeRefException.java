/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

public class TypeRefException extends RuntimeException {

  public TypeRefException(String message) {
    super(message);
  }

  public TypeRefException(String message, Throwable cause) {
    super(message, cause);
  }

  public TypeRefException(Throwable cause) {
    super(cause);
  }

}
