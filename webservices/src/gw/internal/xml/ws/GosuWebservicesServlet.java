/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.ws;

import gw.internal.xml.ws.server.WebservicesServletBase;
import gw.internal.xml.ws.server.WsiServiceInfo;
import gw.lang.reflect.IType;

public final class GosuWebservicesServlet extends WebservicesServletBase {

  public GosuWebservicesServlet() {
  }

  // called reflectively
  @SuppressWarnings( { "UnusedDeclaration" } )
  public GosuWebservicesServlet( boolean wsiLocal ) {
    super( wsiLocal );
  }
  
  public void addWebService( String typeName ) {
    super.addWebService(typeName);
  }

  public void removeWebService( String typeName ) {
    super.removeWebService( typeName );
  }

  @Override
  public void postConfigureWebservice( IType type, WsiServiceInfo serviceInfo ) {
  }

}
