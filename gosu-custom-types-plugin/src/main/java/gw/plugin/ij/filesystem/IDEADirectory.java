/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.filesystem;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.VirtualFile;
import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.fs.IResource;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IDEADirectory extends IDEAResource implements IDirectory
{

  public IDEADirectory(VirtualFile dir) {
    super(dir);
  }

  public IDEADirectory(String dir) {
    super(dir);
  }

  @Override
  public IDirectory dir(@NotNull String relativePath) {
    if (relativePath.startsWith(File.separator)) {
      relativePath = relativePath.substring(1);
    }

    String path = _path + "/" + relativePath.replace(File.separatorChar, '/');
    return ((IDEAFileSystem) CommonServices.getFileSystem()).getIDirectory(path);
  }

  @Override
  public IFile file(@NotNull String relativePath) {
    if (relativePath.startsWith(File.separator)) {
      relativePath = relativePath.substring(1);
    }
    String path = _path + "/" + relativePath.replace(File.separatorChar, '/');
    return ((IDEAFileSystem) CommonServices.getFileSystem()).getIFile(path);
  }

  @Override
  public boolean mkdir() throws IOException {
    return create();
  }

  @NotNull
  @Override
  public List<? extends IDirectory> listDirs() {
    List<IDirectory> result = new ArrayList<>();
    if (_virtualFile != null) {
      for (VirtualFile child : _virtualFile.getChildren()) {
        if (child.isDirectory()) {
          result.add(new IDEADirectory(child));
        }
      }
    }
    return result;
  }

  @NotNull
  @Override
  public List<? extends IFile> listFiles() {
    List<IFile> result = new ArrayList<>();
    if (_virtualFile != null) {
      for (VirtualFile child : _virtualFile.getChildren()) {
        if (!child.isDirectory()) {
          result.add(new IDEAFile(child));
        }
      }
    }
    return result;
  }

  @Override
  public String relativePath(@NotNull IResource resource) {
    String path = ((IDEAResource) resource)._path;
    int index = _path.length() + 1;
    return index > path.length() ? "" : path.substring(index);
  }

  @Override
  public void clearCaches() {
    throw new RuntimeException("Not supported");
  }

  @Override
  public boolean hasChildFile(String path) {
    IFile childFile = file(path);
    return childFile != null && childFile.exists();
  }

  @Override
  public boolean isAdditional() {
    return false;
  }

  protected VirtualFile create(@NotNull final VirtualFile virtualFile, @NotNull final String name) throws IOException {
   final VirtualFile[] result = new VirtualFile[1];
    ApplicationManager.getApplication().runWriteAction(new Runnable() {
      @Override
      public void run() {
        try {
          result[0] = virtualFile.createChildDirectory(this, name);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

    });
    return result[0];
  }
}
