/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.filesystem;

import com.intellij.openapi.vfs.VirtualFile;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.fs.IResource;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class IDEAJarDirectory extends IDEADirectory {

  public IDEAJarDirectory(@NotNull VirtualFile virtualFile) {
    super(virtualFile);
  }

  @Override
  public IDirectory dir(String relativePath) {
    VirtualFile child = _virtualFile.findFileByRelativePath(normalize(relativePath));
    return child == null ? null : new IDEAJarDirectory(child);
  }

  @Override
  public IFile file(String path) {
    VirtualFile child = _virtualFile.findFileByRelativePath(normalize(path));
    return child == null ? null : new IDEAFile(child);
  }

  private String normalize(String relativePath) {
    return relativePath.replace(File.separatorChar, '/');
  }

  @Override
  public String relativePath(IResource resource) {
    return ((IDEAResource) resource)._path.substring(_path.length() + 2);
  }

  @Override
  protected VirtualFile create(VirtualFile virtualFile, String name) throws IOException {
    throw new RuntimeException("Not supported");
  }

  @Override
  public boolean mkdir() throws IOException {
    throw new RuntimeException("Not supported");
  }
}
