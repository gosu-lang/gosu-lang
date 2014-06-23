/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import gw.lang.parser.statements.IClassStatement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuEnumDefinition;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuEnumDefinitionImpl;
import gw.plugin.ij.lang.psi.stubs.GosuTypeDefinitionStub;
import org.jetbrains.annotations.NotNull;

public class GosuEnumDefinitionElementType extends GosuTypeDefinitionElementType<IGosuEnumDefinition> {
  @NotNull
  public IGosuEnumDefinition createPsi(@NotNull GosuTypeDefinitionStub stub) {
    return new GosuEnumDefinitionImpl(stub);
  }

  public GosuEnumDefinitionElementType() {
    super("enum definition", IClassStatement.class);
  }

  @NotNull
  public PsiElement createElement(ASTNode node) {
    return new GosuEnumDefinitionImpl((GosuCompositeElement) node);
  }
}