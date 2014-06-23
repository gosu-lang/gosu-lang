/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws;

public class WebServiceException extends RuntimeException {

  private boolean _senderFault;

  public WebServiceException() {
  }

  public WebServiceException( Throwable cause ) {
    super( cause );
  }

  public WebServiceException( String message ) {
    super( message );
  }

  public WebServiceException( String message, Throwable cause ) {
    super( message, cause );
  }

  public WebServiceException( boolean senderFault ) {
    _senderFault = senderFault;
  }

  public WebServiceException( Throwable cause, boolean senderFault ) {
    super( cause );
    _senderFault = senderFault;
  }

  public WebServiceException( String message, boolean senderFault ) {
    super( message );
    _senderFault = senderFault;
  }

  public WebServiceException( String message, Throwable cause, boolean senderFault ) {
    super( message, cause );
    _senderFault = senderFault;
  }

  public boolean isSenderFault() {
    return _senderFault;
  }

}
