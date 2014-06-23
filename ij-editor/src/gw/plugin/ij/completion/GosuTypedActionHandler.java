/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion;

import com.google.common.collect.ImmutableSet;
import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class GosuTypedActionHandler extends TypedHandlerDelegate {
  private static final Set<Character> INVOCATION_CHARS = ImmutableSet.of('#', ':', '$', '<');

  @NotNull
  @Override
  public Result checkAutoPopup(char charTyped, Project project, Editor editor, PsiFile file) {
    if (INVOCATION_CHARS.contains(charTyped)) {
      AutoPopupController.getInstance(project).autoPopupMemberLookup(editor, new Condition<PsiFile>() {
        public boolean value(final PsiFile file) {
          return true;
        }
      });
      return Result.STOP;
    }
    return Result.CONTINUE;
  }
}
