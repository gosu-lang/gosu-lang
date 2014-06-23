/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.parser;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class GosuSyntheticCompositeElement extends GosuCompositeElement {
  public GosuSyntheticCompositeElement(@NotNull IElementType elementType) {
    super(elementType);
  }
}
