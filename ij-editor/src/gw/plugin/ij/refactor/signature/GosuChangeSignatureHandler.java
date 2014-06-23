
/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.signature;

import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.ide.util.SuperMethodWarningUtil;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.HelpID;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.changeClassSignature.ChangeClassSignatureDialog;
import com.intellij.refactoring.changeSignature.ChangeSignatureGestureDetector;
import com.intellij.refactoring.changeSignature.ChangeSignatureUtil;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import gw.plugin.ij.lang.psi.api.statements.IGosuStatementList;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuChangeSignatureHandler implements com.intellij.refactoring.changeSignature.ChangeSignatureHandler {

  private final ChangeSignatureHandler initSubstisutor;

  public GosuChangeSignatureHandler() {
    this(ChangeSignatureHandler.NULL);
  }

  public GosuChangeSignatureHandler(ChangeSignatureHandler initSubstisutor) {
    this.initSubstisutor = initSubstisutor;
  }



  public void invoke(@NotNull Project project, Editor editor, PsiFile file, DataContext dataContext) {
    editor.getScrollingModel().scrollToCaret(ScrollType.MAKE_VISIBLE);
    PsiElement element = findTargetMember(file, editor);
    if (element == null) {
      element = LangDataKeys.PSI_ELEMENT.getData(dataContext);
    }
    invokeOnElement(project, editor, element, initSubstisutor);
  }

  public static boolean invokeOnElement(Project project, Editor editor,
                                      PsiElement element, ChangeSignatureHandler initSubstisutor) {
    if (element instanceof PsiMethod) {
      final ChangeSignatureGestureDetector detector = ChangeSignatureGestureDetector.getInstance(project);
      final PsiIdentifier nameIdentifier = ((PsiMethod)element).getNameIdentifier();
      if (nameIdentifier != null && detector.isChangeSignatureAvailable(element)) {
        detector.changeSignature(element.getContainingFile(), false);
        return true;
      }
      return invoke((PsiMethod) element, project, editor, initSubstisutor);
    }
    else if (element instanceof PsiClass) {
      return invoke((PsiClass) element, editor);
    }
    else {
      String message = RefactoringBundle.getCannotRefactorMessage(RefactoringBundle.message("error.wrong.caret.position.method.or.class.name"));
      CommonRefactoringUtil.showErrorHint(project, editor, message, REFACTORING_NAME, HelpID.CHANGE_SIGNATURE);
      return false;
    }
  }

  public void invoke(@NotNull final Project project, @NotNull final PsiElement[] elements, @Nullable final DataContext dataContext) {
    if (elements.length != 1) return;
    final Editor editor = dataContext != null ? PlatformDataKeys.EDITOR.getData(dataContext) : null;


    invokeOnElement(project, editor, elements[0], initSubstisutor);


//    ApplicationManager.getApplication().runWriteAction(new Runnable() {
//      public void run() {
//        invokeOnElement(project, editor, elements[0]);
//      }
//    });
  }

  @Nullable
  @Override
  public String getTargetNotFoundMessage() {
    return RefactoringBundle.message("error.wrong.caret.position.method.or.class.name");
  }

  private static boolean invoke(final PsiMethod method, final Project project,
                             @Nullable final Editor editor,
                             ChangeSignatureHandler initSubstisutor) {
    PsiMethod newMethod = SuperMethodWarningUtil.checkSuperMethod(method, RefactoringBundle.message("to.refactor"));
    if (newMethod == null) return false;

    if (!newMethod.equals(method)) {
      ChangeSignatureUtil.invokeChangeSignatureOn(newMethod, project);
      return true;
    }

    if (!CommonRefactoringUtil.checkReadOnlyStatus(project, method)) return false;

    final PsiClass containingClass = method.getContainingClass();
    final PsiReferenceExpression refExpr = editor != null ? TargetElementUtil.findReferenceExpression(editor) : null;
    final boolean allowDelegation = containingClass != null && !containingClass.isInterface();
    final DialogWrapper dialog =
            new GosuChangeSignatureDialog(project,
            new GosuMethodDescriptor((IGosuMethod) method, initSubstisutor), allowDelegation, refExpr, initSubstisutor);
    return dialog.showAndGet();
  }

  private static boolean invoke(final PsiClass aClass, Editor editor) {
    final PsiTypeParameterList typeParameterList = aClass.getTypeParameterList();
    Project project = aClass.getProject();
    if (typeParameterList == null) {
      final String message = RefactoringBundle.getCannotRefactorMessage(RefactoringBundle.message("changeClassSignature.no.type.parameters"));
      CommonRefactoringUtil.showErrorHint(project, editor, message, REFACTORING_NAME, HelpID.CHANGE_CLASS_SIGNATURE);
      return false;
    }
    if (!CommonRefactoringUtil.checkReadOnlyStatus(project, aClass)) return false;

    ChangeClassSignatureDialog dialog = new ChangeClassSignatureDialog(aClass, true);
    //if (!ApplicationManager.getApplication().isUnitTestMode()){

    return dialog.showAndGet();
    //}else {
    //  dialog.showAndGetOk()
    //}
  }

  @Nullable
  public PsiElement findTargetMember(PsiFile file, Editor editor) {
    PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
    return findTargetMember(element);
  }

  public PsiElement findTargetMember(PsiElement element) {

    IGosuMethod method = PsiTreeUtil.getParentOfType(element, IGosuMethod.class);
    IGosuStatementList stList = PsiTreeUtil.getParentOfType(element, IGosuStatementList.class);
    if (method != null && stList == null) {
      return method;
    }

    if (PsiTreeUtil.getParentOfType(element, PsiParameterList.class) != null) {
      return PsiTreeUtil.getParentOfType(element, PsiMethod.class);
    }

    final PsiTypeParameterList typeParameterList = PsiTreeUtil.getParentOfType(element, PsiTypeParameterList.class);
    if (typeParameterList != null) {
      return PsiTreeUtil.getParentOfType(typeParameterList, PsiMember.class);
    }

    final PsiElement elementParent = element.getParent();
    if (elementParent instanceof PsiMethod && ((PsiMethod)elementParent).getNameIdentifier()==element) {
      final PsiClass containingClass = ((PsiMethod)elementParent).getContainingClass();
      if (containingClass != null && containingClass.isAnnotationType()) {
        return null;
      }
      return elementParent;
    }
    if (elementParent instanceof PsiClass && ((PsiClass)elementParent).getNameIdentifier()==element) {
      if (((PsiClass)elementParent).isAnnotationType()) {
        return null;
      }
      return elementParent;
    }

    final PsiCallExpression expression = PsiTreeUtil.getParentOfType(element, PsiCallExpression.class);
    if (expression != null) {
      final PsiExpression qualifierExpression;
      if (expression instanceof PsiMethodCallExpression) {
        qualifierExpression = ((PsiMethodCallExpression)expression).getMethodExpression().getQualifierExpression();
      } else if (expression instanceof PsiNewExpression) {
        qualifierExpression = ((PsiNewExpression)expression).getQualifier();
      } else {
        qualifierExpression = null;
      }
      if (PsiTreeUtil.isAncestor(qualifierExpression, element, false)) {
        final PsiExpressionList expressionList = PsiTreeUtil.getParentOfType(qualifierExpression, PsiExpressionList.class);
        if (expressionList != null) {
          final PsiElement parent = expressionList.getParent();
          if (parent instanceof PsiCallExpression) {
            return ((PsiCallExpression)parent).resolveMethod();
          }
        }
      }
      else {
        return expression.resolveMethod();
      }
    }

    final PsiReferenceParameterList referenceParameterList = PsiTreeUtil.getParentOfType(element, PsiReferenceParameterList.class);
    if (referenceParameterList != null) {
      final PsiJavaCodeReferenceElement referenceElement =
              PsiTreeUtil.getParentOfType(referenceParameterList, PsiJavaCodeReferenceElement.class);
      if (referenceElement != null) {
        final PsiElement resolved = referenceElement.resolve();
        if (resolved instanceof PsiClass) {
          return resolved;
        }
        else if (resolved instanceof PsiMethod) {
          return resolved;
        }
      }
    }
    return null;
  }
}
