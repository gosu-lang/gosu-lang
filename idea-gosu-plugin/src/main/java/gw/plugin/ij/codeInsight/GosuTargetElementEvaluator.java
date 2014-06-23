/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.codeInsight;

import com.intellij.codeInsight.TargetElementEvaluator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import gw.plugin.ij.lang.psi.api.expressions.IGosuReferenceExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuTargetElementEvaluator implements TargetElementEvaluator {
  public boolean includeSelfInGotoImplementation(@NotNull PsiElement element) {
    return false;
  }

  public PsiElement getElementByReference(@NotNull PsiReference ref, int flags) {
    PsiElement sourceElement = ref.getElement();
//## todo:
//    if( sourceElement instanceof IGosuCodeReferenceElement )
//    {
//      GosuNewExpression newExpr;
//
//      if( sourceElement.getParent() instanceof GosuNewExpression )
//      {
//        newExpr = (GosuNewExpression)sourceElement.getParent();
//      }
//      else if( sourceElement.getParent().getParent() instanceof GosuNewExpression )
//      {//anonymous class declaration
//        newExpr = (GosuNewExpression)sourceElement.getParent().getParent();
//      }
//      else
//      {
//        return null;
//      }
//
//      final PsiMethod constructor = newExpr.resolveMethod();
//      final IGosuArgumentList argumentList = newExpr.getArgumentList();
//      if( constructor != null &&
//          argumentList != null &&
//          argumentList.getNamedArguments().length != 0 &&
//          argumentList.getExpressionArguments().length == 0 )
//      {
//        if( constructor.getParameterList().getParametersCount() == 0 )
//        {
//          return constructor.getContainingClass();
//        }
//      }
//
//      return constructor;
//    }

    if (sourceElement instanceof IGosuReferenceExpression) {
      PsiElement resolved = ((IGosuReferenceExpression) sourceElement).resolve();
//      if( resolved instanceof GosuGdkMethod )
//      {
//        return correctSearchTargets( resolved );
//      }
      return resolved;
    }

    return null;
  }

  @Nullable
  public static PsiElement correctSearchTargets(@Nullable PsiElement target) {
    if (target != null && !target.isPhysical()) {
      return target.getNavigationElement();
    }
    return target;
  }
}
