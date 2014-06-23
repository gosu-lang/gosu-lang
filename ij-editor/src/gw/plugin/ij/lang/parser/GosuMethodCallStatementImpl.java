/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.parser;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiExpressionStatement;
import com.intellij.psi.PsiType;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.parser.statements.IMethodCallStatement;
import gw.lang.reflect.IType;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.psi.api.statements.IGosuStatement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import org.jetbrains.annotations.Nullable;

public class GosuMethodCallStatementImpl extends GosuPsiElementImpl<IMethodCallStatement> implements IGosuTypeElement, IGosuStatement, PsiExpressionStatement {
  public GosuMethodCallStatementImpl(GosuCompositeElement node) {
    super(node);
  }

  @Nullable
  public PsiElement getReferenceNameElement() {
    return findLastChildByType(GosuTokenTypes.TT_IDENTIFIER);
  }

  @Override
  public PsiType getType() {
    return createType(getTypeReferenced());
  }

  private IType getTypeReferenced() {
    IMethodCallStatement parsedElement = getParsedElement();
    IParsedElement pe = parsedElement.getLocation().getChildren().get(0).getParsedElement();
    return ((IMethodCallExpression) pe).getType();
  }

  @Override
  public PsiExpression getExpression() {
    return (PsiExpression) getFirstChild();
  }
}
