/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.editors;

import com.intellij.debugger.DebuggerManager;
import com.intellij.debugger.DebuggerManagerEx;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.EditorMouseAdapter;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorImpl;
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import gw.plugin.ij.util.InjectedElementEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.InputEvent;

public class LightweightGosuEditor implements Disposable {
  private static final int MIN_LINENUMBER_WIDTH = 16;
  @NotNull
  private final Project _project;
  @NotNull
  private final PsiAwareTextEditorImpl editor;
  @NotNull
  private final Document _document;
  @Nullable
  private EditorMouseAdapter editorMouseAdapter;
  private JLabel labelCaption;

  private PsiFile psiFile;
  private VirtualFile vFile;

  public LightweightGosuEditor(@NotNull Project project, @NotNull VirtualFile virtualFile, @Nullable Disposable owner, boolean enableDebug) {
    _project = project;
    vFile = virtualFile;
    editor = (PsiAwareTextEditorImpl) PsiAwareTextEditorProvider.getInstance().createEditor(project, virtualFile);
    if (owner != null) {
      Disposer.register(owner, this);
    }

    EditorImpl editorImpl = (EditorImpl) editor.getEditor();
    _document = editorImpl.getDocument();
    psiFile = PsiDocumentManager.getInstance(project).getPsiFile(_document);
    FileDocumentManagerImpl.registerDocument(_document, virtualFile);

    if (enableDebug) {
//      addBreakpointSupport(editorImpl);
    }
  }

  public LightweightGosuEditor(@NotNull Project project, @NotNull VirtualFile virtualFile, @Nullable Disposable owner) {
    this(project, virtualFile, owner, false);
  }

  private void addBreakpointSupport(@NotNull final EditorImpl editorImpl) {
    editorMouseAdapter = new EditorMouseAdapter() {
      @Override
      public void mouseReleased(@NotNull EditorMouseEvent event) {
        if ((event.getMouseEvent().getModifiers() & InputEvent.BUTTON1_MASK) != 0 && event.getArea() != null && event.getArea().toString().equals("LINE_MARKERS_AREA")) {
          LogicalPosition position = editorImpl.xyToLogicalPosition(event.getMouseEvent().getPoint());
          DebuggerManagerEx debugManager = (DebuggerManagerEx) DebuggerManager.getInstance(_project);
          debugManager.getBreakpointManager().addLineBreakpoint(_document, position.line);
        }
      }
    };
    editorImpl.addEditorMouseListener(editorMouseAdapter);
  }

  @NotNull
  public TextEditor getEditor() {
    return editor;
  }

  @NotNull
  public JComponent getContentComponent() {
    return editor.getEditor().getContentComponent();
  }

  @NotNull
  public JComponent getComponent() {
    return editor.getComponent();
  }

  @NotNull
  public Document getDocument() {
    return _document;
  }

  public PsiFile getPsiFile() {
    return psiFile;
  }

  public VirtualFile getVirtualFile() {
    return vFile;
  }

  public String getText() {
    return getDocument().getText();
  }

  @Nullable
  public String getLabel() {
    return labelCaption == null ? null : labelCaption.getText();
  }

  public void setLabel(String text) {
    if (labelCaption == null) {
      labelCaption = new JLabel("<Script Part>");
      labelCaption.setOpaque(true);
      labelCaption.setFont(getComponent().getFont().deriveFont(Font.BOLD));
      labelCaption.setBorder(new EmptyBorder(0, 4 + MIN_LINENUMBER_WIDTH, 0, 0));
      getEditor().getEditor().setHeaderComponent(labelCaption);
    }
    labelCaption.setText(text);
  }

  public void setOneLineMode(boolean oneLineMode) {
    ((EditorEx) editor.getEditor()).setOneLineMode(oneLineMode);
    setGutterVisible(false);
    getEditor().getEditor().getSettings().setVirtualSpace(false);
    ((EditorImpl) getEditor().getEditor()).setHorizontalScrollbarVisible(false);
    psiFile.putUserData(InjectedElementEditor.SINGLE_LINE_EDITOR, oneLineMode);
  }

  public void setGutterVisible(boolean visible) {
    ((EditorEx) editor.getEditor()).getGutterComponentEx().getParent().setVisible(visible);
  }

  @Override
  public void dispose() {
    if (editorMouseAdapter != null) {
      editor.getEditor().removeEditorMouseListener(editorMouseAdapter);
    }
    editor.dispose();
  }

}
