/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.intoduceField;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDeclarationStatement;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiStatement;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.refactoring.HelpID;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.introduce.inplace.AbstractInplaceIntroducer;
import com.intellij.refactoring.introduceParameter.AbstractJavaInplaceIntroducer;
import com.intellij.refactoring.ui.TypeSelectorManagerImpl;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import com.intellij.refactoring.util.occurrences.ExpressionOccurrenceManager;
import com.intellij.refactoring.util.occurrences.NotInSuperCallOccurrenceFilter;
import com.intellij.refactoring.util.occurrences.NotInThisCallFilter;
import com.intellij.refactoring.util.occurrences.OccurrenceFilter;
import com.intellij.refactoring.util.occurrences.OccurrenceManager;
import gw.plugin.ij.lang.psi.api.statements.IGosuVariable;
import gw.plugin.ij.lang.psi.impl.statements.GosuForEachStatementImpl;
import gw.plugin.ij.refactor.GosuRefactoringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GosuIntroduceFieldHandler extends GosuBaseExpressionToFieldHandler {

  public static final String REFACTORING_NAME = RefactoringBundle.message("introduce.field.title");
  private static final MyOccurrenceFilter MY_OCCURRENCE_FILTER = new MyOccurrenceFilter();
  private GosuInplaceIntroduceFieldPopup myGosuInplaceIntroduceFieldPopup;

  public GosuIntroduceFieldHandler() {
    super(false);
  }

  protected String getRefactoringName() {
    return REFACTORING_NAME;
  }

  protected boolean validClass(PsiClass parentClass, Editor editor) {
    if (parentClass.isInterface()) {
      String message = RefactoringBundle.getCannotRefactorMessage(RefactoringBundle.message("cannot.introduce.field.in.interface"));
      CommonRefactoringUtil.showErrorHint(parentClass.getProject(), editor, message, REFACTORING_NAME, getHelpID());
      return false;
    } else if (!GosuRefactoringUtil.isValidToIntroduceField(parentClass, editor, REFACTORING_NAME, getHelpID())) {
      return false;
    } else {
      return true;
    }
  }

  protected String getHelpID() {
    return HelpID.INTRODUCE_FIELD;
  }

  public void invoke(@NotNull final Project project, final Editor editor, PsiFile file, DataContext dataContext) {
    if (!CommonRefactoringUtil.checkReadOnlyStatus(project, file)) {
      return;
    }
    PsiDocumentManager.getInstance(project).commitAllDocuments();

    GosuElementToWorkOn.processElementToWorkOn(editor, file, REFACTORING_NAME, HelpID.INTRODUCE_FIELD, project, getElementProcessor(project, editor));
  }

  protected Settings showRefactoringDialog(Project project, Editor editor, PsiClass parentClass, PsiExpression expr,
                                           PsiType type,
                                           PsiExpression[] occurrences, PsiElement anchorElement, PsiElement anchorElementIfAll, boolean isLocalVariable) {
    final AbstractInplaceIntroducer activeIntroducer = AbstractInplaceIntroducer.getActiveIntroducer(editor);

    PsiLocalVariable localVariable = null;
    if (isLocalVariable && anchorElement instanceof PsiLocalVariable) {
      localVariable = (PsiLocalVariable) anchorElement;
    } else if (GosuRefactoringUtil.isPsiReferenceExpression(expr)) {
      PsiElement ref = ((PsiReference) expr).resolve();
      if (ref instanceof PsiLocalVariable) {
        localVariable = (PsiLocalVariable) ref;
      }
    }

    String enteredName = null;
    boolean replaceAll = false;
    if (activeIntroducer != null) {
      if (!(activeIntroducer instanceof GosuInplaceIntroduceFieldPopup) || !activeIntroducer.startsOnTheSameElement(expr, localVariable)) {
        AbstractInplaceIntroducer.unableToStartWarning(project, editor);
        return null;
      }
      activeIntroducer.stopIntroduce(editor);
      expr = (PsiExpression) activeIntroducer.getExpr();
      localVariable = (PsiLocalVariable) activeIntroducer.getLocalVariable();
      occurrences = (PsiExpression[]) activeIntroducer.getOccurrences();
      enteredName = activeIntroducer.getInputName();
      replaceAll = activeIntroducer.isReplaceAllOccurrences();
      type = ((AbstractJavaInplaceIntroducer) activeIntroducer).getType();
      GosuIntroduceFieldDialog.ourLastInitializerPlace = ((GosuInplaceIntroduceFieldPopup) activeIntroducer).getInitializerPlace();
    }

    final PsiMethod containingMethod = PsiTreeUtil.getParentOfType(expr != null ? expr : anchorElement, PsiMethod.class);
    final PsiModifierListOwner staticParentElement = PsiUtil.getEnclosingStaticElement(getElement(expr, anchorElement), parentClass);
    boolean declareStatic = staticParentElement != null;

    boolean isInSuperOrThis = false;
    if (!declareStatic) {
      for (int i = 0; !declareStatic && i < occurrences.length; i++) {
        PsiExpression occurrence = occurrences[i];
        isInSuperOrThis = isInSuperOrThis(occurrence);
        declareStatic = isInSuperOrThis;
      }
    }
    int occurrencesNumber = occurrences.length;
    final boolean currentMethodConstructor = containingMethod != null && containingMethod.isConstructor();
    final boolean allowInitInMethod = (!currentMethodConstructor || !isInSuperOrThis) && (anchorElement instanceof PsiLocalVariable || GosuRefactoringUtil.isStatement(anchorElement));
    final boolean allowInitInMethodIfAll = (!currentMethodConstructor || !isInSuperOrThis) && GosuRefactoringUtil.isStatement(anchorElementIfAll);

    if (editor != null && false/*editor.getSettings().isVariableInplaceRenameEnabled()*/ &&
            (expr == null || expr.isPhysical()) && activeIntroducer == null) {
      myGosuInplaceIntroduceFieldPopup =
              new GosuInplaceIntroduceFieldPopup(localVariable, parentClass, declareStatic, currentMethodConstructor, occurrences, expr,
                      new TypeSelectorManagerImpl(project, type, containingMethod, expr, occurrences), editor,
                      allowInitInMethod, allowInitInMethodIfAll, anchorElement, anchorElementIfAll,
                      expr != null ? createOccurrenceManager(expr, parentClass) : null, project);
      if (myGosuInplaceIntroduceFieldPopup.startInplaceIntroduceTemplate()) {
        return null;
      }
    }

    GosuIntroduceFieldDialog dialog = new GosuIntroduceFieldDialog(
            project, parentClass, expr, localVariable,
            currentMethodConstructor,
            localVariable != null, declareStatic, occurrences,
            allowInitInMethod, allowInitInMethodIfAll,
            new TypeSelectorManagerImpl(project, type, containingMethod, expr, occurrences),
            enteredName
    );
    dialog.setReplaceAllOccurrences(replaceAll);
    dialog.show();

    if (!dialog.isOK()) {
      if (occurrencesNumber > 1) {
        WindowManager.getInstance().getStatusBar(project).setInfo(RefactoringBundle.message("press.escape.to.remove.the.highlighting"));
      }
      return null;
    }

    if (!dialog.isDeleteVariable()) {
      localVariable = null;
    }


    return new Settings(dialog.getEnteredName(), expr, occurrences, dialog.isReplaceAllOccurrences(),
            declareStatic, dialog.isDeclareFinal(),
            dialog.getInitializerPlace(), dialog.getFieldVisibility(),
            localVariable,
            dialog.getFieldType(), localVariable != null, (TargetDestination) null, false, false);
  }

  private static PsiElement getElement(PsiExpression expr, PsiElement anchorElement) {
    PsiElement element = null;
    if (expr != null) {
      element = expr.getUserData(GosuElementToWorkOn.PARENT);
      if (element == null) {
        element = expr;
      }
    }
    if (element == null) {
      element = anchorElement;
    }
    return element;
  }

  @Override
  public AbstractInplaceIntroducer getInplaceIntroducer() {
    return myGosuInplaceIntroduceFieldPopup;
  }

  private static boolean isInSuperOrThis(PsiExpression occurrence) {
    return !NotInSuperCallOccurrenceFilter.INSTANCE.isOK(occurrence) || !NotInThisCallFilter.INSTANCE.isOK(occurrence);
  }

  protected OccurrenceManager createOccurrenceManager(final PsiExpression selectedExpr, final PsiClass parentClass) {
    final OccurrenceFilter occurrenceFilter = isInSuperOrThis(selectedExpr) ? null : MY_OCCURRENCE_FILTER;
    return new ExpressionOccurrenceManager(selectedExpr, parentClass, occurrenceFilter, true) {
      @Override
      public PsiElement getAnchorStatementForAllInScope(PsiElement scope) {
        return GosuRefactoringUtil.getAnchorElementForMultipleExpressions(getOccurrences(), scope);
      }
    };
  }

  protected boolean invokeImpl(final Project project, PsiLocalVariable localVariable, final Editor editor) {
    final PsiElement parent = localVariable.getParent();
    if (!(parent instanceof PsiDeclarationStatement || localVariable instanceof IGosuVariable)) {
      String message = RefactoringBundle.getCannotRefactorMessage(RefactoringBundle.message("error.wrong.caret.position.local.or.expression.name"));
      CommonRefactoringUtil.showErrorHint(project, editor, message, REFACTORING_NAME, getHelpID());
      return false;
    } else if (parent instanceof GosuForEachStatementImpl) {
      return false;
    }

    if (!GosuRefactoringUtil.isValidToIntroduceField(localVariable, editor, REFACTORING_NAME, getHelpID())) {
      return false;
    }
    GosuLocalToFieldHandler localToFieldHandler = new GosuLocalToFieldHandler(project, false) {
      @Override
      protected Settings showRefactoringDialog(PsiClass aClass,
                                               PsiLocalVariable local,
                                               PsiExpression[] occurences,
                                               boolean isStatic) {
        final PsiStatement statement = PsiTreeUtil.getParentOfType(local, PsiStatement.class);
        return GosuIntroduceFieldHandler.this.showRefactoringDialog(project, editor, aClass, local.getInitializer(), local.getType(), occurences, local, statement, true);
      }

      @Override
      protected int getChosenClassIndex(List<PsiClass> classes) {
        return GosuIntroduceFieldHandler.this.getChosenClassIndex(classes);
      }
    };
    return localToFieldHandler.convertLocalToField(localVariable, editor);
  }

  protected int getChosenClassIndex(List<PsiClass> classes) {
    return classes.size() - 1;
  }

  private static class MyOccurrenceFilter implements OccurrenceFilter {
    public boolean isOK(PsiExpression occurrence) {
      return !isInSuperOrThis(occurrence);
    }
  }
}
