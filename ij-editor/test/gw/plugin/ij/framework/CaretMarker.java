/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CaretMarker {
  public final int offset;
  public final MarkerType type;
  private final FileMarkers fileMarkers;
  private Editor editor;

  public CaretMarker(int markerIndex, MarkerType markerType, FileMarkers fileMarkers) {
    this.offset = markerIndex;
    this.type = markerType;
    this.fileMarkers = fileMarkers;
  }

  @NotNull
  public String toString() {
    return type + " at " + offset;
  }

  public Editor getEditor() {
    return editor;
  }

  @Nullable
  public PsiFile getFile() {
    return PsiDocumentManager.getInstance(editor.getProject()).getPsiFile(editor.getDocument());
  }

  public void setEditor(Editor editor) {
    this.editor = editor;
  }

  public FileMarkers getParent() {
    return fileMarkers;
  }
}
