/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.intellij.openapi.project.Project;
import gw.lang.reflect.gs.IGosuClass;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.WeakHashMap;

/**
 */
public class ProjectClassLoader extends URLClassLoader {

  private static final String[]
    PREFIXES = {
      "gw.internal.",
      "gw.lang.",
      "gw.plugin."
    };

  private static final Map<Project, ProjectClassLoader>
    PRJ_TO_CLASSLOADER = new WeakHashMap<>();

  public static ProjectClassLoader get( Project project ) {
    synchronized( PRJ_TO_CLASSLOADER ) {
      ProjectClassLoader classLoader = PRJ_TO_CLASSLOADER.get( project );
      if( classLoader == null ) {
        PRJ_TO_CLASSLOADER.put( project, classLoader = new ProjectClassLoader() );
      }
      return classLoader;
    }
  }

  private ProjectClassLoader() {
    super( getUrls(), ProjectClassLoader.class.getClassLoader() );
  }

  @Override
  protected Class<?> loadClass( @NotNull String name, boolean resolve ) throws ClassNotFoundException {
    // If _this_ loader has already loaded the class, return it
    Class cls = findLoadedClass( name );
    if( cls != null ) {
      return cls;
    }

    // If the class is a Gosu class or a Plugin class, this loader msut load it
    for( String prefix : PREFIXES ) {
      if( name.startsWith( prefix ) ) {
        return super.loadClass( name, resolve );
      }
    }

    // Otherwise, let the parent classloader load it
    cls = getParent().loadClass( name );
    if( cls != null && resolve ) {
      resolveClass( cls );
    }
    return cls;
  }

  @NotNull
  private static URL[] getUrls() {
    try {
      URL urlMain = ProjectClassLoader.class.getProtectionDomain().getCodeSource().getLocation();
      Class<?> internalClass = Class.forName( "gw.internal.gosu.parser.IGosuClassInternal" );
      URL urlCoreGosu = internalClass.getProtectionDomain().getCodeSource().getLocation();
      URL urlCoreGosuApi = IGosuClass.class.getProtectionDomain().getCodeSource().getLocation();
      if( urlMain.equals( urlCoreGosu ) ) {
        return new URL[] {urlMain};
      }
      return new URL[] {urlMain, urlCoreGosu, urlCoreGosuApi};
    }
    catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }
}
