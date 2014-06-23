/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.signature;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.ui.EditorTextField;
import gw.plugin.ij.editors.LightweightGosuEditor;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class LightEditorCellEditor extends AbstractCellEditor implements TableCellEditor {

  private Document myDocument;
  protected LightweightGosuEditor myCode;
  private final Project myProject;
  private final FileType myFileType;
  protected EditorTextField myEditorTextField;
  private Set<DocumentListener> myListeners = new HashSet<DocumentListener>();

  public LightEditorCellEditor(final Project project, FileType fileType) {
    myProject = project;
    myFileType = fileType;
  }

  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    myCode = (LightweightGosuEditor)value;
    myDocument = myCode.getDocument();
    for (DocumentListener listener : myListeners) {
      myDocument.addDocumentListener(listener);
    }
    return myCode.getComponent();
  }

  public LightweightGosuEditor getCellEditorValue() {
    return myCode;
  }

  public boolean stopCellEditing() {
    super.stopCellEditing();
    PsiDocumentManager.getInstance(myProject).commitDocument(myDocument);
    return true;
  }

  public void addDocumentListener(DocumentListener listener) {
    myListeners.add(listener);
  }

  public void clearListeners() {
    myListeners.clear();
  }
}
