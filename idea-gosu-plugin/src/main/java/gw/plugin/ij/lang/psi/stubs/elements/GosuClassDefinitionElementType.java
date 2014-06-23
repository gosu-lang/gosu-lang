/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubElement;
import gw.lang.parser.statements.IClassStatement;
import gw.plugin.ij.filetypes.GosuProgramFileProvider;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuSyntheticCompositeElement;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuClassDefinition;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuClassDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuSyntheticClassDefinitionImpl;
import gw.plugin.ij.lang.psi.stubs.GosuFileStub;
import gw.plugin.ij.lang.psi.stubs.GosuTypeDefinitionStub;
import org.jetbrains.annotations.NotNull;

public class GosuClassDefinitionElementType extends GosuTypeDefinitionElementType<IGosuClassDefinition> {
  public GosuClassDefinitionElementType() {
    super("class definition", IClassStatement.class);
  }

  @NotNull
  public IGosuClassDefinition createPsi(@NotNull GosuTypeDefinitionStub stub) {
    final StubElement parentStub = stub.getParentStub();
    if (parentStub instanceof GosuFileStub &&
        ((GosuFileStub) parentStub).getExt().getString().equalsIgnoreCase('.' + GosuProgramFileProvider.EXT_PROGRAM)) {
      return new GosuSyntheticClassDefinitionImpl(stub);
    }
    return new GosuClassDefinitionImpl(stub);
  }

  @NotNull
  public PsiElement createElement(ASTNode node) {
    if (node instanceof GosuSyntheticCompositeElement) {
      return new GosuSyntheticClassDefinitionImpl((GosuSyntheticCompositeElement) node);
    }
    return new GosuClassDefinitionImpl((GosuCompositeElement) node);
  }
}
