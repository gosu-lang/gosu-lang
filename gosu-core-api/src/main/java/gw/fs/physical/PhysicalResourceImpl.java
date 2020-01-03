/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs.physical;

import gw.fs.IDirectory;
import gw.fs.IResource;
import gw.fs.ResourcePath;
import gw.lang.UnstableAPI;

import java.io.File;
import java.net.URI;

@UnstableAPI
public class PhysicalResourceImpl implements IResource {
  final ResourcePath _path;
  final IPhysicalFileSystem _backingFileSystem;
  private File _file;
  private URI _uri;

  protected PhysicalResourceImpl(ResourcePath path, IPhysicalFileSystem backingFileSystem) {
    _path = path;
    _backingFileSystem = backingFileSystem;
  }

  @Override
  public IDirectory getParent() {
    if (_path.getParent() == null) {
      return null;
    } else {
      return new PhysicalDirectoryImpl(_path.getParent(), _backingFileSystem);
    }
  }

  @Override
  public String getName() {
    return _path.getName();
  }

  @Override
  public boolean exists() {
    return getIFileMetadata().exists();
  }

  @Override
  public boolean delete() {
    return _backingFileSystem.delete(_path);
  }

  @Override
  public URI toURI() {
    return _uri == null ? _uri = toJavaFile().toURI() : _uri;
  }

  @Override
  public ResourcePath getPath() {
    return _path;
  }

  @Override
  public boolean isChildOf(IDirectory dir) {
    return dir.getPath().isChild(_path);
  }

  @Override
  public boolean isDescendantOf( IDirectory dir ) {
    return dir.getPath().isDescendant(_path);
  }

  @Override
  public File toJavaFile() {
    return _file == null ? _file = new File(_path.getPathString()) : _file;
  }

  @Override
  public boolean isJavaFile() {
    return true;
  }

  @Override
  public boolean isInJar() {
    return false;
  }

  @Override
  public boolean create() {
    return false;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof IResource) {
      return _path.equals(((IResource) obj).getPath());
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return getPath().getFileSystemPathString();
  }
  
  protected IFileMetadata getIFileMetadata() {
    return _backingFileSystem.getFileMetadata(_path);
  }
}
