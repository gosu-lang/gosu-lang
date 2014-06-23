/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements;

import com.intellij.psi.PsiElementVisitor;
import gw.lang.parser.statements.IForEachStatement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import org.jetbrains.annotations.NotNull;

public class GosuForEachStatementImpl extends AbstractStatementWithLocalDeclarationsImpl<IForEachStatement> {
  public GosuForEachStatementImpl(GosuCompositeElement node) {
    super(node);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitForEachStatement(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
