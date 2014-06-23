/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import gw.lang.parser.statements.IClassStatement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuInterfaceDefinition;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuInterfaceDefinitionImpl;
import gw.plugin.ij.lang.psi.stubs.GosuTypeDefinitionStub;
import org.jetbrains.annotations.NotNull;

public class GosuInterfaceDefinitionElementType extends GosuTypeDefinitionElementType<IGosuInterfaceDefinition> {
  @NotNull
  public IGosuInterfaceDefinition createPsi(@NotNull GosuTypeDefinitionStub stub) {
    return new GosuInterfaceDefinitionImpl(stub);
  }

  public GosuInterfaceDefinitionElementType() {
    super("interface definition", IClassStatement.class);
  }

  @NotNull
  public PsiElement createElement(ASTNode node) {
    return new GosuInterfaceDefinitionImpl((GosuCompositeElement) node);
  }
}