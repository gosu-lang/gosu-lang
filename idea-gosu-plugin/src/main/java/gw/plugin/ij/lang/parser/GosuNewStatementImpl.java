/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.parser;

import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiExpressionStatement;
import com.intellij.psi.PsiType;
import gw.lang.parser.statements.INewStatement;
import gw.plugin.ij.lang.psi.api.statements.IGosuStatement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import org.jetbrains.annotations.NotNull;

public class GosuNewStatementImpl extends GosuPsiElementImpl<INewStatement> implements IGosuTypeElement, IGosuStatement, PsiExpressionStatement {
  public GosuNewStatementImpl(GosuCompositeElement node) {
    super(node);
  }

  @Override
  public PsiType getType() {
    return getExpression().getType();
  }

  @NotNull
  @Override
  public PsiExpression getExpression() {
    return (PsiExpression) getFirstChild();
  }
}
