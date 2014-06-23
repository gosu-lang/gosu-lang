/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi;

import com.intellij.psi.PsiConstantEvaluationHelper;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.ConstantExpressionEvaluator;
import org.jetbrains.annotations.Nullable;

/**
 */
public class GosuConstantExpressionEvaluator implements ConstantExpressionEvaluator {
  @Override
  public Object computeConstantExpression( PsiElement expression, boolean throwExceptionOnOverflow ) {
    throw new UnsupportedOperationException( "not implemented yet" );
  }

  @Override
  public Object computeExpression( PsiElement expression, boolean throwExceptionOnOverflow, @Nullable PsiConstantEvaluationHelper.AuxEvaluator auxEvaluator ) {
    throw new UnsupportedOperationException( "not implemented yet" );
  }
}
