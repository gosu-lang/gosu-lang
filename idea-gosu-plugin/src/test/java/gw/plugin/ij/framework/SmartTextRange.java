/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmartTextRange extends TextRange {
  private String text;
  private final FileMarkers parent;
  private Editor editor;

  public SmartTextRange(int startOffset, int endOffset, String text, FileMarkers parent) {
    super(startOffset, endOffset);
    this.text = text;
    this.parent = parent;
  }

  public SmartTextRange(@NotNull PsiElement psi) {
    this(psi.getTextRange().getStartOffset(), psi.getTextRange().getEndOffset(), psi.getText(), null);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SmartTextRange)) {
      return false;
    }
    SmartTextRange that = (SmartTextRange) o;
    return
        this.getStartOffset() == that.getStartOffset() &&
            this.getEndOffset() == that.getEndOffset() &&
            equals(this.text, that.text);
  }

  private boolean equals(@Nullable String text1, @Nullable String text2) {
    if (text1 == null) {
      text1 = "";
    }
    if (text2 == null) {
      text1 = "";
    }
    return text1.equals(text2);
  }

  @Override
  public int hashCode() {
    return 31 * super.hashCode() + (text != null ? text.hashCode() : 0);
  }

  public void setText(String text) {
    this.text = text;
  }

  @NotNull
  public String toString() {
    return "'" + text + "' " + super.toString();
  }

  public void setEditor(Editor editor) {
    this.editor = editor;
  }

  public Editor getEditor() {
    return editor;
  }

  @Nullable
  public PsiFile getFile() {
    return PsiDocumentManager.getInstance(editor.getProject()).getPsiFile(editor.getDocument());
  }

  public FileMarkers getParent() {
    return parent;
  }
}
