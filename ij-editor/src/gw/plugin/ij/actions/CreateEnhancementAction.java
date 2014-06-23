/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions;

import com.google.common.collect.ImmutableList;
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

import java.util.List;

public class CreateEnhancementAction extends AbstractCreateEnhancementInPackageAction<PsiFile> {
  private static final Logger LOG = Logger.getInstance(CreateEnhancementAction.class);

  public CreateEnhancementAction() {
    super(GosuBundle.message("new.enhancement.menu.action.text"), GosuBundle.message("new.enhancement.menu.action.description"), GosuIcons.ENHANCEMENT);
  }

  protected void buildDialog(Project project, final PsiDirectory directory, @NotNull CreateEnhancementDialog.Builder builder) {
    builder.setTitle(GosuBundle.message("new.enhancement.dlg.title"));
  }

  @Override
  protected String getErrorTitle() {
    return GosuBundle.message("error.new.enhancement.dlg.title");
  }

  @Override
  protected String getActionName(@NotNull PsiDirectory directory, String newName, String templateName) {
    return GosuBundle.message("new.enhancement.progress.text", JavaDirectoryService.getInstance().getPackage(directory).getQualifiedName(), newName);
  }

  @Nullable
  @Override
  protected PsiFile doCreate(PsiDirectory dir, String className, String enhancedClassName, String templateName, IModule module) throws IncorrectOperationException {
    AbstractCreateInPackageAction.checkDoesNotExistYet(dir, className);

    try {
      String fileName = className + GosuClassTypeLoader.GOSU_ENHANCEMENT_FILE_EXT;
      return GosuTemplatesFactory.createFromTemplate(dir, className, fileName, templateName, GosuTemplatesFactory.PROPERTY_ENHANCED_CLASS, enhancedClassName);
    } catch (IncorrectOperationException e) {
      throw e;
    } catch (Exception e) {
      LOG.error(e);
      return null;
    }
  }

  @Override
  protected void doCheckCreate(@NotNull PsiDirectory dir, @NotNull String className, String enhancedClassName) throws IncorrectOperationException {
    String fileName = className + GosuClassTypeLoader.GOSU_ENHANCEMENT_FILE_EXT;
    try {
      JavaDirectoryService.getInstance().checkCreateClass(dir, className);
    } catch (IncorrectOperationException e) {
      if (e.getMessage().contains(GosuBundle.message("error.new.file.exists"))) {
        throw new IncorrectOperationException(GosuBundle.message("error.new.artifact.java.conflict", fileName));
      }
      throw e;
    }
    dir.checkCreateFile(fileName);

    IModule module = getModule(dir);
    if (!checkPackageExists(dir)) {
      throw new IncorrectOperationException(GosuBundle.message("error.new.artifact.nopackage"));
    }
    PsiPackage pkg = JavaDirectoryService.getInstance().getPackage(dir);
    String fullyQualifiedClassName = pkg.getQualifiedName() + "." + className;
    if (TypeSystem.getByFullNameIfValid(fullyQualifiedClassName, module) != null) {
      throw new IncorrectOperationException(GosuBundle.message("error.new.artifact.gosu.conflict", fileName));
    }
    if (TypeSystem.getByFullNameIfValid(enhancedClassName, module) == null) {
      throw new IncorrectOperationException(GosuBundle.message("error.new.enhancement.invalid.type", enhancedClassName));
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
