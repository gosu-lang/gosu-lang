/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.parser;

import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.tree.IElementType;
import gw.lang.parser.IToken;
import org.jetbrains.annotations.NotNull;

public class GosuTokenElement extends CompositeElement {
  private final IToken token;

  public GosuTokenElement(@NotNull IElementType elemType, IToken token) {
    super(elemType);
    this.token = token;
  }

  public IToken getToken() {
    return token;
  }
}
