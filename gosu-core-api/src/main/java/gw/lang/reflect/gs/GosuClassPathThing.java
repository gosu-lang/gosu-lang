/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.Gosu;
import gw.lang.reflect.TypeSystem;
import gw.util.concurrent.ConcurrentHashSet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 */
public class GosuClassPathThing {

  private static final String PROTOCOL_PACKAGE = "gw.internal.gosu.compiler.protocols";
  private static final Set<Integer> VISITED_LOADER_IDS = new ConcurrentHashSet<Integer>();

  private static void setupLoaderChainWithGosuUrl( ClassLoader loader ) {
    URLClassLoaderWrapper wrapped = URLClassLoaderWrapper.wrap( loader );
    if( wrapped != null ) {
      addGosuClassUrl( wrapped );
    }
    loader = loader.getParent();
    if( loader != null ) {
      setupLoaderChainWithGosuUrl( loader );
    }
  }

  private static void addGosuClassUrl( URLClassLoaderWrapper urlLoader ) {
    try {
      URL url = makeUrl( urlLoader._loader );
      if( !urlLoader.getURLs().contains( url ) ) {
        urlLoader.addURL( url );
      }
    }
    catch( MalformedURLException e ) {
      throw new RuntimeException( e );
    }
  }

  private static URL makeUrl( ClassLoader loader ) throws MalformedURLException {
    String spec = "gosuclass://" + System.identityHashCode( loader ) + "/";
    URL url;
    try {
      url = new URL( null, spec );
    }
    catch( Exception e ) {
      // If our Handler class is not in the system loader and not accessible within the Caller's
      // classloader from the URL constructor (3 activation records deep), then our Handler class
      // is not loadable by the URL class, but the honey badger doesn't really care; it gets
      // what it wants.
      addOurProtocolHandler();
      url = new URL( null, spec );
    }
    return url;
  }

  private static void addOurProtocolHandler() {
    try {
      Field field = URL.class.getDeclaredField( "handlers" );
      field.setAccessible( true );
      Method put = Hashtable.class.getMethod( "put", Object.class, Object.class );
      Field instanceField = Class.forName( "gw.internal.gosu.compiler.protocols.gosuclass.Handler" ).getField( "INSTANCE" );
      Object handler = instanceField.get( null );
      put.invoke( field.get( null ), "gosuclass", handler );
    } catch (Exception e) {
      throw new IllegalStateException("Failed to configure gosu protocol handler", e);
    }
  }

  private static void removeOurProtocolHandler() {
    try {
      Field field = URL.class.getDeclaredField( "handlers" );
      field.setAccessible( true );
      Method remove = Hashtable.class.getMethod( "remove", Object.class );
      remove.invoke( field.get( null ), "gosuclass" );
    } catch (Exception e) {
      throw new IllegalStateException("Failed to cleanup gosu protocol handler", e);
    }
  }

  private static boolean addOurProtocolPackage() {
    // XXX: Do not add protocol package since OSGi implementation of URLStreamFactory
    // first delegates to those and only then calls service from Service Registry
    String strProtocolProp = "java.protocol.handler.pkgs";
    String protocols = PROTOCOL_PACKAGE;
    String oldProp = System.getProperty( strProtocolProp );
    if( oldProp != null ) {
      if( oldProp.contains( PROTOCOL_PACKAGE ) ) {
        return false;
      }
      protocols += '|' + oldProp;
    }
    System.setProperty( strProtocolProp, protocols );
    return true;
  }

  private static void removeOurProtocolPackage() {
    String strProtocolProp = "java.protocol.handler.pkgs";
    String protocols = System.getProperty( strProtocolProp );
    if( protocols != null ) {
      // Remove our protocol from the list
      protocols = protocols.replace( PROTOCOL_PACKAGE + '|' , "" );
      System.setProperty( strProtocolProp, protocols );
    }
  }

  public synchronized static boolean init() {
    if( addOurProtocolPackage() ) {
      if( Gosu.bootstrapGosuWhenInitiatedViaClassfile() ) {
        // Assuming we are in runtime, we push the root module in the case where the process was started with java.exe and not gosu.cmd
        // In other words a Gosu class can be loaded directly from classfile in a bare bones Java program where only the Gosu runtime is
        // on the classpath and no module was pushed prior to loading.
        TypeSystem.pushModule( TypeSystem.getGlobalModule() );
      }
    }
    setupLoaderChainWithGosuUrl( TypeSystem.getGosuClassLoader().getActualLoader() );
    return true;
  }

  public synchronized static void cleanup() {
    removeOurProtocolPackage();
    // XXX: We can't remove URL from classloader easily.
    //removeGosuClassProtocolToClasspath();
    removeOurProtocolHandler();
  }

  private static class URLClassLoaderWrapper {
    final ClassLoader _loader;
    final Method _getURLs;
    final Method _addUrl;

    static URLClassLoaderWrapper wrap( ClassLoader loader ) {
      int loaderId = System.identityHashCode( loader );
      if( VISITED_LOADER_IDS.contains( loaderId ) ) {
        // Already visited
        return null;
      }
      VISITED_LOADER_IDS.add( loaderId );
      Method getURLs = findMethod( loader.getClass(), "getURLs", new Class[0], List.class, URL[].class );
      if( getURLs != null ) {
        Method addUrl = findMethod( loader.getClass(), "addUrl", new Class[] {URL.class}, void.class );
        if( addUrl != null ) {
          return new URLClassLoaderWrapper( loader, getURLs, addUrl );
        }
      }
      return null;
    }

    private static Method findMethod( Class cls, String methodName, Class[] paramTypes, Class... returnType ) {
      outer: for( Method m: cls.getDeclaredMethods() ) {
        if( m.getName().equalsIgnoreCase( methodName ) ) {
          Class<?>[] types = m.getParameterTypes();
          if( types.length == paramTypes.length ) {
            for( int i = 0; i < paramTypes.length; i++ ) {
              if( !paramTypes[i].equals( types[i] ) ) {
                continue outer;
              }
            }
            for( Class t: returnType ) {
              if( m.getReturnType().equals( t ) ) {
                m.setAccessible( true );
                return m;
              }
            }
          }
        }
      }
      return cls.getSuperclass() != null ? findMethod( cls.getSuperclass(), methodName, paramTypes, returnType ) : null;
    }

    private URLClassLoaderWrapper( ClassLoader loader, Method getURLs, Method addUrl ) {
      _loader = loader;
      _getURLs = getURLs;
      _addUrl = addUrl;
    }

    void addURL(URL url) {
      try {
        _addUrl.invoke( _loader, new Object[] {url} );
      }
      catch( Exception e ) {
        throw new RuntimeException( e );
      }
    }

    List<URL> getURLs() {
      if( _loader instanceof URLClassLoader ) {
        return Arrays.asList( ((URLClassLoader)_loader).getURLs() );
      }

      try {
        Object invoke = _getURLs.invoke( _loader );
        if( invoke.getClass().isArray() ) {
          invoke = Arrays.asList( (Object[])invoke );
        }
        return (List<URL>)invoke;
      }
      catch( Exception e ) {
        throw new RuntimeException( e );
      }
    }

  }
}
