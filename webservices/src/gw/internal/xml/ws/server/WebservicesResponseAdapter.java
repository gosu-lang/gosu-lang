/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server;

import gw.xml.XmlException;
import gw.xml.ws.HttpHeaders;

import java.io.IOException;
import java.io.OutputStream;

public abstract class WebservicesResponseAdapter {

  public abstract void sendError( int error, XmlException ex ) throws IOException;

  public abstract void setStatus( int status );

  public abstract void setContentType( String contentType );

  public abstract OutputStream getOutputStream() throws IOException;

  public abstract HttpHeaders getHttpHeaders();

  public abstract void commitHttpHeaders();
  
}
