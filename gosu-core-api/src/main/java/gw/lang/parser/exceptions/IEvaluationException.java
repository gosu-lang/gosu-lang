

/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

public abstract class IEvaluationException extends RuntimeException {

  protected IEvaluationException(String msg ) {
    super(msg);
  }

  protected IEvaluationException(Throwable t ) {
    super(t.getMessage(), t);
  }

  public abstract void setAdditionalDetails(String details);

}