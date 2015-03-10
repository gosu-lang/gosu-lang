/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.config.ExecutionMode;
import gw.fs.IDirectory;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IJreModule;
import gw.lang.reflect.module.IModule;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleClassLoader extends URLClassLoader implements IModuleClassLoader {
  private IModule _module;

  private ModuleClassLoader(URL[] urls, ClassLoader parent, IModule module) {
    super(urls, parent);
    _module = module;
    super.addURL(null);
  }

  public Class<?> loadLocalClass(String name, boolean resolve) throws ClassNotFoundException {
    // Load class without traversing dependent modules.
    // Note: look in the parent classloader first. That means, all modules try to load class from
    // JRE module first and only then from itself.
    return super.loadClass(name, resolve);
  }

  public boolean isDeferToParent() {
    return getURLs().length == 0 && (_module == _module.getExecutionEnvironment().getJreModule());
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    if ( _module == null) {
      throw new RuntimeException("This loader has been disposed and cannot be used for class loading purposes.");
    }
    if (name.equalsIgnoreCase("con") || name.toLowerCase().endsWith(".con")) {
      return null;
    }

    Throwable t;
    try {
      Class<?> aClass = loadLocalClass(name, resolve);
      return aClass;
    } catch (ClassNotFoundException e1) {
      // Ok, not found locally
      t = e1;
    } catch (NoClassDefFoundError e2) {
      // XXX-isd: One of the class dependencies could not be loaded -- treat as class not being found.
      t = e2;
    }

    for (IModule m : _module.getModuleTraversalList()) {
      if (m != _module && m != TypeSystem.getGlobalModule() ) {
        ClassLoader moduleClassLoader = m.getModuleClassLoader();
        Class aClass;
        try {
          if (moduleClassLoader instanceof IModuleClassLoader) {
            aClass = ((IModuleClassLoader) moduleClassLoader).loadLocalClass(name, resolve);
          } else {
            aClass = moduleClassLoader.loadClass(name);
          }
          return aClass;
        } catch (ClassNotFoundException e2) {
          // Ignore
        }
      }
    }
    throw new ClassNotFoundException("Class not found at all: " + name, t);
  }

  public static ClassLoader create(IModule module) {
    List<IDirectory> javaClassPath = module.getJavaClassPath();
    List<URL> urls = new ArrayList<URL>(javaClassPath.size());
    for (IDirectory entry : javaClassPath) {
      try {
        urls.add(entry.toURI().toURL());
      } catch (MalformedURLException e) {
        throw new RuntimeException(e);
      }
    }

    IExecutionEnvironment environment = module.getExecutionEnvironment();
    if (ExecutionMode.isRuntime()) {
      // XXX-isd: we need discardable classloader for JRE module, so we can use it for defining throw-away proxy classes.
      urls = Collections.emptyList();
      //return CommonServices.getEntityAccess().getPluginClassLoader();
    }

    // JRE module delegates to plugin classloader, all other modules delegate to JRE module first.
    ClassLoader parent = (module == environment.getJreModule()) ?
            CommonServices.getEntityAccess().getPluginClassLoader() :
            null; //environment.getJreModule().getModuleClassLoader();

    return new ModuleClassLoader(urls.toArray(new URL[urls.size()]), parent, module);
  }

  public void dispose() {
    _module = null;
  }
}
