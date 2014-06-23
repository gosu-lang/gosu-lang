/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions;

import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.sdk.GosuSdkUtils;
import gw.plugin.ij.util.GosuBundle;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;

/**
 * The main purpose of this class is to conditionally display the Create actions in the action popup menu.
 *
 * @author pfong
 */
public abstract class AbstractCreateInPackageAction extends TypeSystemAwareAction {
  private final boolean myInSourceOnly;

  public AbstractCreateInPackageAction(String text, String description, Icon icon, boolean inSourceOnly) {
    super(text, description, icon);
    myInSourceOnly = inSourceOnly;
  }

  public void updateImpl(@NotNull final AnActionEvent e) {
    final DataContext dataContext = e.getDataContext();
    final Presentation presentation = e.getPresentation();

    final boolean enabled = isAvailable(dataContext);

    presentation.setVisible(enabled);
    presentation.setEnabled(enabled);
  }

  protected boolean isAvailable(@NotNull final DataContext dataContext) {
    final Project project = PlatformDataKeys.PROJECT.getData(dataContext);
    final IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
    if (project == null || view == null || view.getDirectories().length == 0) {
      return false;
    }

    if( !GosuSdkUtils.isGosuSdkSet( project ) &&
        !GosuSdkUtils.isGosuApiModuleInProject( project ) ) {
      return false;
    }

    if (!myInSourceOnly) {
      return true;
    }

    ProjectFileIndex projectFileIndex = ProjectRootManager.getInstance(project).getFileIndex();
    for (PsiDirectory dir : view.getDirectories()) {
      if (projectFileIndex.isInSourceContent(dir.getVirtualFile()) && checkPackageExists(dir)) {
        return true;
      }
    }

    return false;
  }

  protected boolean checkPackageExists(@NotNull PsiDirectory dir) {
    Boolean gosuActionsAvailable = dir.getUserData(GosuSdkUtils.NEW_GOSU_ACTIONS_AVAILABLE_KEY);
    if (gosuActionsAvailable != null && !gosuActionsAvailable.booleanValue()) {
      return false;
    }

    PsiPackage pkg = JavaDirectoryService.getInstance().getPackage(dir);
    return pkg != null;
  }

  public static void checkDoesNotExistYet(PsiDirectory dir, String className) {
    PsiPackage pkg = JavaDirectoryService.getInstance().getPackage(dir);
    String typeName = pkg.getQualifiedName() + "." + className;

    // compute the topmost module that has this package
    IModule module = GosuModuleUtil.findModuleForFile(dir.getVirtualFile(), pkg.getProject());
    PsiDirectory[] directories = pkg.getDirectories();
    for (PsiDirectory directory : directories) {
      IModule m = GosuModuleUtil.findModuleForFile(directory.getVirtualFile(), pkg.getProject());
      if (Arrays.asList(m.getModuleTraversalList()).contains(module)) {
        module = m;
      }
    }

    // check if the type already exists
    IType type = TypeSystem.getByFullNameIfValid(typeName, module);
    if (type != null && type.getTypeLoader().getModule().equals(module)) {
      throw new IllegalArgumentException(
          "Cannot create " + typeName + " since it already exists. Use a different name.");
    }
  }
}
