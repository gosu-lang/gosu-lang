/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements;

import com.intellij.psi.PsiElementVisitor;
import gw.lang.parser.statements.IUsesStatementList;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatement;
import gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatementList;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import org.jetbrains.annotations.NotNull;

public class GosuUsesStatementListImpl extends GosuPsiElementImpl<IUsesStatementList> implements IGosuUsesStatementList {
  public GosuUsesStatementListImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }

  public void accept(@NotNull GosuElementVisitor visitor) {
    visitor.visitUsesStatementList(this);
  }

  @NotNull
  @Override
  public IGosuUsesStatement[] getUsesStatements() {
    return findChildrenByClass(IGosuUsesStatement.class);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitUsesStatementList(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
