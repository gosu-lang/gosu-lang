/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import gw.lang.parser.statements.IClassStatement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuAnonymousClassDefinition;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuAnonymousClassDefinitionImpl;
import gw.plugin.ij.lang.psi.stubs.GosuTypeDefinitionStub;
import org.jetbrains.annotations.NotNull;

public class GosuAnonymousClassDefinitionElementType extends GosuTypeDefinitionElementType<IGosuAnonymousClassDefinition> {
  @NotNull
  public IGosuAnonymousClassDefinition createPsi(@NotNull GosuTypeDefinitionStub stub) {
    return new GosuAnonymousClassDefinitionImpl(stub);
  }

  public GosuAnonymousClassDefinitionElementType() {
    super("anonymous class definition", IClassStatement.class);
  }

  @NotNull
  public PsiElement createElement(ASTNode node) {
    return new GosuAnonymousClassDefinitionImpl((GosuCompositeElement) node);
  }
}
