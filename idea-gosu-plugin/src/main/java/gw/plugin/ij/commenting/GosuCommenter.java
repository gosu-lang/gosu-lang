/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.commenting;

import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.psi.PsiComment;
import com.intellij.psi.impl.source.tree.JavaDocElementType;
import com.intellij.psi.tree.IElementType;
import gw.plugin.ij.lang.GosuTokenTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuCommenter implements CodeDocumentationAwareCommenter {
  public String getLineCommentPrefix() {
    return "//";
  }

  public String getBlockCommentPrefix() {
    return "/*";
  }

  public String getBlockCommentSuffix() {
    return "*/";
  }

  public String getCommentedBlockCommentPrefix() {
    return null;
  }

  public String getCommentedBlockCommentSuffix() {
    return null;
  }

  @Nullable
  public IElementType getLineCommentTokenType() {
    return GosuTokenTypes.TT_COMMENT_LINE;
  }

  @Nullable
  public IElementType getBlockCommentTokenType() {
    return GosuTokenTypes.TT_COMMENT_MULTILINE;
  }

  @Nullable
  public IElementType getDocumentationCommentTokenType() {
    return JavaDocElementType.DOC_COMMENT;
  }

  @Nullable
  public String getDocumentationCommentPrefix() {
    return "/**";
  }

  @Nullable
  public String getDocumentationCommentLinePrefix() {
    return "*";
  }

  @Nullable
  public String getDocumentationCommentSuffix() {
    return "*/";
  }

  public boolean isDocumentationComment(@NotNull PsiComment element) {
    return element.getText().startsWith(getDocumentationCommentPrefix());
  }
}
