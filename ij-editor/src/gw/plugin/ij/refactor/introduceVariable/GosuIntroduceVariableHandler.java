/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.introduceVariable;

import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.refactoring.HelpID;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.introduce.inplace.OccurrencesChooser;
import com.intellij.refactoring.introduceVariable.IntroduceVariableSettings;
import com.intellij.refactoring.ui.ConflictsDialog;
import com.intellij.refactoring.ui.TypeSelectorManagerImpl;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import com.intellij.util.containers.MultiMap;

import java.util.ArrayList;

public class GosuIntroduceVariableHandler extends GosuIntroduceVariableBase {


  @Override
  public IntroduceVariableSettings getSettings(Project project, Editor editor,
                                               PsiExpression expr, PsiExpression[] occurrences,
                                               TypeSelectorManagerImpl typeSelectorManager,
                                               boolean declareFinalIfAll,
                                               boolean anyAssignmentLHS,
                                               final GosuInputValidator validator,
                                               PsiElement anchor, final OccurrencesChooser.ReplaceChoice replaceChoice) {
    if (replaceChoice != null) {
      return super.getSettings(project, editor, expr, occurrences, typeSelectorManager, declareFinalIfAll, anyAssignmentLHS, validator,
              anchor, replaceChoice);
    }
    ArrayList<RangeHighlighter> highlighters = new ArrayList<>();
    HighlightManager highlightManager = null;
    if (editor != null) {
      highlightManager = HighlightManager.getInstance(project);
      EditorColorsManager colorsManager = EditorColorsManager.getInstance();
      TextAttributes attributes = colorsManager.getGlobalScheme().getAttributes(EditorColors.SEARCH_RESULT_ATTRIBUTES);
      if (occurrences.length > 1) {
        highlightManager.addOccurrenceHighlights(editor, occurrences, attributes, true, highlighters);
      }
    }

    GosuIntroduceVariableDialog dialog = new GosuIntroduceVariableDialog(
            project, expr, occurrences.length, anyAssignmentLHS, declareFinalIfAll,
            typeSelectorManager,
            validator);
    dialog.show();
    if (!dialog.isOK()) {
      if (occurrences.length > 1) {
        WindowManager.getInstance().getStatusBar(project).setInfo(RefactoringBundle.message("press.escape.to.remove.the.highlighting"));
      }
    } else {
      if (editor != null) {
        for (RangeHighlighter highlighter : highlighters) {
          highlightManager.removeSegmentHighlighter(editor, highlighter);
        }
      }
    }

    return dialog;
  }

  protected void showErrorMessage(final Project project, Editor editor, String message) {
    CommonRefactoringUtil.showErrorHint(project, editor, message, REFACTORING_NAME, HelpID.INTRODUCE_VARIABLE);
  }

  protected boolean reportConflicts(final MultiMap<PsiElement, String> conflicts, final Project project, IntroduceVariableSettings dialog) {
    ConflictsDialog conflictsDialog = new ConflictsDialog(project, conflicts);
    conflictsDialog.show();
    final boolean ok = conflictsDialog.isOK();
    if (!ok && conflictsDialog.isShowConflicts()) {
      if (dialog instanceof DialogWrapper) {
        ((DialogWrapper) dialog).close(DialogWrapper.CANCEL_EXIT_CODE);
      }
    }
    return ok;
  }
}
