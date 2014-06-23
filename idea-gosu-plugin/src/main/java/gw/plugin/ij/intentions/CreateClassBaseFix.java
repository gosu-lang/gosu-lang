/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.plugin.ij.intentions;

import com.intellij.codeInsight.daemon.QuickFixBundle;
import com.intellij.codeInsight.daemon.impl.quickfix.CreateClassKind;
import com.intellij.codeInsight.daemon.impl.quickfix.CreateFromUsageUtils;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiAnonymousClass;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassObjectAccessExpression;
import com.intellij.psi.PsiCodeFragment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiInstanceOfExpression;
import com.intellij.psi.PsiJavaCodeReferenceCodeFragment;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.PsiReferenceList;
import com.intellij.psi.PsiReferenceParameterList;
import com.intellij.psi.PsiTypeCastExpression;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.SmartPsiElementPointer;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.plugin.ij.lang.psi.impl.expressions.GosuExpressionListImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuMethodCallExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuNewExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author ven
 */
public abstract class CreateClassBaseFix extends BaseIntentionAction {
  protected static final Logger LOG = Logger.getInstance(
    "#com.intellij.codeInsight.daemon.impl.actions.CreateClassBaseFix");
  protected final GosuCreateClassKind myKind;
  @NotNull
  private final SmartPsiElementPointer<GosuTypeLiteralImpl> myRefElement;

  public CreateClassBaseFix(GosuCreateClassKind kind, @NotNull final GosuTypeLiteralImpl refElement) {
    myKind = kind;
    myRefElement = SmartPointerManager.getInstance(refElement.getProject()).createLazyPointer(refElement);
  }

  protected abstract String getText(String varName);

  private boolean isAvailableInContext(final @NotNull GosuTypeLiteralImpl element) {
    return true;
//    PsiElement parent = element.getParent();
//
//    if (parent instanceof PsiJavaCodeReferenceCodeFragment) return true;
//
//    if (parent instanceof PsiTypeElement) {
//      if (parent.getParent() instanceof PsiReferenceParameterList) return true;
//
//      while (parent.getParent() instanceof PsiTypeElement) parent = parent.getParent();
//      if (parent.getParent() instanceof PsiCodeFragment ||
//          parent.getParent() instanceof PsiVariable ||
//          parent.getParent() instanceof PsiMethod ||
//          parent.getParent() instanceof PsiClassObjectAccessExpression ||
//          parent.getParent() instanceof PsiTypeCastExpression ||
//          (parent.getParent() instanceof PsiInstanceOfExpression && ((PsiInstanceOfExpression)parent.getParent()).getCheckType() == parent)) {
//        return true;
//      }
//    }
//    else if (parent instanceof PsiReferenceList) {
//      if (myKind == CreateClassKind.ENUM) return false;
//      if (parent.getParent() instanceof PsiClass) {
//        PsiClass psiClass = (PsiClass)parent.getParent();
//        if (psiClass.getExtendsList() == parent) {
//          if (myKind == CreateClassKind.CLASS && !psiClass.isInterface()) return true;
//          if (myKind == CreateClassKind.INTERFACE && psiClass.isInterface()) return true;
//        }
//        if (psiClass.getImplementsList() == parent && myKind == CreateClassKind.INTERFACE) return true;
//      }
//      else if (parent.getParent() instanceof PsiMethod) {
//        PsiMethod method = (PsiMethod)parent.getParent();
//        if (method.getThrowsList() == parent && myKind == CreateClassKind.CLASS) return true;
//      }
//    }
//    else if (parent instanceof PsiAnonymousClass && ((PsiAnonymousClass)parent).getBaseClassReference() == element) {
//      return true;
//    }
//
//    if (element instanceof PsiReferenceExpression) {
//      if (parent instanceof PsiMethodCallExpression) {
//        return false;
//      }
//      return !(parent.getParent() instanceof PsiMethodCallExpression) || myKind == CreateClassKind.CLASS;
//    }
//    return false;
  }

  private static boolean checkClassName(@NotNull String name) {
    return Character.isUpperCase(name.charAt(0));
  }

  public boolean isAvailable(@NotNull final Project project, @NotNull final Editor editor, final PsiFile file) {
    final GosuTypeLiteralImpl element = getRefElement();
    if (element == null ||
        !element.getManager().isInProject(element) ||
        CreateFromUsageUtils.isValidReference(element, true)) return false;
    final String refName = element.getReferenceName();
    if (refName == null || !checkClassName(refName)) return false;
    PsiElement nameElement = element.getReferenceNameElement();
    if (nameElement == null) return false;
//    PsiElement parent = element.getParent();
//    if (parent instanceof PsiExpression && !(parent instanceof PsiReferenceExpression)) return false;
    if (!isAvailableInContext(element)) return false;
    final int offset = editor.getCaretModel().getOffset();
    if (CreateFromUsageUtils.shouldShowTag(offset, nameElement, element)) {
      setText(getText(nameElement.getText()));
      return true;
    }

    return false;
  }

  @NotNull
  public String getFamilyName() {
    return QuickFixBundle.message("create.class.from.usage.family");
  }

  @Nullable
  protected GosuTypeLiteralImpl getRefElement() {
    return myRefElement.getElement();
  }
}
