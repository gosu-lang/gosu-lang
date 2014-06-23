/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements.typedef;

import com.intellij.psi.PsiElementVisitor;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuExtendsClause;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.stubs.GosuReferenceListStub;
import org.jetbrains.annotations.NotNull;

public class GosuExtendsClauseImpl extends GosuReferenceListImpl implements IGosuExtendsClause {
  public GosuExtendsClauseImpl(GosuCompositeElement node) {
    super(node);
  }

  public GosuExtendsClauseImpl(final GosuReferenceListStub stub) {
    super(stub, GosuElementTypes.EXTENDS_CLAUSE);
  }

  public void accept(@NotNull GosuElementVisitor visitor) {
    visitor.visitExtendsClause(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitExtendsClause(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
