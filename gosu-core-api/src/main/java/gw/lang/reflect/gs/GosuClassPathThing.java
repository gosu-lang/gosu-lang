/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.Gosu;
import gw.lang.reflect.TypeSystem;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

/**
 */
public class GosuClassPathThing {

  private static final String PROTOCOL_PACKAGE = "gw.internal.gosu.compiler.protocols";

  private static void addGosuClassProtocolToClasspath() {
    try {
      URLClassLoaderWrapper urlLoader = findUrlLoader();
      URL url = makeURL();
      if (!urlLoader.getURLs().contains(url)) {
        urlLoader.addURL(url);
      }
    }
    catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  private static URL makeURL() throws MalformedURLException {
    String protocol = "gosuclass";
    URL url;
    try {
      url = new URL( null, protocol + "://honeybadger/" );
    }
    catch( Exception e ) {
      // If our Handler class is not in the system loader and not accessible within the Caller's
      // classloader from the URL constructor (3 activation records deep), then our Handler class
      // is not loadable by the URL class, but the honey badget  doesn't really care; it gets
      // what it wants.
      addOurProtocolHandler();
      url = new URL( null, protocol + "://honeybadger/" );
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
    String ours = "gw.internal.gosu.compiler.protocols";
    String protocols = System.getProperty( strProtocolProp );
    if( protocols != null ) {
      // Remove our protocol from the list
      protocols = protocols.replace( PROTOCOL_PACKAGE + '|' , "" );
      System.setProperty( strProtocolProp, protocols );
    }
  }

  private static URLClassLoaderWrapper findUrlLoader() {
    ClassLoader loader = TypeSystem.getGosuClassLoader().getActualLoader();
    if (loader instanceof URLClassLoader) {
      return new SunURLClassLoaderWrapper((URLClassLoader) loader);
    }
    else {
      Class<?> ijUrlClassLoaderClass = findSuperClass(loader.getClass(), IJUrlClassLoaderWrapper.CLASS_NAME);
      if (ijUrlClassLoaderClass != null) {
        return new IJUrlClassLoaderWrapper(loader, ijUrlClassLoaderClass);
      }
    }
    throw new IllegalStateException("class loader not identified as a URL-based loader: " + loader.getClass().getName());
  }

  private static Class<?> findSuperClass(Class<?> loaderClass, String possibleSuperClassName) {
    if (loaderClass == null) {
      return null;
    }
    if (loaderClass.getName().equals(possibleSuperClassName)) {
      return loaderClass;
    }
    return findSuperClass(loaderClass.getSuperclass(), possibleSuperClassName);
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
    addGosuClassProtocolToClasspath();
    return true;
  }

  public synchronized static void cleanup() {
    removeOurProtocolPackage();
    // XXX: We can't remove URL from classloader easily.
    //removeGosuClassProtocolToClasspath();
    removeOurProtocolHandler();
  }

  private static abstract class URLClassLoaderWrapper {
    final ClassLoader _loader;
    final Class _classLoaderClass;

    URLClassLoaderWrapper(ClassLoader loader, Class classLoaderClass) {
      _loader = loader;
      _classLoaderClass = classLoaderClass;
    }

    abstract void addURL(URL url);
    abstract List<URL> getURLs();

    Object invokeMethod(String methodName, Class<?>[] paramTypes, Object[] params) {
      try {
        Method method = _classLoaderClass.getDeclaredMethod( methodName, paramTypes );
        method.setAccessible(true);
        return method.invoke(_loader, params);
      } catch (NoSuchMethodException e) {
        throw new RuntimeException( e );
      } catch (IllegalAccessException e) {
        throw new RuntimeException( e );
      } catch (InvocationTargetException e) {
        throw new RuntimeException( e );
      }
    }
  }

  private static class SunURLClassLoaderWrapper extends URLClassLoaderWrapper {
    SunURLClassLoaderWrapper(URLClassLoader loader) {
      super(loader, URLClassLoader.class);
    }

    @Override
    void addURL(URL url) {
      invokeMethod("addURL", new Class[] { URL.class }, new Object[] { url });
    }

    @Override
    List<URL> getURLs() {
      return Arrays.asList(((URLClassLoader) _loader).getURLs());
    }
  }

  private static class IJUrlClassLoaderWrapper extends URLClassLoaderWrapper {
    static final String CLASS_NAME = "com.intellij.util.lang.UrlClassLoader";

    IJUrlClassLoaderWrapper(ClassLoader loader, Class<?> classLoaderClass) {
      super(loader, classLoaderClass);
    }

    @Override
    void addURL(URL url) {
      invokeMethod("addURL", new Class<?>[] { URL.class }, new Object[] { url });
    }

    @Override
    List<URL> getURLs() {
      //noinspection unchecked
      return (List<URL>) invokeMethod("getUrls", new Class<?>[0], new Object[0]);
    }
  }
}
