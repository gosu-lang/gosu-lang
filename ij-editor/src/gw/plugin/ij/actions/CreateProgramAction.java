/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPackage;
import com.intellij.util.IncorrectOperationException;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.module.IModule;

import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateProgramAction extends AbstractCreateFromTemplateInPackageAction<PsiFile> {
  private static final Logger LOG = Logger.getInstance(CreateProgramAction.class);

  public CreateProgramAction() {
    super(GosuBundle.message("new.program.menu.action.text"), GosuBundle.message("new.program.menu.action.description"), GosuIcons.FILE_PROGRAM);
  }

  protected void buildDialog(Project project, final PsiDirectory directory, @NotNull CreateSimpleDialog.Builder builder) {
    builder.setTitle(GosuBundle.message("new.program.dlg.title"));
    builder.setTemplateName(GosuTemplatesFactory.GOSU_PROGRAM_TEMPLATE);
  }

  @Override
  protected String getErrorTitle() {
    return GosuBundle.message("error.new.program.dlg.title");
  }

  @Override
  protected String getActionName(@NotNull PsiDirectory directory, String newName, String templateName) {
    if (!checkPackageExists(directory)) {
      return GosuBundle.message("error.new.artifact.nopackage");
    } else {
      PsiPackage pkg = JavaDirectoryService.getInstance().getPackage(directory);
      return GosuBundle.message("new.program.progress.text", pkg.getQualifiedName(), newName);
    }
  }

  protected void doCheckCreate(@NotNull PsiDirectory dir, @NotNull String className) throws IncorrectOperationException {
    String fileName = className + GosuClassTypeLoader.GOSU_PROGRAM_FILE_EXT;
    try {
      JavaDirectoryService.getInstance().checkCreateClass(dir, className);
    } catch (IncorrectOperationException e) {
      if (e.getMessage().contains("File already exists")) {
        throw new IncorrectOperationException(GosuBundle.message("error.new.artifact.java.conflict", fileName));
      }
      throw e;
    }
    dir.checkCreateFile(fileName);

    IModule module = getModule(dir);
    if (!checkPackageExists(dir)) {
      throw new IncorrectOperationException(GosuBundle.message("error.new.artifact.nopackage"));
    }
    String fullyQualifiedClassName = JavaDirectoryService.getInstance().getPackage(dir).getQualifiedName() + "." + className;
    if (TypeSystem.getByFullNameIfValid(fullyQualifiedClassName, module) != null) {
      throw new IncorrectOperationException(GosuBundle.message("error.new.artifact.gosu.conflict", fileName));
    }
  }

  @Nullable
  protected final PsiFile doCreate(@NotNull PsiDirectory dir, String className, String templateName) throws IncorrectOperationException {
    if (!checkPackageExists(dir)) {
      throw new IncorrectOperationException(GosuBundle.message("error.new.artifact.nopackage"));
    }

    AbstractCreateInPackageAction.checkDoesNotExistYet(dir, className);

    try {
      String fileName = className + GosuClassTypeLoader.GOSU_PROGRAM_FILE_EXT;
      return GosuTemplatesFactory.createFromTemplate(dir, className, fileName, templateName);
    } catch (IncorrectOperationException e) {
      throw e;
    } catch (Exception e) {
      LOG.error(e);
      return null;
    }
  }

  @Override
  protected boolean isAvailable(DataContext dataContext) {
    if (ActionUtil.isInConfigFolder(dataContext)) {
      return false;
    } else {
      return super.isAvailable(dataContext);
    }
  }
}
