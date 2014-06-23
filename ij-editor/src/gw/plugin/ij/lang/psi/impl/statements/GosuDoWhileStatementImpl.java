/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements;

import com.intellij.psi.PsiElementVisitor;
import gw.lang.parser.statements.IDoWhileStatement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import org.jetbrains.annotations.NotNull;

public class GosuDoWhileStatementImpl extends AbstractStatementWithLocalDeclarationsImpl<IDoWhileStatement> {
  public GosuDoWhileStatementImpl( GosuCompositeElement node ) {
    super(node);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitDoWhileStatement(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
