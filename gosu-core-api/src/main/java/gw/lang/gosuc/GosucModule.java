/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import gw.fs.IDirectory;
import gw.lang.reflect.module.INativeModule;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GosucModule implements INativeModule
{
  private String _name;
  private List<String> _allSourceRoots;
  private List<String> _excludedRoots;
  private List<String> _classpath;
  private List<String> _backingSourcePath;
  private String _outputPath;

  public GosucModule( String name,
                      List<String> allSourceRoots,
                      List<String> classpath,
                      List<String> backingSourcePath,
                      String outputPath,
                      List<String> excludedRoots )
  {
    _allSourceRoots = new ArrayList<>();
    //noinspection Convert2streamapi
    for( String sourceRoot: allSourceRoots )
    {
      if( !sourceRoot.endsWith( ".jar" ) )
      {
        _allSourceRoots.add( convertToUri( sourceRoot ) );
      }
    }
    makeUris( excludedRoots, _excludedRoots = new ArrayList<>() );
    makeUris( classpath, _classpath = new ArrayList<>() );
    makeUris( backingSourcePath, _backingSourcePath = new ArrayList<>() );
    _outputPath = convertToUri( outputPath );
    _name = name;
  }

  private void makeUris( List<String> from, List<String> to )
  {
    for( String path: from )
    {
      to.add( convertToUri( path ) );
    }
  }

  private String convertToUri( String filePath )
  {
    try
    {
      File file = new File( filePath );
      if( file.exists() )
      {
        return file.getAbsoluteFile().toURI().toString();
      }
    }
    catch( Exception ignore )
    {
    }

    try
    {
      URI uri = URI.create( filePath );
      if( !uri.isAbsolute() )
      {
        filePath = new File( filePath ).getAbsoluteFile().toURI().toString();
      }
    }
    catch( Exception e )
    {
      filePath = new File( filePath ).getAbsoluteFile().toURI().toString();
    }
    return filePath;
  }

  public List<String> getAllSourceRoots()
  {
    return _allSourceRoots;
  }

  public List<String> getExcludedRoots()
  {
    return _excludedRoots;
  }

  public List<String> getClasspath()
  {
    return _classpath;
  }

  public List<String> getBackingSourcePath()
  {
    return _backingSourcePath;
  }

  public String getName()
  {
    return _name;
  }

  @Override
  public Object getNativeModule()
  {
    return this;
  }

  @Override
  public IDirectory getOutputPath()
  {
    return _outputPath != null ? GosucUtil.getDirectoryForPath( _outputPath ) : null;
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( o == null || getClass() != o.getClass() )
    {
      return false;
    }

    GosucModule that = (GosucModule)o;

    if( !_allSourceRoots.equals( that._allSourceRoots ) )
    {
      return false;
    }
    if( !_excludedRoots.equals( that._excludedRoots ) )
    {
      return false;
    }
    if( !_classpath.equals( that._classpath ) )
    {
      return false;
    }
    if( !_backingSourcePath.equals( that._backingSourcePath ) )
    {
      return false;
    }
    if( !_name.equals( that._name ) )
    {
      return false;
    }
    return Objects.equals( _outputPath, that._outputPath );
  }

  @Override
  public int hashCode()
  {
    int result = _name.hashCode();
    result = 31 * result + _allSourceRoots.hashCode();
    result = 31 * result + _excludedRoots.hashCode();
    result = 31 * result + _classpath.hashCode();
    result = 31 * result + _backingSourcePath.hashCode();
    result = 31 * result + (_outputPath != null ? _outputPath.hashCode() : 0);
    return result;
  }
}
