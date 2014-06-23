/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.parser;

import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class GosuCompositeElement extends CompositeElement {
  public GosuCompositeElement(@NotNull IElementType elementType) {
    super(elementType);
  }
}
