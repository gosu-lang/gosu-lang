/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.plugin.ij.refactor.intoduceField;

import com.intellij.codeInsight.TargetElementUtilBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Pass;
import com.intellij.psi.PsiDeclarationStatement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiExpressionStatement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.refactoring.IntroduceTargetChooser;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import gw.plugin.ij.refactor.GosuCodeInsightUtil;
import gw.plugin.ij.refactor.GosuRefactoringUtil;
import gw.plugin.ij.refactor.GosuRenderFuction;
import gw.plugin.ij.refactor.introduceVariable.GosuIntroduceVariableBase;

import java.util.List;

/**
 * @author dsl
 */
public class GosuElementToWorkOn {
  public static final Key<PsiElement> PARENT = Key.create("PARENT");
  private final PsiExpression myExpression;
  private final PsiLocalVariable myLocalVariable;
  public static final Key<String> PREFIX = Key.create("prefix");
  public static final Key<String> SUFFIX = Key.create("suffix");
  public static final Key<RangeMarker> TEXT_RANGE = Key.create("range");
  public static final Key<Boolean> OUT_OF_CODE_BLOCK = Key.create("out_of_code_block");

  private GosuElementToWorkOn(PsiLocalVariable localVariable, PsiExpression expr) {
    myLocalVariable = localVariable;
    myExpression = expr;
  }

  public PsiExpression getExpression() {
    return myExpression;
  }

  public PsiLocalVariable getLocalVariable() {
    return myLocalVariable;
  }

  public boolean isInvokedOnDeclaration() {
    return myExpression == null;
  }

  public static void processElementToWorkOn(final Editor editor, final PsiFile file, final String refactoringName, final String helpId, final Project project, final Pass<GosuElementToWorkOn> processor) {
    PsiLocalVariable localVar = null;
    PsiExpression expr = null;

    if (!editor.getSelectionModel().hasSelection()) {
      PsiElement element = TargetElementUtilBase.findTargetElement(editor, TargetElementUtilBase
              .ELEMENT_NAME_ACCEPTED | TargetElementUtilBase
              .REFERENCED_ELEMENT_ACCEPTED | TargetElementUtilBase
              .LOOKUP_ITEM_ACCEPTED);
      if (element instanceof PsiLocalVariable) {
        localVar = (PsiLocalVariable) element;
        PsiElement elementAt = file.findElementAt(editor.getCaretModel().getOffset());
        if (elementAt instanceof PsiIdentifier && GosuRefactoringUtil.isPsiReferenceExpression(elementAt.getParent())) {
          expr = (PsiExpression) elementAt.getParent();
        } else {
          final PsiReference reference = TargetElementUtilBase.findReference(editor);
          if (reference != null) {
            final PsiElement refElement = reference.getElement();
            if (GosuRefactoringUtil.isPsiReferenceExpression(refElement)) {
              expr = (PsiExpression) refElement;
            }
          }
        }
      } else {
        final PsiLocalVariable variable = PsiTreeUtil.getParentOfType(file.findElementAt(editor.getCaretModel().getOffset()), PsiLocalVariable.class);

        final int offset = editor.getCaretModel().getOffset();
        final PsiElement[] statementsInRange = GosuIntroduceVariableBase.findStatementsAtOffset(editor, file, offset);

        if (statementsInRange.length == 1 && (PsiUtilCore.hasErrorElementChild(statementsInRange[0]) || !GosuRefactoringUtil.isStatementOrExpressionstatement(statementsInRange[0]))) {
          editor.getSelectionModel().selectLineAtCaret();
          final GosuElementToWorkOn elementToWorkOn = getElementToWorkOn(editor, file, refactoringName, helpId, project, localVar, expr);
          if (elementToWorkOn == null || elementToWorkOn.getLocalVariable() == null && elementToWorkOn.getExpression() == null) {
            editor.getSelectionModel().removeSelection();
          }
        }

        if (!editor.getSelectionModel().hasSelection()) {
          final List<PsiExpression> expressions = GosuIntroduceVariableBase.collectExpressions(file, editor, offset, statementsInRange);
          if (expressions.isEmpty()) {
            editor.getSelectionModel().selectLineAtCaret();
          } else if (expressions.size() == 1) {
            expr = expressions.get(0);
          } else {
            IntroduceTargetChooser.showChooser(editor, expressions, new Pass<PsiExpression>() {
              @Override
              public void pass(final PsiExpression selectedValue) {
                PsiLocalVariable var = null; //replace var if selected expression == var initializer
                if (variable != null && variable.getInitializer() == selectedValue) {
                  var = variable;
                }
                processor.pass(getElementToWorkOn(editor, file, refactoringName, helpId, project, var, selectedValue));
              }
            }, new GosuRenderFuction());
            return;
          }
        }
      }
    }


    processor.pass(getElementToWorkOn(editor, file, refactoringName, helpId, project, localVar, expr));
  }

  private static GosuElementToWorkOn getElementToWorkOn(final Editor editor, final PsiFile file,
                                                        final String refactoringName,
                                                        final String helpId,
                                                        final Project project, PsiLocalVariable localVar, PsiExpression expr) {
    int startOffset = 0;
    int endOffset = 0;
    if (localVar == null && expr == null) {
      startOffset = editor.getSelectionModel().getSelectionStart();
      endOffset = editor.getSelectionModel().getSelectionEnd();
      expr = GosuCodeInsightUtil.findExpressionInRange(file, startOffset, endOffset);
      if (expr == null) {
        PsiIdentifier ident = GosuCodeInsightUtil.findElementInRange(file, startOffset, endOffset, PsiIdentifier.class);
        if (ident != null) {
          localVar = PsiTreeUtil.getParentOfType(ident, PsiLocalVariable.class);
        }
      }
    }

    if (expr == null && localVar == null) {
      PsiElement[] statements = GosuCodeInsightUtil.findStatementsInRange(file, startOffset, endOffset);
      if (statements.length == 1 && statements[0] instanceof PsiExpressionStatement) {
        expr = ((PsiExpressionStatement) statements[0]).getExpression();
      } else if (statements.length == 1 && statements[0] instanceof PsiDeclarationStatement) {
        PsiDeclarationStatement decl = (PsiDeclarationStatement) statements[0];
        PsiElement[] declaredElements = decl.getDeclaredElements();
        if (declaredElements.length == 1 && declaredElements[0] instanceof PsiLocalVariable) {
          localVar = (PsiLocalVariable) declaredElements[0];
        }
      }
    }
    if (localVar == null && expr == null) {
      expr = GosuIntroduceVariableBase.getSelectedExpression(project, file, startOffset, endOffset);
    }

    if (localVar == null) {
      if (expr != null) {
        final String errorMessage = GosuIntroduceVariableBase.getErrorMessage(expr);
        if (errorMessage != null) {
          CommonRefactoringUtil.showErrorHint(project, editor, errorMessage, refactoringName, helpId);
          return null;
        }
      }
      if (expr == null) {
        String message = RefactoringBundle.getCannotRefactorMessage(RefactoringBundle.message("error.wrong.caret.position.local.or.expression.name"));
        CommonRefactoringUtil.showErrorHint(project, editor, message, refactoringName, helpId);
        return null;
      }
    }
    return new GosuElementToWorkOn(localVar, expr);
  }
}
