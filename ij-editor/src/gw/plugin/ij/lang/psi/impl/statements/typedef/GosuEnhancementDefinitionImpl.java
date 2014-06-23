/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements.typedef;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuEnhancementDefinition;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiTypeResolver;
import gw.plugin.ij.lang.psi.stubs.GosuTypeDefinitionStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GosuEnhancementDefinitionImpl extends GosuTypeDefinitionImpl implements IGosuEnhancementDefinition {
  public GosuEnhancementDefinitionImpl(GosuTypeDefinitionStub stub) {
    super(stub, GosuElementTypes.ENHANCEMENT_DEFINITION);
  }

  public GosuEnhancementDefinitionImpl(GosuCompositeElement node) {
    super(node);
  }

  @Override
  public boolean isEnhancement() {
    return true;
  }

  @Nullable
  @Override
  public PsiElement getEnhancedType() {
    final IClassStatement parsedElement = getParsedElement();
    final IType enhancedType = ((IGosuEnhancement) parsedElement.getGosuClass()).getEnhancedType();
    return PsiTypeResolver.resolveType(enhancedType, this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitEnhancementDefinition(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}