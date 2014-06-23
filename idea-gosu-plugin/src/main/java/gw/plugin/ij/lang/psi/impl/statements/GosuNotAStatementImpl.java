/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import org.jetbrains.annotations.NotNull;

public class GosuNotAStatementImpl extends ASTWrapperPsiElement {
  public GosuNotAStatementImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }
}
