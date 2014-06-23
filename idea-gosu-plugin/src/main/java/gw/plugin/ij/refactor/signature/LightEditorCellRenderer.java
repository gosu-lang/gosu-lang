/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.signature;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import gw.plugin.ij.editors.LightweightGosuEditor;
import gw.plugin.ij.lang.GosuLanguage;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;


public class LightEditorCellRenderer implements TableCellRenderer {

  private final Project myProject;
  private final FileType myFileType;

  public LightEditorCellRenderer(Project project) {
    this(project, GosuLanguage.instance().getAssociatedFileType());
  }

  public LightEditorCellRenderer(Project project, FileType fileType) {
    myProject = project;
    myFileType = fileType;
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, final boolean hasFocus, int row, int column) {
    LightweightGosuEditor code = (LightweightGosuEditor) value;

    final EditorTextField editorTextField;
    Document document = null;

    final Color bg = table.getSelectionBackground();
    final Color fg = table.getSelectionForeground();
    JComponent base = code.getComponent();
    base.setForeground(fg);
    base.setBackground(bg);

    return base;
  }
}
