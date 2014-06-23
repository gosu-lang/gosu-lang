/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.psi.*;
import com.intellij.psi.scope.NameHint;
import com.intellij.psi.scope.PsiScopeProcessor;
import gw.lang.parser.IExpression;
import gw.lang.parser.expressions.IBlockExpression;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.expressions.IGosuExpression;
import gw.plugin.ij.lang.psi.api.statements.IGosuParametersOwner;
import gw.plugin.ij.lang.psi.api.statements.params.IGosuParameter;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import gw.plugin.ij.lang.psi.impl.statements.params.GosuParameterListImpl;
import org.jetbrains.annotations.NotNull;


public class GosuBlockExpressionImpl extends GosuPsiElementImpl<IBlockExpression> implements IGosuParametersOwner, IGosuExpression {
  public GosuBlockExpressionImpl(GosuCompositeElement node) {
    super(node);
  }

  @NotNull
  @Override
  public IGosuParameter[] getParameters() {
    final GosuParameterListImpl parameters = findChildByClass(GosuParameterListImpl.class);
    return parameters != null ? parameters.getParameters() : IGosuParameter.EMPTY_ARRAY;
  }

  public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                     @NotNull ResolveState state,
                                     PsiElement lastParent,
                                     @NotNull PsiElement place) {
    for (IGosuParameter parameter : getParameters()) {
      if (!processElement(processor, parameter, state)) {
        return false;
      }
    }

    processor.handleEvent(DECLARATION_SCOPE_PASSED, this);
    return true;
  }

  public static final PsiScopeProcessor.Event DECLARATION_SCOPE_PASSED = new PsiScopeProcessor.Event() {
  };

  boolean processElement(@NotNull PsiScopeProcessor processor, @NotNull PsiNamedElement namedElement, ResolveState state) {
    NameHint nameHint = processor.getHint(NameHint.KEY);
    String name = nameHint == null ? null : nameHint.getName(state);
    if (name == null || name.equals(namedElement.getName())) {
      return processor.execute(namedElement, state);
    }
    return true;
  }

  @Override
  public PsiType getType() {
    IExpression expr = getParsedElement();
    return expr != null ? createType( expr.getType() ) : null;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitBlockExpression(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
