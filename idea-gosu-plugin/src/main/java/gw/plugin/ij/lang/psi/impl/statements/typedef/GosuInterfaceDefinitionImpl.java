/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements.typedef;

import com.intellij.psi.PsiElementVisitor;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuInterfaceDefinition;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.stubs.GosuTypeDefinitionStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GosuInterfaceDefinitionImpl extends GosuTypeDefinitionImpl implements IGosuInterfaceDefinition {
  public GosuInterfaceDefinitionImpl(GosuTypeDefinitionStub stub) {
    super(stub, GosuElementTypes.INTERFACE_DEFINITION);
  }

  public GosuInterfaceDefinitionImpl(GosuCompositeElement node) {
    super(node);
  }

  public boolean isInterface() {
    return true;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitInterfaceDefinition(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}