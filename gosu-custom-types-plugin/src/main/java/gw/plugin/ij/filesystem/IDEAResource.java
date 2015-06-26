/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.filesystem;

import com.google.common.base.Objects;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.impl.jar.JarFileSystemImpl;
import com.intellij.openapi.vfs.impl.local.LocalFileSystemImpl;
import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IResource;
import gw.fs.ResourcePath;
import gw.plugin.ij.util.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class IDEAResource implements IResource
{
  @Nullable
  protected VirtualFile _virtualFile;
  protected final String _path;

  public IDEAResource(@NotNull VirtualFile virtualFile) {
    this._virtualFile = checkNotNull(virtualFile);
    this._path = checkNotNull( FileUtil.removeJarSeparator( virtualFile.getPath() ));
  }

  public IDEAResource(String dir) {
    this._path = checkNotNull(dir);
  }

  @NotNull
  @Override
  public IDirectory getParent() {
    if (_virtualFile != null) {
      return new IDEADirectory(_virtualFile.getParent());
    } else {
      return ((IDEAFileSystem) CommonServices.getFileSystem()).getIDirectory(_path.substring(0, _path.lastIndexOf('/')));
    }
  }

  @Nullable
  @Override
  public String getName() {
    return _virtualFile != null ? _virtualFile.getName() : new File(_path).getName();
  }

  @Override
  public boolean exists() {
    if (_virtualFile != null) {
      return _virtualFile.exists();
    } else {
      return new File(_path).exists();
    }
  }

  @Override
  public boolean delete() throws IOException {
    return false;
  }

  @Override
  public URI toURI() {
    return new File(_path).toURI();
  }

  @Override
  public ResourcePath getPath() {
    return ResourcePath.parse( _path );
  }

  @Override
  public boolean isChildOf(@NotNull IDirectory dir) {
    String dirPath = ((IDEADirectory) dir)._path;
    return this._path.length() > dirPath.length() && this._path.startsWith(dirPath) && this._path.charAt(dirPath.length()) == '/';
  }

  @Override
  public boolean isDescendantOf(@NotNull IDirectory dir) {
    if( dir instanceof IDEADirectory ) {
      // note, trailing '/' prevents /root/src2 matching against /root/src
      if (_path.contains(".jar")) {
        return _path.startsWith(((IDEADirectory) dir)._path);
      } else {
        return (_path + '/').startsWith(((IDEADirectory) dir)._path + '/');
      }
    }
    return false;
  }

  @NotNull
  @Override
  public File toJavaFile() {
    return new File(_path.replace('/', File.separatorChar));
  }

  @Override
  public boolean isJavaFile() {
    return _virtualFile.getFileSystem() instanceof LocalFileSystemImpl;
  }

  public String toString() {
    return _path;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (o instanceof IDEAResource) {
      IDEAResource other = (IDEAResource) o;
      if (Objects.equal(_virtualFile, other._virtualFile)) {
        return true;
      }
      if ((_virtualFile == null || other._virtualFile == null) &&
                      Objects.equal(_path, other._path)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(_virtualFile);
  }

  @Override
  public boolean create() {
    if (_virtualFile == null) {
      final IDEADirectory parent = (IDEADirectory) getParent();
      parent.create();
      try {
        final int index = _path.lastIndexOf('/');
        final String name = _path.substring(index + 1);
        _virtualFile = create(parent._virtualFile, name);
        parent._virtualFile.refresh(false, true);
        return true;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return true;
  }

  protected abstract VirtualFile create(VirtualFile virtualFile, String name) throws IOException;

  @Nullable
  public VirtualFile getVirtualFile() {
    return _virtualFile;
  }

  @Nullable
  public VirtualFile resolveVirtualFile() {
    if (_virtualFile == null) {
      //try re-resolve virtual file if it null
      _virtualFile = LocalFileSystemImpl.getInstance().findFileByPath(_path);
      if (_virtualFile == null && _path.contains(".jar!")) {
        _virtualFile = JarFileSystemImpl.getInstance().findFileByPath(_path);
      }
    }
    return _virtualFile;
  }

  @Override
  public boolean isInJar() {
    return _virtualFile != null && _virtualFile.getFileSystem() instanceof JarFileSystem;
  }
}
