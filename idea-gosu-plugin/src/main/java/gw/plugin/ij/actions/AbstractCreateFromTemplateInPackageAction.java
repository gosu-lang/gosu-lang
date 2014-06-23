/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions;

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
 * Class description...
 *
 * @author pfong
 */
public abstract class AbstractCreateFromTemplateInPackageAction <T extends PsiFile> extends AbstractCreateInPackageAction {

  public AbstractCreateFromTemplateInPackageAction(String text, String description, Icon icon) {
    super(text, description, icon, true);
  }

  public final void actionPerformed(@NotNull final AnActionEvent e) {
    final DataContext dataContext = e.getDataContext();

    final IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
    if (view == null) {
      return;
    }

    final Project project = PlatformDataKeys.PROJECT.getData(dataContext);

    final PsiDirectory dir = view.getOrChooseDirectory();
    if (dir == null || project == null) return;

    final CreateSimpleDialog.Builder builder = CreateSimpleDialog.createDialog(project);
    buildDialog(project, dir, builder);

    final T createdElement =
      builder.show(getErrorTitle(), builder.getTemplateName(), new CreateSimpleDialog.FileCreator<T>() {
        public void checkBeforeCreate(@NotNull String name, @NotNull String templateName) {
          AbstractCreateFromTemplateInPackageAction.this.checkBeforeCreate(name, templateName, dir);
        }

        public T createFile(@NotNull String name, @NotNull String templateName) {
          return AbstractCreateFromTemplateInPackageAction.this.createFile(name, templateName, dir);
        }

        @NotNull
        public String getActionName(@NotNull String name, @NotNull String templateName) {
          return AbstractCreateFromTemplateInPackageAction.this.getActionName(dir, name, templateName);
        }
      });
    if (createdElement != null) {
      view.selectElement(createdElement);
    }
  }

  @Nullable
  protected T createFile(String name, @NotNull String templateName, PsiDirectory dir) {
    return checkOrCreate(name, dir, templateName, false);
  }

  protected IModule getModule(@NotNull PsiDirectory dir) {
    return GosuModuleUtil.findModuleForFile(dir.getVirtualFile(), dir.getProject());
  }

  protected void checkBeforeCreate(String name, @NotNull String templateName, PsiDirectory dir) {
    checkOrCreate(name, dir, templateName, true);
  }

  @Nullable
  private T checkOrCreate(String newName, PsiDirectory directory, @NotNull String templateName, boolean check) throws IncorrectOperationException {
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
      doCheckCreate(dir, className);
      return null;
    }
    return doCreate(dir, className, templateName);
  }

  protected abstract String getErrorTitle();
  protected abstract void buildDialog(Project project, PsiDirectory directory, CreateSimpleDialog.Builder builder);
  protected abstract String getActionName(PsiDirectory directory, String newName, String templateName);
  protected abstract void doCheckCreate(PsiDirectory dir, String className) throws IncorrectOperationException;
  protected abstract T doCreate(PsiDirectory dir, String className, String templateName);
}
