/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.debugger;

public interface RemoteServiceProtocolHandler
{
  public String call( String strSessionId, String strService, String strMethod, String strParamTypes, String strArgs ) throws Exception;
}
