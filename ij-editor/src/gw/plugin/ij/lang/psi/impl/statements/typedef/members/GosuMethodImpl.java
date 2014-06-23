/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements.typedef.members;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.StubBasedPsiElement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.stubs.GosuMethodStub;
import org.jetbrains.annotations.NotNull;

public class GosuMethodImpl extends GosuMethodBaseImpl<GosuMethodStub> implements IGosuMethod, StubBasedPsiElement<GosuMethodStub> {
  public GosuMethodImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }

  public GosuMethodImpl(@NotNull GosuMethodStub stub) {
    super(stub, GosuElementTypes.METHOD_DEFINITION);
  }

  @Override
  public String[] getNamedParametersArray() {
    final GosuMethodStub stub = getStub();
    if (stub != null) {
      return stub.getNamedParameters();
    }
    return super.getNamedParametersArray();
  }

  @NotNull
  @Override
  public String getName() {
    final GosuMethodStub stub = getStub();
    if (stub != null) {
      return stub.getName();
    }
    return super.getName();
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitMethod(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}