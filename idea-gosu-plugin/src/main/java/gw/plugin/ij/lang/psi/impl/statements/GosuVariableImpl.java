/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements;

import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.statements.IGosuLocalVariable;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuVariableImpl<E extends IParsedElementWithAtLeastOneDeclaration> extends GosuVariableBaseImpl<E, StubElement> implements IGosuLocalVariable {
  public GosuVariableImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }

  public void accept(@NotNull GosuElementVisitor visitor) {
    visitor.visitVariable(this);
  }

  public String toString() {
    return "Local Variable";
  }

  @Override
  public void setInitializer(@Nullable PsiExpression initializer) throws IncorrectOperationException {
    throw new UnsupportedOperationException("Men at work");
  }

  public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
    return processor.execute(this, state);
  }

  @Override
  public void accept( @NotNull PsiElementVisitor visitor ) {
    if( visitor instanceof GosuElementVisitor ) {
      ((GosuElementVisitor)visitor).visitVariable(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
