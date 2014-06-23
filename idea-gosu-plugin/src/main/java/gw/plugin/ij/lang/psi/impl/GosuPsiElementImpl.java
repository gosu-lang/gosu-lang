/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.psi.stubs.StubElement;
import gw.lang.parser.IParsedElement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import org.jetbrains.annotations.NotNull;

public abstract class GosuPsiElementImpl<T extends IParsedElement> extends GosuBaseElementImpl<T, StubElement> implements IGosuPsiElement {
  public GosuPsiElementImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }
}
