/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import gw.util.GosuStringUtil;
import gw.xml.ws.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

public class ServletWebservicesRequest extends WebservicesRequest {

  private final HttpServletRequest _request;

  public ServletWebservicesRequest( HttpServletRequest request ) {
    _request = request;
  }

  @Override
  public String getPathInfo() {
    return _request.getPathInfo();
  }

  @Override
  public String getQueryString() {
    return _request.getQueryString();
  }

  @Override
  public HttpServletRequest getHttpServletRequest() {
    return _request;
  }

  @Override
  public String getRequestURL() {
    return _request.getRequestURL().toString();
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return _request.getInputStream();
  }

  @Override
  public HttpHeaders getHttpHeaders() {
    HttpHeaders ret = new HttpHeaders();
    Enumeration e = _request.getHeaderNames();
    while ( e.hasMoreElements() ) {
      String headerName = (String) e.nextElement();
      List<String> values = new ArrayList<String>();
      Enumeration e2 = _request.getHeaders( headerName );
      while ( e2.hasMoreElements() ) {
        String value = (String) e2.nextElement();
        values.add( value );
      }
      ret.setHeader( headerName, GosuStringUtil.join( ", ", values ) );
    }
    return ret;
  }

  @Override
  public void createSession() {
    _request.getSession( true );
  }

}
