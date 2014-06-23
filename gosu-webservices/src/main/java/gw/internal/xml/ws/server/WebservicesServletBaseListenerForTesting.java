/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.server;

import javax.servlet.http.HttpServletRequest;

public interface WebservicesServletBaseListenerForTesting {

  public void requestComplete( HttpServletRequest req, Throwable t );

}
