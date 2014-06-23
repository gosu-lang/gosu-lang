/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws;

import gw.lang.PublishInGosu;

/**
 * This exception is thrown when there is a problem with a loaded WSDL.
 */
@PublishInGosu
public class WsdlValidationException extends RuntimeException {

  /**
   * Constructor
   */
  public WsdlValidationException(String message){
    super( message );
  }

  /**
   * Constructor
   */
  public WsdlValidationException( String message, Throwable cause ) {
    super( message, cause );
  }
}
