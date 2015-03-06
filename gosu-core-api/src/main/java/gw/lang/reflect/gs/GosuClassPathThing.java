/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.Gosu;
import gw.lang.reflect.TypeSystem;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
public class GosuClassPathThing {
  public static final String GOSU_CLASS_PROTOCOL = "gosuclass";
  private static final String PROTOCOL_PACKAGE = "gw.internal.gosu.compiler.protocols";
  private static Boolean CAN_WRAP = null;

  private static void setupLoaderChainWithGosuUrl( ClassLoader loader ) {
    UrlClassLoaderWrapper wrapped = UrlClassLoaderWrapper.wrapIfNotAlreadyVisited( loader );
    if( wrapped == null ) {
      return;
    }
    addGosuClassUrl( wrapped );
    if( canWrapChain() ) {
      if( loader != ClassLoader.getSystemClassLoader() ) { // we don't bother messing with any loaders above the system loader e.g., ExtClassLoader
        loader = loader.getParent();
        if( loader != null ) {
          setupLoaderChainWithGosuUrl( loader );
        }
      }
    }
  }

  /*
    We don't currently wrap the chain of loaders for WebSphere or WebLogic or JBoss
    because they use "module" class loaders that are not URLClassLoader-like.  We
    can maybe someday handle them seperately.

    IBM class loader chain:
    ~~~~~~~~~~~~~~~~~~~~~~~
    com.guidewire.pl.system.gosu.GosuPluginContainer ->
       com.guidewire.pl.system.integration.plugins.PluginContainer ->
         com.guidewire.pl.system.integration.plugins.SharedPluginContainer ->
           com.guidewire.pl.system.integration.plugins.PluginContainer ->

             [weblogic.utils.classloaders.ChangeAwareClassLoader ->
               weblogic.utils.classloaders.FilteringClassLoader ->
                 weblogic.utils.classloaders.GenericClassLoader]* ->

                   sun.misc.Launcher$AppClassLoader ->
                     sun.misc.Launcher$ExtClassLoader ->
                       <null>

    WebLogic class loader chain:
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    com.guidewire.pl.system.gosu.GosuPluginContainer ->
       com.guidewire.pl.system.integration.plugins.PluginContainer ->
         com.guidewire.pl.system.integration.plugins.SharedPluginContainer ->
            com.guidewire.pl.system.integration.plugins.PluginContainer ->

              org.jboss.modules.ModuleClassLoader ->

                sun.misc.Launcher$AppClassLoader ->
                  sun.misc.Launcher$ExtClassLoader ->
                    <null>

   */
  private static boolean canWrapChain( ClassLoader loader ) {
    if( loader == null ) {
      return false;
    }
    UrlClassLoaderWrapper wrapped = UrlClassLoaderWrapper.wrap( loader );
    boolean bSysLoader = loader == ClassLoader.getSystemClassLoader();
    if( bSysLoader ) {
      return wrapped != null;
    }
    loader = loader.getParent();
    return wrapped != null && canWrapChain( loader );
  }

  private static void addGosuClassUrl( UrlClassLoaderWrapper urlLoader ) {
    try {
      URL url = makeUrl( urlLoader.getLoader() );
      if( !urlLoader.getURLs().contains( url ) ) {
        urlLoader.addURL( url );
      }
    }
    catch( MalformedURLException e ) {
      throw new RuntimeException( e );
    }
  }

  private static URL makeUrl( ClassLoader loader ) throws MalformedURLException {
    int loaderAddress = System.identityHashCode( loader );
    String spec = GOSU_CLASS_PROTOCOL + "://" + loaderAddress + "/";
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
    ClassLoader loader = TypeSystem.getGosuClassLoader().getActualLoader();
    makeLoaderChainLockOnSystemLoader( loader );
    setupLoaderChainWithGosuUrl( loader );
    return true;
  }

  public static boolean canWrapChain() {
    return CAN_WRAP == null ? CAN_WRAP = canWrapChain( TypeSystem.getGosuClassLoader().getActualLoader() ) : CAN_WRAP;
  }

  private static void makeLoaderChainLockOnSystemLoader( ClassLoader loader )
  {
    makeLoaderUseSystemLoaderLock( loader );
    if( loader != ClassLoader.getSystemClassLoader() ) // we don't bother messing with any loaders above the system loader e.g., ExtClassLoader
    {
      loader = loader.getParent();
      if( loader != null )
      {
        makeLoaderChainLockOnSystemLoader( loader );
      }
    }
  }

  private static void makeLoaderUseSystemLoaderLock( ClassLoader loader )
  {
    try
    {
      Field field = ClassLoader.class.getDeclaredField( "parallelLockMap" );
      field.setAccessible( true );
      field.set( loader, new FuMap() ); // make the loader use the system class loader's monitor

      field = ClassLoader.class.getDeclaredField( "package2certs" );
      field.setAccessible( true );
      field.set( loader, new ConcurrentHashMap() );

      field = ClassLoader.class.getDeclaredField( "assertionLock" );
      field.setAccessible( true );
      field.set( loader, ClassLoader.getSystemClassLoader() );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  public synchronized static void cleanup() {
    removeOurProtocolPackage();
    // XXX: We can't remove URL from classloader easily.
    //removeGosuClassProtocolToClasspath();
    removeOurProtocolHandler();
  }

  private static class FuMap extends ConcurrentHashMap<String, Object>
  {
    @Override
    public Object put( String key, Object value )
    {
      return null;
    }

    @Override
    public void putAll( Map<? extends String, ?> m )
    {
    }

    @Override
    public Object putIfAbsent( String key, Object value )
    {
      return ClassLoader.getSystemClassLoader();
    }

    @Override
    public Object get( Object key )
    {
      return ClassLoader.getSystemClassLoader();
    }
  }

}
