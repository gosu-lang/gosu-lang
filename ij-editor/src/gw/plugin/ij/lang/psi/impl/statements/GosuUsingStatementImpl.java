/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements;

import com.intellij.psi.PsiElementVisitor;
import gw.lang.parser.statements.IUsingStatement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import org.jetbrains.annotations.NotNull;

public class GosuUsingStatementImpl extends AbstractStatementWithLocalDeclarationsImpl<IUsingStatement> {
  public GosuUsingStatementImpl(GosuCompositeElement node) {
    super(node);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitUsingStatement(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
