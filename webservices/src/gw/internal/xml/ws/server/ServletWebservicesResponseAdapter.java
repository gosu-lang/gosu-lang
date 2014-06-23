/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server;

import gw.util.ILogger;
import gw.xml.XmlException;
import gw.xml.ws.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;


public class ServletWebservicesResponseAdapter extends WebservicesResponseAdapter {


  private final HttpServletResponse _response;
  private HttpHeaders _httpHeaders;
  private final ILogger _logger;

  public ServletWebservicesResponseAdapter( HttpServletResponse response, ILogger logger ) {
    _response = response;
    _logger = logger;
  }

  @Override
  public void sendError( int error, XmlException ex ) throws IOException {
    _response.setStatus( error );
  }

  @Override
  public void setStatus( int status ) {
    _response.setStatus( status );
  }

  @Override
  public void setContentType( String contentType ) {
    _response.setContentType( contentType );
  }

  @Override
  public OutputStream getOutputStream() throws IOException {
    // if any io exceptions occur while writing to the output stream, we should just debug log, but otherwise ignore those errors...
    return new DebugLoggingOutputStream( _response.getOutputStream(), _logger );
  }

  @Override
  public HttpHeaders getHttpHeaders() {
    if ( _httpHeaders == null ) {
      _httpHeaders = new HttpHeaders();
    }
    return _httpHeaders;
  }

  public void commitHttpHeaders() {
    if ( _httpHeaders != null ) {
      for ( String headerName : _httpHeaders.getHeaderNames() ) {
        String headerValue = _httpHeaders.getHeader( headerName );
        _response.setHeader( headerName, headerValue );
      }
    }
  }

}
