/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import manifold.util.ManExceptionUtil;

/**
 */
public class GosucUtil {
  public static String indent( String in ) {
    StringBuilder sb = new StringBuilder( in );
    sb.insert( 0, "  " );
    for( int i = 0; i < sb.length(); i++ ) {
      char c = sb.charAt( i );
      if( c == '\n' ) {
        sb.insert( i + 1, "  " );
      }
    }
    return sb.toString();
  }

  public static IDirectory getDirectoryForPath( String uriPath ) {
    return CommonServices.getFileSystem().getIDirectory( Paths.get( URI.create( uriPath ) ) );
  }

  public static List<String> makeStringPaths( List<IDirectory> sourcePaths ) {
    List<String> paths = new ArrayList<>();
    for( IDirectory dir: sourcePaths ) {
      paths.add( dir.getPath().getPathString() );
    }
    return paths;
  }

  public static URL toURL( File file ) {
    try {
      return file.toURI().toURL();
    }
    catch( MalformedURLException e ) {
      System.err.println( "Could not get URL for " + file );
      return null;
    }
  }

  public static List<IDirectory> toDirectories(List<String> uriPaths) {
    List<IDirectory> dirs = new ArrayList<>();
    for (String uri : uriPaths) {
      dirs.add( CommonServices.getFileSystem().getIDirectory( Paths.get( URI.create( uri ) ) ) );
    }
    return dirs;
  }

  public static List<String> getGosuBootstrapJars() {
    return Arrays.asList(getClassLocation("gw.internal.gosu.parser.MetaType"), //get gosu-core
        getClassLocation("gw.lang.Gosu"), //get gosu-core-api
        getClassLocation("manifold.api.host.IManifoldHost"), //get manifold core
//        getClassLocation("manifold.ext.ExtensionMethod"), //get manifold-ext
        getClassLocation("manifold.util.ReflectUtil"), //get manifold-util
        //getClassLocation("com.github.benmanes.caffeine.cache.Caffeine"), //get caffeine
        getClassLocation("gw.internal.ext.org.objectweb.asm.ClassWriter"), //get asm
        getClassLocation("gw.internal.ext.com.beust.jcommander.JCommander") //get jcommander
    );
  }

  public static String getClassLocation( String className )
  {
    Class clazz;
    try
    {
      clazz = Class.forName( className );
    }
    catch( ClassNotFoundException cnfe )
    {
      throw new RuntimeException( "Unable to locate Gosu libraries in classpath.\n", cnfe );
    }

    ProtectionDomain pDomain = clazz.getProtectionDomain();
    CodeSource cSource = pDomain.getCodeSource();

    if( cSource != null )
    {
      URL loc = cSource.getLocation();
      URI file;
      try
      {
        file = loc.toURI();
      }
      catch( Exception e )
      {
        throw ManExceptionUtil.unchecked( e );
      }
      return file.toString();
    }
    else
    {
      throw new RuntimeException( "Cannot find the location of the requested className <" + className + "> in classpath." );
    }
  }

}
