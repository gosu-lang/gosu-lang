/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor;

import com.intellij.codeInsight.daemon.impl.quickfix.RemoveUnusedVariableFix;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.*;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.refactoring.safeDelete.JavaSafeDeleteProcessor;
import com.intellij.refactoring.safeDelete.NonCodeUsageSearchInfo;
import com.intellij.refactoring.safeDelete.usageInfo.SafeDeleteReferenceJavaDeleteUsageInfo;
import com.intellij.usageView.UsageInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GosuSafeDeleteProcessor extends JavaSafeDeleteProcessor {

  @Nullable
  public NonCodeUsageSearchInfo findUsages(final PsiElement element, final PsiElement[] allElementsToDelete, final List<UsageInfo> usages) {
    if (element instanceof PsiLocalVariable) {
      Condition<PsiElement> insideDeletedCondition = getUsageInsideDeletedFilter(allElementsToDelete);
      for (PsiReference reference : ReferencesSearch.search(element)) {
        PsiElement referencedElement = reference.getElement();
        final PsiStatement statement = PsiTreeUtil.getParentOfType(referencedElement, PsiStatement.class);

        boolean isSafeToDelete = isAccessedForWriting(referencedElement);
        boolean hasSideEffects = false;
        if (isOnAssignmentLeftHand(referencedElement)) {
          hasSideEffects =
              RemoveUnusedVariableFix.checkSideEffects(((PsiAssignmentExpression) referencedElement.getParent()).getRExpression(), ((PsiLocalVariable) element), new ArrayList<PsiElement>());
        }
        usages.add(new SafeDeleteReferenceJavaDeleteUsageInfo(statement, element, isSafeToDelete && !hasSideEffects));
      }
      return new NonCodeUsageSearchInfo(insideDeletedCondition, element);
    }

    return super.findUsages(element, allElementsToDelete, usages);
  }

  public static boolean isAccessedForWriting(@NotNull PsiElement expr) {
    if (isOnAssignmentLeftHand(expr)) return true;
    PsiElement parent = PsiTreeUtil.skipParentsOfType(expr, PsiParenthesizedExpression.class);
    if (parent instanceof PsiPrefixExpression) {
      IElementType tokenType = ((PsiPrefixExpression) parent).getOperationTokenType();
      return tokenType == JavaTokenType.PLUSPLUS || tokenType == JavaTokenType.MINUSMINUS;
    } else if (parent instanceof PsiPostfixExpression) {
      IElementType tokenType = ((PsiPostfixExpression) parent).getOperationTokenType();
      return tokenType == JavaTokenType.PLUSPLUS || tokenType == JavaTokenType.MINUSMINUS;
    } else {
      return false;
    }
  }

  public static boolean isOnAssignmentLeftHand(@NotNull PsiElement expr) {
    PsiElement parent = PsiTreeUtil.skipParentsOfType(expr, PsiParenthesizedExpression.class);
    return parent instanceof PsiAssignmentExpression &&
        PsiTreeUtil.isAncestor(((PsiAssignmentExpression) parent).getLExpression(), expr, false);
  }
}
