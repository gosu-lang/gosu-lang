/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang;

import com.intellij.codeInsight.daemon.impl.analysis.HighlightVisitorImpl;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 */
public class GosuTokenImpl extends LeafPsiElement implements PsiJavaToken {
  public GosuTokenImpl( IElementType type, CharSequence text ) {
    super( type, text );
  }

  @Override
  public IElementType getTokenType() {
    return getElementType();
  }

  @Override
  public void accept( @NotNull PsiElementVisitor visitor ) {
    if( visitor instanceof JavaElementVisitor && !(visitor instanceof HighlightVisitorImpl)) {
      ((JavaElementVisitor)visitor).visitJavaToken( this );
    }
    else {
      visitor.visitElement( this );
    }
  }

  public String toString() {
    return "GosuTokenImpl: " + getElementType().toString();
  }
}
