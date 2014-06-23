/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions;

import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;

public class ActionUtil {

  public static boolean isInConfigFolder(DataContext dataContext) {
    final Project project = PlatformDataKeys.PROJECT.getData(dataContext);
    final IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
    if (project == null || view == null || view.getDirectories().length == 0) {
      return true;
    }

    ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(project).getFileIndex();
    for (PsiDirectory dir : view.getDirectories()) {
      VirtualFile sourceRoot = projectFileIndex.getSourceRootForFile(dir.getVirtualFile());
      if (sourceRoot != null && "config".equals(sourceRoot.getName())) {
        return true;
      }
    }
    return false;
  }

}
