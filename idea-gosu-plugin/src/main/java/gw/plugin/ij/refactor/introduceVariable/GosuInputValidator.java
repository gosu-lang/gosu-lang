/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.introduceVariable;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiVariable;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.introduceVariable.IntroduceVariableSettings;
import com.intellij.refactoring.rename.JavaUnresolvableLocalCollisionDetector;
import com.intellij.refactoring.util.RefactoringUIUtil;
import com.intellij.refactoring.util.occurrences.ExpressionOccurrenceManager;
import com.intellij.util.containers.HashSet;
import com.intellij.util.containers.MultiMap;

/**
 * copy of Intellij InputValidator
 */
public class GosuInputValidator implements GosuIntroduceVariableBase.Validator {
  private final Project myProject;
  private final PsiElement myAnchorStatementIfAll;
  private final PsiElement myAnchorStatement;
  private final ExpressionOccurrenceManager myOccurenceManager;
  private final GosuIntroduceVariableBase myIntroduceVariableBase;

  public boolean isOK(IntroduceVariableSettings settings) {
    String name = settings.getEnteredName();
    final PsiElement anchor;
    final boolean replaceAllOccurrences = settings.isReplaceAllOccurrences();
    if (replaceAllOccurrences) {
      anchor = myAnchorStatementIfAll;
    } else {
      anchor = myAnchorStatement;
    }
    final PsiElement scope = anchor.getParent();
    if (scope == null) {
      return true;
    }
    final MultiMap<PsiElement, String> conflicts = new MultiMap<>();
    final HashSet<PsiVariable> reportedVariables = new HashSet<>();
    JavaUnresolvableLocalCollisionDetector.CollidingVariableVisitor visitor = new JavaUnresolvableLocalCollisionDetector.CollidingVariableVisitor() {
      public void visitCollidingElement(PsiVariable collidingVariable) {
        if (!reportedVariables.contains(collidingVariable)) {
          reportedVariables.add(collidingVariable);
          String message = RefactoringBundle.message("introduced.variable.will.conflict.with.0", RefactoringUIUtil.getDescription(collidingVariable, true));
          conflicts.putValue(collidingVariable, message);
        }
      }
    };
    JavaUnresolvableLocalCollisionDetector.visitLocalsCollisions(anchor, name, scope, anchor, visitor);
    if (replaceAllOccurrences) {
      final PsiExpression[] occurences = myOccurenceManager.getOccurrences();
      for (PsiExpression occurence : occurences) {
        GosuIntroduceVariableBase.checkInLoopCondition(occurence, conflicts);
      }
    } else {
      GosuIntroduceVariableBase.checkInLoopCondition(myOccurenceManager.getMainOccurence(), conflicts);
    }

    if (conflicts.size() > 0) {
      return myIntroduceVariableBase.reportConflicts(conflicts, myProject, settings);
    } else {
      return true;
    }
  }


  public GosuInputValidator(final GosuIntroduceVariableBase introduceVariableBase,
                            Project project,
                            PsiElement anchorStatementIfAll,
                            PsiElement anchorStatement,
                            ExpressionOccurrenceManager occurenceManager) {
    myIntroduceVariableBase = introduceVariableBase;
    myProject = project;
    myAnchorStatementIfAll = anchorStatementIfAll;
    myAnchorStatement = anchorStatement;
    myOccurenceManager = occurenceManager;
  }
}

