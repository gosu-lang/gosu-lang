/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.runner;

import com.intellij.execution.configurations.JavaParameters;
import com.intellij.openapi.compiler.ex.CompilerPathsEx;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PathUtil;
import gw.fs.IDirectory;
import gw.lang.reflect.module.IModule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GosuClasspathBuilder {
  private final Sdk _gosuSdk;
  private final IModule _module;
  private final JavaParameters _params;
  private List<String> _progClasspath;

  public GosuClasspathBuilder(Sdk gosuSdk, IModule module, JavaParameters params) {
    _gosuSdk = gosuSdk;
    _module = module;
    _params = params;
  }

  public void fillClasspath() {
    buildClassPath();
    addToClasspathFromDependentModules();
    addCurrentFolderToClasspath();
  }

  public String getProgramClasspath() {
    StringBuilder sb = new StringBuilder();
    List<String> classpathList = _params.getClassPath().getPathList();
    for (String path : getProgramClasspathFromDependentModules()) {
      if( !classpathList.contains( path ) ) {
        sb.append(path).append(','); // gosu classpath is comma delimited
      }
    }
    return sb.toString();
  }

  private void addCurrentFolderToClasspath() {
    _params.getClassPath().add(new File("."));
  }

  private void buildClassPath() {
    VirtualFile[] jars = ((ProjectJdkImpl) _gosuSdk).getRoots(OrderRootType.CLASSES);
    for (VirtualFile jar : jars) {
      _params.getClassPath().add(jar);
    }
  }

  private void addToClasspathFromDependentModules() {
    for (String path : getProgramClasspathFromDependentModules()) {
      _params.getClassPath().add(path);
    }
  }

  private List<String> getProgramClasspathFromDependentModules() {
    if (_progClasspath == null) {
      List<String> progClasspath = new ArrayList<>();
      for (IModule gsModule : _module.getModuleTraversalList()) {
        final Module ijModule = (Module) gsModule.getNativeModule();
        if (ijModule != null) {
          // Add output path to classpath
          for (String strOutputPath : CompilerPathsEx.getOutputPaths(new Module[]{ijModule})) {
            if (!progClasspath.contains(strOutputPath)) {
              progClasspath.add(strOutputPath);
            }
          }

          // Add jar files to classpath
          for (OrderEntry entry : ModuleRootManager.getInstance(ijModule).getOrderEntries()) {
            for (VirtualFile vfile : entry.getFiles(OrderRootType.CLASSES)) {
              String strPath = PathUtil.getLocalPath(vfile);
              if (!progClasspath.contains(strPath)) {
                progClasspath.add(strPath);
              }
            }
          }
        }

        // Add source paths as specified in Gosu modules to classpath
        for (IDirectory directory : gsModule.getSourcePath()) {
          final VirtualFile file = LocalFileSystem.getInstance().findFileByIoFile(new File(directory.getPath().getPathString()));
          final String strPath = PathUtil.getLocalPath(file);
          if (!progClasspath.contains(strPath)) {
            progClasspath.add(strPath);
          }
        }
      }
      _progClasspath = progClasspath;
    }
    return _progClasspath;
  }
}
