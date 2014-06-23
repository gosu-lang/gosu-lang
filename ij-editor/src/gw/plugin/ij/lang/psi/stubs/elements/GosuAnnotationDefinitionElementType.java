/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import gw.lang.parser.statements.IClasspathStatement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuAnnotationDefinition;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuAnnotationDefinitionImpl;
import gw.plugin.ij.lang.psi.stubs.GosuTypeDefinitionStub;
import org.jetbrains.annotations.NotNull;

public class GosuAnnotationDefinitionElementType extends GosuTypeDefinitionElementType<IGosuAnnotationDefinition> {
  @NotNull
  public IGosuAnnotationDefinition createPsi(@NotNull GosuTypeDefinitionStub stub) {
    return new GosuAnnotationDefinitionImpl(stub);
  }

  public GosuAnnotationDefinitionElementType() {
    super("annotation definition", IClasspathStatement.class);
  }

  @NotNull
  public PsiElement createElement(ASTNode node) {
    return new GosuAnnotationDefinitionImpl((GosuCompositeElement) node);
  }
}