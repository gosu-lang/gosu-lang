/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import gw.fs.IFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * Makes available the standard system properties, which should be available on all JVMs
 */
public class SystemPropertiesPropertySet implements PropertySet
{

  public static final PropertySetSource SOURCE = new PropertySetSource()
  {

    private final PropertySet _propertySet = new SystemPropertiesPropertySet();

    @Override
    public Set<String> getPropertySetNames()
    {
      return Collections.singleton( _propertySet.getName() );
    }

    @Override
    public PropertySet getPropertySet( String name )
    {
      if( _propertySet.getName().equals( name ) )
      {
        return _propertySet;
      }
      return null;
    }

    @Override
    public PropertySet getPropertySetForFile( IFile file )
    {
      return null;
    }

    @Override
    public IFile getFile( String name )
    {
      return null;
    }

  };

  private final Set<String> _keys = Collections.unmodifiableSet( new TreeSet<String>( Arrays.asList(
    "java.version",
    "java.vendor",
    "java.vendor.url",
    "java.home",
    "java.vm.specification.version",
    "java.vm.specification.vendor",
    "java.vm.specification.name",
    "java.vm.version",
    "java.vm.vendor",
    "java.vm.name",
    "java.specification.version",
    "java.specification.vendor",
    "java.specification.name",
    "java.class.version",
    "java.class.path",
    "java.library.path",
    "java.io.tmpdir",
    "java.compiler",
    "java.ext.dirs",
    "os.name",
    "os.arch",
    "os.version",
    "file.separator",
    "path.separator",
    "line.separator",
    "user.name",
    "user.home",
    "user.dir"
  ) ) );

  private SystemPropertiesPropertySet()
  {
  }

  @Override
  public Set<String> getKeys()
  {
    return _keys;
  }

  @Override
  public String getName()
  {
    return "gw.lang.SystemProperties";
  }

  @Override
  public String getValue( String key )
  {
    return System.getProperty( key );
  }

}
