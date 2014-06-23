/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatement;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static gw.plugin.ij.util.GosuBundle.message;

public class RemoveUnnecessaryImports extends BaseIntentionAction {

  private final Collection<IGosuUsesStatement> unnecessaryImports;

  public RemoveUnnecessaryImports(Collection<IGosuUsesStatement> unnecessaryImports) {
    this.unnecessaryImports = unnecessaryImports;
  }

  @NotNull
  @Override
  public String getText() {
    if (unnecessaryImports.size() == 1) {
      return message("fix.removeUnusedImport");
    } else {
      return message("fix.removeAllUnusedImports");
    }
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return "imports";
  }

  @Override
  public void invokeImpl(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
    for (IGosuUsesStatement theImport : unnecessaryImports) {
      theImport.delete();
    }
  }
}
