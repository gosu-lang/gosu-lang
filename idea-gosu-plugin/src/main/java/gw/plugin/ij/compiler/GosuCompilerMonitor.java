/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.compiler;

import com.intellij.openapi.compiler.CompilerPaths;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileMoveEvent;
import gw.plugin.ij.compiler.parser.CompilerUtils;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class GosuCompilerMonitor implements ProjectComponent {
  private static final Logger LOG = Logger.getInstance(GosuCompilerMonitor.class);

  private static final String GOSU_DEPENDENCIES_FILE_NAME = "gosu-dependencies.dat";

  private VirtualFileListener vfsListener = new VirtualFileAdapter() {
    private void updateDependents(VirtualFile file) {
      final FileDependencyCache cache = getDependencyCache();
      final Set<VirtualFile> dependents = cache.getDependentsOn(file);

      for (VirtualFile dependent : dependents) {
        final Module ijModule = GosuModuleUtil.getModule(GosuModuleUtil.findModuleForFile(dependent, project));
        if (ijModule != null) {
          final Pair<String, String> sourceFolderAndRelativePath = CompilerUtils.getSourceFolderAndRelativePath(ijModule, dependent);
          final File outputFile = CompilerUtils.getOutputFile(ijModule, sourceFolderAndRelativePath);
          final VirtualFile outputVirtualFile = LocalFileSystem.getInstance().findFileByIoFile(outputFile);
          if (outputVirtualFile != null) {
            try {
              outputVirtualFile.delete(this);
            } catch (IOException e) {
              // Ignore
            }
          }
        }
      }
      cache.remove(file);
    }

    @Override
    public void fileMoved(@NotNull VirtualFileMoveEvent event) {
      updateDependents(event.getFile());
    }

    @Override
    public void fileDeleted(@NotNull VirtualFileEvent event) {
      updateDependents(event.getFile());
    }
  };


  private FileDependencyCache cache;
  private final Project project;
  private final VirtualFileManager vfsManager;
  private final File cacheFile;

  public static GosuCompilerMonitor getInstance(@NotNull Project project) {
    return project.getComponent(GosuCompilerMonitor.class);
  }

  public GosuCompilerMonitor(@NotNull Project project, @NotNull VirtualFileManager vfsManager) {
    this.project = project;
    this.vfsManager = vfsManager;
    this.cacheFile = new File(CompilerPaths.getCacheStoreDirectory(project), GOSU_DEPENDENCIES_FILE_NAME);
  }


  public synchronized FileDependencyCache getDependencyCache() {
    if (cache == null) {
      cache = new FileDependencyCache();
      try {
        cache.load(project, cacheFile);
        setCacheInSync(true);
      } catch (IOException e) {
        cache.clear();
        setCacheInSync(false);
      }
    }
    return cache;
  }

  private boolean inSync = false;

  public boolean isCacheInSync() {
    return inSync;
  }

  public void setCacheInSync(boolean value) {
    inSync = value;
  }

  // ProjectComponent
  @Override
  public void projectOpened() {
    // Nothing to do
  }

  @Override
  public void projectClosed() {
    // Nothing to do
  }

  // BaseComponent
  @Override
  public void initComponent() {
    this.vfsManager.addVirtualFileListener(vfsListener, project);
  }

  @Override
  public void disposeComponent() {
    vfsManager.removeVirtualFileListener(vfsListener);

    if (cache != null) {
      try {
        cache.save(cacheFile);
      } catch (IOException e) {
        LOG.error("Error while saving cache file " + cacheFile, e);
      }
    }
  }

  // NamedComponent
  @NotNull
  @Override
  public String getComponentName() {
    return "GosuCompilerMonitor";
  }
}
