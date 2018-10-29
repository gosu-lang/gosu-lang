package gw.lang.gosuc;

import java.lang.module.ModuleDescriptor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class ClasspathUtil
{
  private static final String MANIFEST = "META-INF/MANIFEST.MF";

  /**
   * Returns urls for the classloader.
   *
   * @param classLoader classloader in which to find urls
   *
   * @return list of urls or {@code null} if not found
   */
  public static URL[] of( ClassLoader classLoader, Class clazz )
  {
    if( clazz == null )
    {
      clazz = ClasspathUtil.class;
    }
    if( classLoader == null )
    {
      classLoader = clazz.getClassLoader();
    }
    final Set<URL> urls = new LinkedHashSet<>();

    while( classLoader != null )
    {
      if( classLoader instanceof URLClassLoader )
      {
        URLClassLoader urlClassLoader = (URLClassLoader)classLoader;
        URL[] allURLS = urlClassLoader.getURLs();
        Collections.addAll( urls, allURLS );
        break;
      }

      URL classUrl = classModuleUrl( classLoader, clazz );
      if( classUrl != null )
      {
        urls.add( classUrl );
      }
      classUrl = classModuleUrl( classLoader, ClasspathUtil.class );
      if( classUrl != null )
      {
        urls.add( classUrl );
      }

      ModuleDescriptor moduleDescriptor = clazz.getModule().getDescriptor();

      if( moduleDescriptor != null )
      {
        moduleDescriptor.requires().forEach( req -> ModuleLayer.boot()
          .findModule( req.name() )
          .ifPresent( mod -> {
            ClassLoader moduleClassLoader = mod.getClassLoader();
            if( moduleClassLoader != null )
            {
              URL url = moduleClassLoader.getResource( MANIFEST );
              if( url != null )
              {
                url = fixManifestUrl( url );
                urls.add( url );
              }
            }
          } ) );
      }

      classLoader = classLoader.getParent();
    }

    return urls.toArray( new URL[0] );
  }

  private static URL fixManifestUrl( URL url )
  {
    String urlString = url.toString();
    int ndx = urlString.indexOf( MANIFEST );
    urlString = urlString.substring( 0, ndx ) + urlString.substring( ndx + MANIFEST.length() );

    try
    {
      return new URL( urlString );
    }
    catch( MalformedURLException ignore )
    {
      return null;
    }
  }

  private static URL classModuleUrl( ClassLoader classLoader, Class clazz )
  {
    if( clazz == null )
    {
      return null;
    }
    final String name = clazz.getName().replace( '.', '/' ) + ".class";

    return resourceModuleUrl( classLoader, name );
  }

  private static URL resourceModuleUrl( ClassLoader classLoader, String resourceClassPath )
  {
    URL url = classLoader.getResource( resourceClassPath );

    if( url == null )
    {
      return null;
    }

    // use root
    String urlString = url.toString();
    int ndx = urlString.indexOf( resourceClassPath );
    urlString = urlString.substring( 0, ndx ) + urlString.substring( ndx + resourceClassPath.length() );

    try
    {
      return new URL( urlString );
    }
    catch( MalformedURLException ignore )
    {
      return null;
    }
  }

}