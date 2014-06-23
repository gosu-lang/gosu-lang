/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public class OrganizeImports extends BaseIntentionAction {

  @Override
  public String getText() {
    return "Organize Imports";
  }

  @Override
  public String getFamilyName() {
    return "imports";
  }

  @Override
  public void invokeImpl(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
    new GosuImportOptimizer().processFile(file).run();
  }
}
