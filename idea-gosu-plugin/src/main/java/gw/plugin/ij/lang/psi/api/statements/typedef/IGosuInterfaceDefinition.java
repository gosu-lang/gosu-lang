/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements.typedef;

import com.intellij.psi.StubBasedPsiElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeParameterListOwner;
import gw.plugin.ij.lang.psi.stubs.GosuTypeDefinitionStub;
import org.jetbrains.annotations.Nullable;

public interface IGosuInterfaceDefinition extends IGosuTypeDefinition, IGosuTypeParameterListOwner, StubBasedPsiElement<GosuTypeDefinitionStub> {
  @Nullable
  public IGosuImplementsClause getImplementsClause();
}
