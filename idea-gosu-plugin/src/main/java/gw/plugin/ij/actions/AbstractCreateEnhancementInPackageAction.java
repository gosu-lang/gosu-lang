/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions;

import com.intellij.CommonBundle;
import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * This class is modified from CreateFromTemplateAction.java
 * We need to show a different dialog for enhancement, yet the actionPerformed() method in CreateFromTemplateAction cannot be overriden.
 *
 * @author pfong
 */
public abstract class AbstractCreateEnhancementInPackageAction<T extends PsiFile> extends AbstractCreateInPackageAction {
  public AbstractCreateEnhancementInPackageAction(String text, String description, Icon icon) {
    super(text, description, icon, true);
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    final DataContext dataContext = e.getDataContext();

    final IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
    if (view == null) {
      return;
    }

    final Project project = PlatformDataKeys.PROJECT.getData(dataContext);

    final PsiDirectory dir = view.getOrChooseDirectory();
    if (dir == null || project == null) {
      return;
    }

    final CreateEnhancementDialog.Builder builder = CreateEnhancementDialog.createDialog(project);
    buildDialog(project, dir, builder);

    final T createdElement =
      builder.show(getErrorTitle(), new CreateEnhancementDialog.FileCreator<T>() {
        public void checkBeforeCreate(@NotNull String name, @NotNull String enhancedClassName, @NotNull String templateName ) {
          AbstractCreateEnhancementInPackageAction.this.checkBeforeCreate(name, enhancedClassName, templateName, dir);
        }

        public T createFile(@NotNull String name, @NotNull String enhancedClassName, @NotNull String templateName) {
          return AbstractCreateEnhancementInPackageAction.this.createFile(name, enhancedClassName, templateName, dir);
        }

        @NotNull
        public String getActionName(@NotNull String name, @NotNull String templateName) {
          return AbstractCreateEnhancementInPackageAction.this.getActionName(dir, name, templateName);
        }
      });
    if (createdElement != null) {
      view.selectElement(createdElement);
    }
  }

  protected String getErrorTitle() {
    return CommonBundle.getErrorTitle();
  }

  @Nullable
  protected T createFile(String name, String enhancedClassName, @NotNull String templateName, @NotNull PsiDirectory dir) {
    return checkOrCreate(name, dir, enhancedClassName, templateName, false, getModule(dir));
  }

  protected IModule getModule(@NotNull PsiDirectory dir) {
    return GosuModuleUtil.findModuleForFile(dir.getVirtualFile(), dir.getProject());
  }

  protected void checkBeforeCreate(String name, String enhancedClassName, @NotNull String templateName, @NotNull PsiDirectory dir) {
    checkOrCreate(name, dir, enhancedClassName, templateName, true, getModule(dir));
  }

  @Nullable
  private T checkOrCreate(String newName, PsiDirectory directory, String enhancedClassName, @NotNull String templateName, boolean check, IModule module) throws IncorrectOperationException {
    PsiDirectory dir = directory;
    String className = newName;

    final String extension = StringUtil.getShortName(templateName);
    if (StringUtil.isNotEmpty(extension)) {
      className = StringUtil.trimEnd(className, "." + extension);
    }

    if (className.contains(".")) {
      String[] names = className.split("\\.");
      for (int i = 0; i < names.length - 1; i++) {
        String name = names[i];
        PsiDirectory subDir = dir.findSubdirectory(name);
        if (subDir == null) {
          if (check) {
            dir.checkCreateSubdirectory(name);
            return null;
          }
          subDir = dir.createSubdirectory(name);
        }
        dir = subDir;
      }
      className = names[names.length - 1];
    }
    if (check) {
      doCheckCreate(dir, className, enhancedClassName);
      return null;
    }
    return doCreate(dir, className, enhancedClassName, templateName, module);
  }

  protected abstract void buildDialog(Project project, PsiDirectory directory, CreateEnhancementDialog.Builder builder);
  protected abstract String getActionName(PsiDirectory directory, String newName, String templateName);
  protected abstract T doCreate(PsiDirectory dir, String className, String enhancedClassName, String templateName, IModule module) throws IncorrectOperationException;
  protected abstract void doCheckCreate(PsiDirectory dir, String className, String enhancedClassName) throws IncorrectOperationException;
}
