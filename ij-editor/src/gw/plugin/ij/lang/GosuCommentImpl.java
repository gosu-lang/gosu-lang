/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang;

import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.tree.IElementType;

/**
 */
public class GosuCommentImpl extends PsiCommentImpl {
  public GosuCommentImpl( IElementType type, CharSequence text ) {
    super( type, text );
  }
}
