/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.module.fs;

import gw.fs.IResource;
import gw.fs.ResourcePath;
import gw.fs.IDirectory;
import gw.config.CommonServices;

import java.io.File;
import java.io.Serializable;
import java.net.URI;

public abstract class JavaResourceImpl implements IResource, Serializable {

  protected File _file;
  private String _name;
  private URI _uri;
  private ResourcePath _path;
  private IDirectory _parent;
  private int _hash;

  protected JavaResourceImpl(File file) {
    _file = file.getAbsoluteFile();
  }

  @Override
  public IDirectory getParent() {
    if( _parent == null )
    {
      File parentFile = _file.getParentFile();
      if( parentFile != null )
      {
        _parent = CommonServices.getFileSystem().getIDirectory( parentFile );
      }
    }
    return _parent;
  }

  @Override
  public String getName() {
    return _name == null ? _name = _file.getName() : _name;
  }

  @Override
  public boolean delete() {
    return _file.delete();
  }

  @Override
  public URI toURI() {
    return _uri == null ? _uri = _file.toURI() : _uri;
  }

  @Override
  public ResourcePath getPath() {
    return _path == null ? _path = ResourcePath.parse(_file.getAbsolutePath()) : _path;
  }

  @Override
  public boolean isChildOf(IDirectory dir) {
    return dir.equals(getParent());
  }

  @Override
  public boolean isDescendantOf( IDirectory dir ) {
    if ( ! ( dir instanceof JavaDirectoryImpl ) ) {
      return false;
    }
    File javadir = ( (JavaDirectoryImpl) dir )._file;
    File javafile = _file.getParentFile();
    while ( javafile != null ) {
      if ( javafile.equals( javadir ) ) {
        return true;
      }
      javafile = javafile.getParentFile();
    }
    return false;
  }

  @Override
  public File toJavaFile() {
    return _file;
  }

  @Override
  public boolean isJavaFile() {
    return true;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof JavaResourceImpl) {
      return _file.equals(((JavaResourceImpl) obj)._file);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return _hash == 0 ? _hash = _file.hashCode() : _hash;
  }

  @Override
  public String toString() {
    return _file.toString();
  }

  @Override
  public boolean create() {
    return false;
  }

  @Override
  public boolean isInJar() {
    return false;
  }
}
