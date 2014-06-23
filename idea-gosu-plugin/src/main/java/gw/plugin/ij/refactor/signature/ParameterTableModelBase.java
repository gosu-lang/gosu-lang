/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.signature;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiCodeFragment;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.changeSignature.ParameterInfo;
import com.intellij.refactoring.changeSignature.ParameterInfoImpl;
import com.intellij.refactoring.ui.StringTableCellEditor;
import com.intellij.ui.*;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import gw.plugin.ij.editors.LightweightGosuEditor;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ParameterTableModelBase extends ListTableModel<ParameterTableModelItem> implements RowEditableTableModel {

  protected final PsiElement myTypeContext;
  protected final PsiElement myDefaultValueContext;

  public ParameterTableModelBase(PsiElement typeContext,
                                 PsiElement defaultValueContext,
                                 ColumnInfo... columnInfos) {
    super(columnInfos);
    myTypeContext = typeContext;
    myDefaultValueContext = defaultValueContext;
  }

  protected abstract ParameterTableModelItem createRowItem(@Nullable ParameterInfoImpl parameterInfo);

  public void addRow() {
    addRow(createRowItem(null));
  }

  public void setParameterInfos(List<ParameterInfoImpl> parameterInfos) {
    List<ParameterTableModelItem> items = new ArrayList<ParameterTableModelItem>(parameterInfos.size());
    for (ParameterInfoImpl parameterInfo : parameterInfos) {
      items.add(createRowItem(parameterInfo));
    }
    setItems(items);
  }

  public void setValueAtWithoutUpdate(Object aValue, int rowIndex, int columnIndex) {
    super.setValueAt(aValue, rowIndex, columnIndex);
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    super.setValueAt(aValue, rowIndex, columnIndex);
    fireTableCellUpdated(rowIndex, columnIndex); // to update signature
  }

  protected static abstract class ColumnInfoBase<Aspect>
    extends ColumnInfo<ParameterTableModelItem, Aspect> {
    private TableCellRenderer myRenderer;
    private TableCellEditor myEditor;

    public ColumnInfoBase(String name) {
      super(name);
    }

    @Override
    public final TableCellEditor getEditor(ParameterTableModelItem o) {
      if (myEditor == null) {
        myEditor = doCreateEditor(o);
      }
      return myEditor;
    }

    @Override
    public final TableCellRenderer getRenderer(ParameterTableModelItem item) {
      if (myRenderer == null) {
        final TableCellRenderer original = doCreateRenderer(item);
        myRenderer = new TableCellRenderer() {

          public Component getTableCellRendererComponent(JTable table,
                                                         Object value,
                                                         boolean isSelected,
                                                         boolean hasFocus,
                                                         int row,
                                                         int column) {
            Component component = original.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!table.isCellEditable(row, table.convertColumnIndexToModel(column))) {
              Color bg = table.getBackground().darker();
              component.setBackground(ColorUtil.toAlpha(bg, 230));
            }

            if (component instanceof SimpleColoredComponent) {
              ((SimpleColoredComponent)component).setIpad(new Insets(0, 0, 0, 0));
            }

            return component;
          }
        };
      }
      return myRenderer;
    }

    protected abstract TableCellRenderer doCreateRenderer(ParameterTableModelItem item);

    protected abstract TableCellEditor doCreateEditor(ParameterTableModelItem item);
  }

  protected static class TypeColumn extends ColumnInfoBase<LightweightGosuEditor> {
    protected final Project myProject;
    private final FileType myFileType;

    public TypeColumn(Project project, FileType fileType) {
      this(project, fileType, RefactoringBundle.message("column.name.type"));
    }

    public TypeColumn(Project project, FileType fileType, String title) {
      super(title);
      myProject = project;
      myFileType = fileType;
    }

    @Override
    public LightweightGosuEditor valueOf(ParameterTableModelItem item) {
      return item.typeCodeFragment;
    }

    @Override
    public boolean isCellEditable(ParameterTableModelItem pParameterTableModelItemBase) {
      return true;
    }

    public TableCellRenderer doCreateRenderer(ParameterTableModelItem pParameterTableModelItemBase) {
      return new LightEditorCellRenderer(myProject, myFileType);
    }

    public TableCellEditor doCreateEditor(ParameterTableModelItem o) {
      return new LightEditorCellEditor(myProject, myFileType);
    }
  }

  protected static class NameColumn extends ColumnInfoBase<String> {
    private final Project myProject;

    public NameColumn(Project project) {
      this(project, RefactoringBundle.message("column.name.name"));
    }

    public NameColumn(Project project, String title) {
      super(title);
      myProject = project;
    }

    @Override
    public String valueOf(ParameterTableModelItem item) {
      return item.parameter.getName();
    }

    @Override
    public void setValue(ParameterTableModelItem item, String value) {
      item.parameter.setName(value);
    }

    @Override
    public boolean isCellEditable(ParameterTableModelItem pParameterTableModelItemBase) {
      return true;
    }

    public TableCellRenderer doCreateRenderer(ParameterTableModelItem item) {
      return new ColoredTableCellRenderer() {
        public void customizeCellRenderer(JTable table, Object value,
                                          boolean isSelected, boolean hasFocus, int row, int column) {
          if (value == null) return;
          append((String)value, new SimpleTextAttributes(Font.PLAIN, null));
        }
      };
    }

    public TableCellEditor doCreateEditor(ParameterTableModelItem o) {
      return new StringTableCellEditor(myProject);
    }
  }

  protected static class DefaultValueColumn extends ColumnInfoBase<LightweightGosuEditor> {
    private final Project myProject;
    private final FileType myFileType;

    public DefaultValueColumn(Project project, FileType fileType) {
      this(project, fileType, RefactoringBundle.message("column.name.default.value"));
    }

    public DefaultValueColumn(Project project, FileType fileType, String title) {
      super(title);
      myProject = project;
      myFileType = fileType;
    }

    @Override
    public boolean isCellEditable(ParameterTableModelItem item) {
      return item.parameter.getOldIndex() == -1;
    }

    @Override
    public LightweightGosuEditor valueOf(ParameterTableModelItem item) {
      return item.defaultValueCodeFragment;
    }

    public TableCellRenderer doCreateRenderer(ParameterTableModelItem item) {
      return new LightEditorCellRenderer(myProject, myFileType);
    }

    public TableCellEditor doCreateEditor(ParameterTableModelItem item) {
      return new LightEditorCellEditor(myProject, myFileType);
    }
  }

  protected static class AnyVarColumn extends ColumnInfoBase<Boolean> {

    public AnyVarColumn() {
      super(RefactoringBundle.message("column.name.any.var"));
    }

    @Override
    public boolean isCellEditable(ParameterTableModelItem item) {
      return item.parameter.getOldIndex() == -1;
    }

    @Override
    public Boolean valueOf(ParameterTableModelItem item) {
      return item.parameter.isUseAnySingleVariable();
    }

    @Override
    public void setValue(ParameterTableModelItem item, Boolean value) {
      item.parameter.setUseAnySingleVariable(value);
    }

    public TableCellRenderer doCreateRenderer(ParameterTableModelItem item) {
      return new BooleanTableCellRenderer();
    }

    public TableCellEditor doCreateEditor(ParameterTableModelItem item) {
      return new BooleanTableCellEditor(false);
    }

    @Override
    public int getWidth(JTable table) {
      final int headerWidth = table.getFontMetrics(table.getFont()).stringWidth(getName()) + 8;
      return Math.max(new JCheckBox().getPreferredSize().width, headerWidth);
    }
  }

}
