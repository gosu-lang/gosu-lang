/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu;

import gw.internal.gosu.parser.DefaultEntityAccess;

import java.net.URL;
import java.io.File;
import java.net.URLClassLoader;

public class ShellEntityAccess extends DefaultEntityAccess
{
  private static final URLClassLoader _classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();

  @Override
  public URLClassLoader getPluginClassLoader()
  {
    return _classLoader;
  }

  public StringBuilder getPluginRepositories()
  {
    StringBuilder sbFileRepositories = new StringBuilder();
    addPluginRepositories( sbFileRepositories );
    return sbFileRepositories;
  }

  private void addPluginRepositories( StringBuilder sbFileRepositories )
  {
//## this is crazy, it adds everything under the sun, i don;t think we need anything here since gosu classes load in the same loader as java classes
//    URL[] ls = _classLoader.getURLs();
//    for( URL url : ls )
//    {
//      String strFileName = URLDecoder.decode(url.getFile());
//      if( strFileName.startsWith( "//" ) || strFileName.startsWith( "/" ) )
//      {
//        String fileName = strFileName.substring( 1 );
//        if (new File(fileName).exists()) {
//          strFileName = fileName;
//        }
//      }
//      strFileName = strFileName.replace( '/', File.separatorChar );
//      sbFileRepositories.append( strFileName ).append( File.pathSeparatorChar );
//    }
  }
}
