/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import static com.intellij.codeInsight.CodeInsightUtilBase.prepareFileForWrite;

public abstract class BaseIntentionAction implements IntentionAction {
  private String myText = "";

  @Override
  public final void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
    if (editor == null || !prepareFileForWrite(getAffectedFiles(file))) {
      return;
    }
    invokeImpl(project, editor, file);
  }

  protected PsiFile getAffectedFiles(PsiFile file) {
    return file;
  }

  protected abstract void invokeImpl(Project project, Editor editor, PsiFile file);

  @Override
  public boolean startInWriteAction() {
    return true;
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    return true;
  }

  @Override
  @NotNull
  public String getText() {
    return myText;
  }

  protected void setText(@NotNull String text) {
    myText = text;
  }

  @Override
  public String toString() {
    return getText();
  }
}
