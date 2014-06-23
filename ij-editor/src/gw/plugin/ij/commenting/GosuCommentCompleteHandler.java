/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.commenting;

import com.intellij.codeInsight.editorActions.CommentCompleteHandler;
import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.JavaDocTokenType;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocToken;
import gw.plugin.ij.lang.GosuLanguage;

public class GosuCommentCompleteHandler implements CommentCompleteHandler {
  @Override
  public boolean isCommentComplete(PsiComment comment, CodeDocumentationAwareCommenter commenter, Editor editor) {
    final PsiElement first = comment.getFirstChild();
    final PsiElement last = comment.getLastChild();
    return first instanceof PsiDocToken && last instanceof PsiDocToken &&
        ((PsiDocToken)first).getTokenType() == JavaDocTokenType.DOC_COMMENT_START &&
        ((PsiDocToken)last).getTokenType() == JavaDocTokenType.DOC_COMMENT_END;
  }

  @Override
  public boolean isApplicable(PsiComment comment, CodeDocumentationAwareCommenter commenter) {
    return comment instanceof PsiDocComment &&
        comment.getParent().getLanguage() == GosuLanguage.instance();
  }
}
