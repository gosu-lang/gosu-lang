/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.debugger;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.FakePsiElement;
import gw.lang.reflect.module.IModule;
import gw.lang.IModuleAware;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProjectPsiElement extends FakePsiElement implements IModuleAware {
  private final Project _project;

  public ProjectPsiElement(Project project) {
    _project = project;
  }

  @Nullable
  @Override
  public PsiElement getParent() {
    return null;
  }

  @NotNull
  @Override
  public Project getProject() {
    return _project;
  }

  @Nullable
  @Override
  public PsiFile getContainingFile() {
    return null;
  }

  @Override
  public IModule getModule() {
    return GosuModuleUtil.getGlobalModule(getProject());
  }
}
