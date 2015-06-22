/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import gw.config.AbstractPlatformHelper;
import gw.config.ExecutionMode;
import gw.lang.reflect.module.IModule;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class IDEAPlatformHelper extends AbstractPlatformHelper implements FileEditorManagerListener {
  private Project _project;

  public IDEAPlatformHelper(Project project) {
    _project = project;
  }

  @Override
  public ExecutionMode getExecutionMode() {
    return ExecutionMode.IDE;
  }

  @Override
  public boolean shouldCacheTypeNames() {
    return true;
  }

  @Override
  public void refresh(IModule module) {
  }

  // FileEditorManagerListener
  public void fileOpened(FileEditorManager source, final VirtualFile file) {
  }

  public void fileClosed(FileEditorManager source, VirtualFile file) {
  }

  public void selectionChanged(@NotNull FileEditorManagerEvent event) {
  }

  @Override
  public File getIndexFile(String id) {
    final File indexPath = PathManager.getIndexRoot();
    File dir = new File(indexPath, "gosutypenames");
    if (!dir.exists()) {
      dir.mkdir();
    }
    String projectID = _project.getName() + "$" + _project.getLocationHash();
    return new File(dir, projectID + "$" + id + "$index.txt");
  }


  public File getIDEACachesDirFile() {
    return new File(getIDEACachesDir());
  }

  public String getIDEACachesDir() {
    String dir = System.getProperty("caches_dir");
    return dir == null ? PathManager.getSystemPath() + "/caches/" : dir;
  }

  public File getIDEACorruptionMarkerFile() {
    return new File(getIDEACachesDirFile(), "corruption.marker");
  }

  @Override
  public boolean isPathIgnored(String relativePath) {
    if (_project.isInitialized() && relativePath != null) {
      Sdk projectSdk = ProjectRootManager.getInstance(_project).getProjectSdk();
      if (projectSdk != null) {
        VirtualFile[] sources = projectSdk.getRootProvider().getFiles(OrderRootType.SOURCES);
        VirtualFile jarFile = null;
        if (relativePath.contains("!")) {
          VirtualFile jarEntry = JarFileSystem.getInstance().findFileByPath(relativePath);
          if (jarEntry != null ) {
            jarFile = JarFileSystem.getInstance().getVirtualFileForJar(jarEntry);
          }
        }
        boolean comesFromJar = jarFile != null;

        for (VirtualFile source : sources) {
          if (comesFromJar != source.getFileSystem() instanceof JarFileSystem) {
            continue;
          }
          if (comesFromJar) {
            VirtualFile jarSource = JarFileSystem.getInstance().getVirtualFileForJar(source);
            if (jarFile.equals(jarSource)) {
              return true;
            }
          } else {
            String canonicalPath = source.getCanonicalPath();
            if (canonicalPath != null && canonicalPath.startsWith(relativePath)) {
              return true;
            }
          }
        }
      }
    }
    return super.isPathIgnored(relativePath);
  }
}
