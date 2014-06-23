/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.debugger.evaluation;

import com.intellij.debugger.SourcePosition;
import com.intellij.debugger.engine.evaluation.EvaluateException;
import com.intellij.debugger.engine.evaluation.expression.EvaluatorBuilder;
import com.intellij.debugger.engine.evaluation.expression.ExpressionEvaluator;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassOwner;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import gw.plugin.ij.lang.psi.IGosuPsiElement;

/**
 */
public class GosuEvaluatorBuilderImpl implements EvaluatorBuilder {
  private static final GosuEvaluatorBuilderImpl INSTANCE = new GosuEvaluatorBuilderImpl();

  public static EvaluatorBuilder instance() {
    return INSTANCE;
  }

  public ExpressionEvaluator build( PsiElement codeFragment, SourcePosition position ) throws EvaluateException {
    PsiClass contextOfType = PsiTreeUtil.getContextOfType( codeFragment, PsiClass.class, false );
    PsiClassOwner ctxFile = (PsiClassOwner)contextOfType.getContainingFile();
    return new GosuExpressionEvaluatorImpl(
      codeFragment.getProject(),
      "DebuggerFragment",
      codeFragment.getText(),
      ctxFile.getClasses()[0].getQualifiedName(),
      contextOfType.getQualifiedName(),
      position.getOffset() );
  }

  private IGosuPsiElement getGosuPsiElementFromCtx( PsiElement context ) {
    if( context == null ) {
      return null;
    }
    if( context instanceof IGosuPsiElement ) {
      return (IGosuPsiElement)context;
    }
    return getGosuPsiElementFromCtx( context.getParent() );
  }
}
