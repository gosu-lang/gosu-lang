/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions;

import com.intellij.codeInsight.editorActions.wordSelection.JavaWordSelectioner;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiKeyword;
import com.intellij.psi.tree.IElementType;
import gw.plugin.ij.lang.GosuTokenTypes;

public class GosuWordSelectioner extends JavaWordSelectioner {
  @Override
  public boolean canSelect(PsiElement e) {
    if (e instanceof PsiKeyword) {
      return true;
    }
    if (e instanceof PsiJavaToken) {
      IElementType tokenType = ((PsiJavaToken)e).getTokenType();
      return tokenType == GosuTokenTypes.TT_OP_quote_double || tokenType == GosuTokenTypes.TT_OP_quote_single;
    }
    return false;
  }
}
