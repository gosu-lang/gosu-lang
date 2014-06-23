/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server;

import java.io.IOException;
import gw.xml.ws.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

/**
 * Represents a request to a WSI web service.
 */
public abstract class WebservicesRequest {

  /**
   * Returns the URL path information of the request.
   */
  public abstract String getPathInfo();

  /**
   * Returns the URL query string of the request.
   */
  public abstract String getQueryString();

  /**
   * Returns the request URL.
   */
  public abstract String getRequestURL();

  /**
   * Returns an input stream to read the request content.
   */
  public abstract InputStream getInputStream() throws IOException;

  /**
   * Returns the HTTP headers of the request.
   */
  public abstract HttpHeaders getHttpHeaders();

  /**
   * Causes an application server session to be created.
   */
  public abstract void createSession();

  /**
   * Returns the HttpServletRequest, or null if this is not an HTTP servlet request.
   * @return
   */
  public abstract HttpServletRequest getHttpServletRequest();

}
