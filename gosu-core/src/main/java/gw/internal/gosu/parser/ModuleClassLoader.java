/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.config.ExecutionMode;
import gw.fs.IDirectory;
import gw.lang.reflect.TypeSystem;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleClassLoader extends URLClassLoader implements IModuleClassLoader {
  private ModuleClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
    super.addURL(null);
  }

  public Class<?> loadLocalClass(String name, boolean resolve) throws ClassNotFoundException {
    // Load class without traversing dependent modules.
    // Note: look in the parent classloader first. That means, all modules try to load class from
    // JRE module first and only then from itself.
    return super.loadClass(name, resolve);
  }

  public boolean isDeferToParent() {
    return getURLs().length == 0;
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    if (name.equalsIgnoreCase("con") || name.toLowerCase().endsWith(".con")) {
      return null;
    }

    Throwable t;
    try {
      return loadLocalClass(name, resolve);
    }
    catch( ClassNotFoundException | NoClassDefFoundError e1) {
      // Ok, not found locally
      t = e1;
    }

    throw new ClassNotFoundException("Class not found at all: " + name, t);
  }

  public static ClassLoader create() {
    List<IDirectory> javaClassPath = TypeSystem.getModule().getJavaClassPath();
    List<URL> urls = new ArrayList<>( javaClassPath.size() );
    for (IDirectory entry : javaClassPath) {
      try {
        urls.add(entry.toURI().toURL());
      } catch (MalformedURLException e) {
        throw new RuntimeException(e);
      }
    }

    if (ExecutionMode.isRuntime()) {
      urls = Collections.emptyList();
    }

    // JRE module delegates to plugin classloader, all other modules delegate to JRE module first.
    ClassLoader parent = CommonServices.getEntityAccess().getPluginClassLoader();

    return new ModuleClassLoader(urls.toArray( new URL[0] ), parent);
  }

  public void dispose() {
  }
}
