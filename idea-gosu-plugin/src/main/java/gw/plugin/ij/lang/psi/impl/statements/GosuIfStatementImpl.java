/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements;

import com.intellij.psi.PsiElementVisitor;
import gw.lang.parser.statements.IIfStatement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import org.jetbrains.annotations.NotNull;

public class GosuIfStatementImpl extends AbstractStatementWithLocalDeclarationsImpl<IIfStatement> {
  public GosuIfStatementImpl( GosuCompositeElement node ) {
    super(node);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitIfStatement(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
