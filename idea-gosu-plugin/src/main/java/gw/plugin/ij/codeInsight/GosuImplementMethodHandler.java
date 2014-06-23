/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.codeInsight;

import com.intellij.lang.LanguageCodeInsightActionHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import gw.plugin.ij.filetypes.GosuCodeFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuImplementMethodHandler implements LanguageCodeInsightActionHandler {
  public boolean isValidFor(Editor editor, @Nullable PsiFile psiFile) {
    return psiFile != null && GosuCodeFileType.INSTANCE.equals(psiFile.getFileType());
  }

  public void invoke(@NotNull final Project project, @NotNull Editor editor, @NotNull PsiFile file) {
    GosuOverrideImplementUtil.invokeOverrideImplement(editor, file, true);
  }

  public boolean startInWriteAction() {
    return false;
  }
}
