/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import gw.config.CommonServices;
import gw.fs.IFile;
import gw.fs.cache.ModulePathCache;
import gw.lang.reflect.module.IModule;
import gw.util.cache.FqnCache;
import gw.util.concurrent.LockingLazyVar;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * A property set based on an underlying {@link Properties} object. Knows how to finds Properties
 * files within a module and to converts them into PropertySet objects.
 */
public class PropertiesPropertySet implements PropertySet
{

  /**
   * Knows how to find all the property files in a module and create PropertiesPropertySets
   * from them.
   */
  public static class Source implements PropertySetSource
  {
    private IModule _module = null;

    private final LockingLazyVar<FqnCache<IFile>> _filesByFqn =
      LockingLazyVar.make( () -> ModulePathCache.instance().get( _module ).getExtensionCache( PropertiesSourceProducer.FILE_EXTENSION ) );

    @Override
    public PropertySet getPropertySetForFile( IFile file )
    {
      if( !file.getExtension().equalsIgnoreCase( PropertiesSourceProducer.FILE_EXTENSION ) )
      {
        return null;
      }

      String fqn = ModulePathCache.instance().get( _module ).getFqnForFile( file );
      if( fqn != null )
      {
        return getPropertySet( fqn );
      }
      return null;
    }

    @Override
    public IFile getFile( String name )
    {
      return _filesByFqn.get().get( name );
    }

    public Source( IModule module )
    {
      _module = module;
    }

    @Override
    public Set<String> getPropertySetNames()
    {
      return _filesByFqn.get().getFqns();
    }

    @Override
    public PropertySet getPropertySet( String name )
    {
      IFile file = _filesByFqn.get().get( name );
      if( file == null )
      {
        throw new IllegalArgumentException( name + " is not the name of a properties file property set" );
      }
      PropertySet result = new EmptyPropertySet( name );
      if( file.exists() )
      {
        try
        {
          InputStream propertiesStream = file.openInputStream();
          Properties properties = new Properties();
          try
          {
            properties.load( propertiesStream );
            result = new PropertiesPropertySet( name, properties );
          }
          finally
          {
            closeSafely( propertiesStream );
          }
        }
        catch( IOException e )
        {
          CommonServices.getEntityAccess().getLogger().error( String.format( "Could not read property file %s", file ), e );
        }
      }
      return result;
    }

    private static void closeSafely( InputStream inputStream )
    {
      try
      {
        inputStream.close();
      }
      catch( IOException e )
      {
        // Ignore
      }
    }
  }

  private final String _name;
  private final Set<String> _keys;
  private final Properties _properties;

  public PropertiesPropertySet( String name, Properties properties )
  {
    _name = name;
    _properties = properties;
    _keys = properties.stringPropertyNames();
  }

  @Override
  public Set<String> getKeys()
  {
    return _keys;
  }

  @Override
  public String getName()
  {
    return _name;
  }

  @Override
  public String getValue( String key )
  {
    return _properties.getProperty( key );
  }
}
