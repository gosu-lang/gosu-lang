/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import gw.lang.parser.statements.IClassStatement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuEnhancementDefinition;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuEnhancementDefinitionImpl;
import gw.plugin.ij.lang.psi.stubs.GosuTypeDefinitionStub;
import org.jetbrains.annotations.NotNull;

public class GosuEnhancementDefinitionElementType extends GosuTypeDefinitionElementType<IGosuEnhancementDefinition> {
  @NotNull
  public IGosuEnhancementDefinition createPsi(@NotNull GosuTypeDefinitionStub stub) {
    return new GosuEnhancementDefinitionImpl(stub);
  }

  public GosuEnhancementDefinitionElementType() {
    super("enhancement definition", IClassStatement.class);
  }

  @NotNull
  public PsiElement createElement(ASTNode node) {
    return new GosuEnhancementDefinitionImpl((GosuCompositeElement) node);
  }
}