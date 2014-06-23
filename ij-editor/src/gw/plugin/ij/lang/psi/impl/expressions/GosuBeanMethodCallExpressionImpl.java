/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.codeInsight.daemon.impl.analysis.HighlightVisitorImpl;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.JavaResolveResult;
import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReferenceParameterList;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.codeStyle.CodeEditUtil;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.util.PsiMatcherImpl;
import com.intellij.psi.util.PsiMatchers;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.reflect.IMethodInfo;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeParameterList;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiFeatureResolver;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuSyntheticClassDefinitionImpl;
import gw.plugin.ij.lang.psi.util.ElementTypeMatcher;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.ExecutionUtil;
import gw.plugin.ij.util.SafeCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class GosuBeanMethodCallExpressionImpl extends GosuReferenceExpressionImpl<IBeanMethodCallExpression> implements IGosuCodeReferenceElement, IGosuTypeElement, PsiCallExpression {
  public GosuBeanMethodCallExpressionImpl(GosuCompositeElement node) {
    super(node);
  }

  @Nullable
  public PsiElement getReferenceNameElement() {
    PsiElement child = getLastChild();
    while (child != null) {
      final ASTNode node = child.getNode();
      if (node != null && (node.getElementType() == GosuTokenTypes.TT_IDENTIFIER || GosuTokenTypes.isKeyword(node.getElementType()))) return child;
      child = child.getPrevSibling();
    }
    return null;
  }

  @Override
  public IGosuCodeReferenceElement getQualifier() {
    final PsiElement firstChild = getFirstChild();
    return firstChild instanceof IGosuCodeReferenceElement ? (IGosuCodeReferenceElement) firstChild : null;
  }

  @Override
  public void setQualifier(IGosuCodeReferenceElement newQualifier) {
    throw new UnsupportedOperationException("Men at work");
  }

  @Nullable
  public IGosuTypeParameterList getTypeParameterList() {
    return null;
  }

  @Override
  public PsiType[] getTypeArguments() {
    return PsiType.EMPTY_ARRAY;
  }

  @Override
  public PsiElement resolve() {
    return ExecutionUtil.execute(new SafeCallable<PsiElement>(this) {
      @Nullable
      public PsiElement execute() throws Exception {
        final IBeanMethodCallExpression pe = getParsedElement();
        if (pe != null) {
          final IMethodInfo mi = pe.getGenericMethodDescriptor();
          if (mi != null) {
            return PsiFeatureResolver.resolveMethodOrConstructor(mi, GosuBeanMethodCallExpressionImpl.this);
          }
        }
        return null;
      }
    });
  }

  @Override
  public void accept( @NotNull PsiElementVisitor visitor ) {
    if( visitor instanceof JavaElementVisitor && !(visitor instanceof HighlightVisitorImpl) ) {
      ((JavaElementVisitor)visitor).visitCallExpression( this );
    } else if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitBeanMethodCallExpression(this);
    }
    else {
      visitor.visitElement( this );
    }

  }

  @NotNull
  @Override
  public PsiExpressionList getArgumentList() {
    return (GosuExpressionListImpl)findChildByType( GosuElementTypes.ELEM_TYPE_ArgumentListClause );
  }

  @Override
  public PsiMethod resolveMethod() {
    PsiElement ref = resolve();
    return ref instanceof PsiMethod ? (PsiMethod)ref : null;
  }

  @NotNull
  @Override
  public JavaResolveResult resolveMethodGenerics() {
    throw new UnsupportedOperationException( "Not implemented yet" );
  }

  @NotNull
  @Override
  public PsiReferenceParameterList getTypeArgumentList() {
    return null;
  }
}
