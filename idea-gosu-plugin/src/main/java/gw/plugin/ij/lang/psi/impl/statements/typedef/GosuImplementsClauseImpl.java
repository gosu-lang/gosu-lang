/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements.typedef;

import com.intellij.psi.PsiElementVisitor;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuImplementsClause;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.stubs.GosuReferenceListStub;
import org.jetbrains.annotations.NotNull;

public class GosuImplementsClauseImpl extends GosuReferenceListImpl implements IGosuImplementsClause {
  public GosuImplementsClauseImpl(GosuCompositeElement node) {
    super(node);
  }

  public GosuImplementsClauseImpl(final GosuReferenceListStub stub) {
    super(stub, GosuElementTypes.IMPLEMENTS_CLAUSE);
  }

  public void accept(@NotNull GosuElementVisitor visitor) {
    visitor.visitImplementsClause(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitImplementsClause(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
