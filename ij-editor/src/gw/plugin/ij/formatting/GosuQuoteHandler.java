/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.psi.JavaTokenType;
import com.intellij.psi.tree.IElementType;

public class GosuQuoteHandler extends SimpleTokenSetQuoteHandler {
  public GosuQuoteHandler() {
    super(JavaTokenType.STRING_LITERAL, JavaTokenType.CHARACTER_LITERAL);
  }
}
