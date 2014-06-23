/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml;

import gw.lang.PublishInGosu;

/**
 * This exception is thrown when a single content matching a certain criteria is expected
 * and multiple contents are found.
 */
@PublishInGosu
public final class MultipleContentMatchesException extends XmlException {

  /**
   * Creates a new instance of this exception class.
   * @param msg The exception message
   */
  public MultipleContentMatchesException( String msg ) {
    super( msg );
  }

}
