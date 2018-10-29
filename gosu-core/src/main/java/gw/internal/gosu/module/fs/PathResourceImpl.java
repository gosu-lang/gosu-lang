/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.module.fs;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IResource;
import gw.fs.ResourcePath;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class PathResourceImpl implements IResource, Serializable
{
  private Path _path;

  PathResourceImpl( Path path )
  {
    _path = path.toAbsolutePath();
  }

  Path get_Path()
  {
    return _path;
  }

  @Override
  public IDirectory getParent()
  {
    Path parent = _path.getParent();
    if( parent == null )
    {
      return null;
    }
    else
    {
      return CommonServices.getFileSystem().getIDirectory( parent );
    }
  }

  @Override
  public String getName()
  {
    return _path.getFileName().toString();
  }

  @Override
  public boolean delete() throws IOException
  {
    Files.delete( _path );
    return true;
  }

  @Override
  public URI toURI()
  {
    return _path.toUri();
  }

  @Override
  public ResourcePath getPath()
  {
    return ResourcePath.parse( _path.toAbsolutePath().toString() );
  }

  @Override
  public boolean isChildOf( IDirectory dir )
  {
    return dir.equals( getParent() );
  }

  @Override
  public boolean isDescendantOf( IDirectory dir )
  {
    if( !(dir instanceof PathDirectoryImpl) )
    {
      return false;
    }
    Path parent = ((PathDirectoryImpl)dir).get_Path();
    return parent.equals( _path ) || _path.toAbsolutePath().toString().startsWith( parent.toString() + "/" );
  }

  @Override
  public File toJavaFile()
  {
    return _path.toFile();
  }

  @Override
  public boolean isJavaFile()
  {
    return Files.isRegularFile( _path ) && _path.getFileSystem() == FileSystems.getDefault();
  }

  @Override
  public boolean equals( Object obj )
  {
    if( obj instanceof PathResourceImpl )
    {
      return _path.equals( ((PathResourceImpl)obj)._path );
    }
    else
    {
      return false;
    }
  }

  @Override
  public int hashCode()
  {
    return _path.hashCode();
  }

  @Override
  public String toString()
  {
    return _path.toUri().toString();
  }

  @Override
  public boolean create()
  {
    return false;
  }

  @Override
  public boolean isInJar()
  {
    return false;
  }
}
