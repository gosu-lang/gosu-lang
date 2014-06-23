/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws;


import gw.internal.ext.org.mortbay.jetty.Connector;
import gw.internal.ext.org.mortbay.jetty.servlet.Context;
import gw.internal.ext.org.mortbay.jetty.servlet.ServletHolder;
import gw.internal.xml.ws.GosuWebservicesServlet;
import gw.internal.xml.ws.server.ServerAnnotationVerifier;
import gw.lang.PublishInGosu;
import gw.lang.parser.IUsageSiteValidatorReference;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.util.GosuExceptionUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows easy launch of an embedded web server to serve Webservices.
 */
@PublishInGosu
public final class Server {

  private static final Map<Integer,Server> _servers = new HashMap<Integer, Server>();

  private boolean _started;
  private gw.internal.ext.org.mortbay.jetty.Server _server;
  private GosuWebservicesServlet _servlet;
  private Integer _registeredPort;

  private Server( Integer registeredPort, int port ) {
    _registeredPort = registeredPort;
    try {
      _server = new gw.internal.ext.org.mortbay.jetty.Server( port );
      Context context = new Context( _server, "/" );
      ServletHolder servletHolder = new ServletHolder();
      _servlet = new GosuWebservicesServlet();
      servletHolder.setServlet( _servlet );
      context.addServlet( servletHolder, "/*" );
    }
    catch ( Exception ex ) {
      throw GosuExceptionUtil.forceThrow( ex );
    }
  }

  /**
   * Publishes a type annotated with @WsiWebService on a port.
   * @param type the type to publish
   */
  @IUsageSiteValidatorReference(ServerAnnotationVerifier.class)
  public void publish( IType type ) {
    TypeSystem.lock();
    try {
      String errorMessage = GosuWebservicesServlet.checkWebServiceForErrors( type );
      if ( errorMessage != null ) {
        throw new IllegalArgumentException( type.getName() + " is not a valid webservice: " + errorMessage );
      }
      _servlet.addWebService( type.getName() );
    }
    catch ( Exception ex ) {
      throw GosuExceptionUtil.forceThrow( ex );
    }
    finally {
      TypeSystem.unlock();
    }
  }

  /**
   * Starts a server on the specified port. If the server has already been started, calling this method has no
   * effect.
   */
  public void start() {
    TypeSystem.lock();
    try {
      if ( _started ) {
        return;
      }
      _server.start();
      _started = true;
    }
    catch ( Exception ex ) {
      throw GosuExceptionUtil.forceThrow( ex );
    }
    finally {
      TypeSystem.unlock();
    }
  }

  /**
   * Stops a server on the specified port. Once stopped, the server is no longer valid, and cannot be started again.
   * Any future call to get() for this server's port will cause a new server to be created.
   */
  public void stop() {
    try {
      _server.stop();
    }
    catch ( Exception ex ) {
      throw GosuExceptionUtil.forceThrow( ex );
    }
    TypeSystem.lock();
    try {
      if ( ! _started ) {
        return;
      }
      //noinspection ObjectEquality
      if ( _registeredPort != null && _servers.get( _registeredPort ) == this ) {
        _servers.remove( _registeredPort );
      }
    }
    finally {
      TypeSystem.unlock();
    }
  }

  /**
   * Returns the local port of the server. This method can only be used once the server has been started.
   * @return the local port of the server
   */
  public int getPort() {
    if ( ! _started ) {
      throw new IllegalStateException( "Server has not been started" );
    }
    try {
      Connector[] connectors = _server.getConnectors();
      return connectors[0].getLocalPort();
    }
    catch ( Exception ex ) {
      throw GosuExceptionUtil.forceThrow( ex );
    }
  }

  /**
   * Returns the server on the specified port, creating it if needed.
   * @param port the port, or zero for a random port. The random port number can be queried after starting the server by using the getPort() method.
   * @return the server
   */
  public static Server get( int port ) {
    if ( port < 0 ) {
      throw new IllegalArgumentException( "Port cannot be less than zero" );
    }
    TypeSystem.lock();
    try {
      Server server = _servers.get( port );
      if ( server == null ) {
        server = new Server( port, port );
        _servers.put( port, server );
      }
      return server;
    }
    finally {
      TypeSystem.unlock();
    }
  }

  /**
   * Creates a new server on a random port and returns it. This server will not be accessible through the get() method.
   * @param port the port, or zero for a random port. The random port number can be queried after starting the server by using the getPort() method.
   * @return the created server
   */
  public static Server newPrivateServer( int port ) {
    if ( port < 0 ) {
      throw new IllegalArgumentException( "Port cannot be less than zero" );
    }
    return new Server( null, port );
  }
}
