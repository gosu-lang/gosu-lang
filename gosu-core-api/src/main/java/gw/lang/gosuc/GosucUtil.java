/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import gw.config.CommonServices;
import gw.fs.IDirectory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

  public static IDirectory getDirectoryForPath( String path ) {
    return CommonServices.getFileSystem().getIDirectory( new File( path ) );
  }

  public static List<String> makeStringPaths( List<IDirectory> sourcePaths ) {
    List<String> paths = new ArrayList<String>();
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

  public static List<IDirectory> toDirectories(List<String> paths) {
    List<IDirectory> dirs = new ArrayList<IDirectory>();
    for (String path : paths) {
      dirs.add(CommonServices.getFileSystem().getIDirectory(new File(path)));
    }
    return dirs;
  }
}
