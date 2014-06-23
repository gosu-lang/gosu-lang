/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.plugin.ij.refactor.signature;

import com.intellij.codeInsight.completion.JavaCompletionUtil;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiType;
import com.intellij.psi.codeStyle.VariableKind;
import com.intellij.refactoring.changeSignature.ParameterInfoImpl;
import com.intellij.refactoring.ui.StringTableCellEditor;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.ColoredTableCellRenderer;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ui.ColumnInfo;
import gw.plugin.ij.completion.handlers.SymbolCompletionHandler;
import gw.plugin.ij.editors.LightweightGosuEditor;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.psi.impl.GosuFragmentFileImpl;
import gw.plugin.ij.lang.psi.impl.types.CompletionVoter;
import gw.plugin.ij.refactor.GosuRefactoringUtil;
import gw.plugin.ij.util.LightVirtualFileWithModule;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedHashSet;
import java.util.Set;

import static gw.plugin.ij.refactor.GosuRefactoringUtil.findFragmentType;

public class GosuParameterTableModel extends ParameterTableModelBase {

  private final Project myProject;

  private GosuChangeSignatureDialog dialog;

  public GosuParameterTableModel(final PsiElement typeContext,
                                 PsiElement defaultValueContext,
                                 final GosuChangeSignatureDialog dialog) {
    this(typeContext, defaultValueContext,
         new JavaTypeColumn(typeContext.getProject()),
         new JavaNameColumn(typeContext.getProject()),
         new DefaultValueColumn(typeContext.getProject(), GosuLanguage.instance().getAssociatedFileType()),
                 new AnyVarColumn() {
        @Override
        public boolean isCellEditable(ParameterTableModelItem item) {
          return dialog.isGenerateDelegate() && super.isCellEditable(item);
        }
      });
    this.dialog = dialog;
  }

  protected GosuParameterTableModel(PsiElement typeContext, PsiElement defaultValueContext, ColumnInfo... columns) {
    super(typeContext, defaultValueContext, columns);
    myProject = typeContext.getProject();
  }

  private static int id;
  private static synchronized String  genName() {
    return GosuParameterTableModel.class.getName() + "_" + id++;
  }

  public LightweightGosuEditor createDefaultValueEditor(String content) {
    final VirtualFile virtualFile = PsiFileFactory.getInstance(myProject).
        createFileFromText(genName(), GosuLanguage.instance(), content, true, false).
        getVirtualFile();
    return makeGosuEditor(virtualFile);
  }

  private LightweightGosuEditor makeGosuEditor(VirtualFile virtualFile) {
    LightVirtualFileWithModule.attachModule((LightVirtualFile) virtualFile, dialog.getModule());
    LightweightGosuEditor editor = new LightweightGosuEditor(myProject, virtualFile, dialog.getDisposable());
    editor.setOneLineMode(true);
    return editor;
  }

  @Override
  protected ParameterTableModelItem createRowItem(@Nullable ParameterInfoImpl parameterInfo) {
    if (parameterInfo == null) {
      parameterInfo = new ParameterInfoImpl(-1);
    }
    LightweightGosuEditor defaultValueCode = createDefaultValueEditor(parameterInfo.getDefaultValue());
    LightweightGosuEditor paramTypeCode = createTypeEditor(parameterInfo.getTypeText());
    return new ParameterTableModelItem(parameterInfo, paramTypeCode, defaultValueCode);
  }

  private LightweightGosuEditor createTypeEditor(String typeText) {
    GosuFragmentFileImpl fragmentFile = new GosuFragmentFileImpl(myProject, typeText, genName(), myTypeContext);
    fragmentFile.setAllowedCompletionHandlers(SymbolCompletionHandler.class);
    fragmentFile.setAllowedCompletionTypes(CompletionVoter.Type.TYPE);
    return makeGosuEditor(fragmentFile.getVirtualFile());
  }

  @Nullable
  private static PsiType getRowType(JTable table, int row) {
    return findFragmentType(((GosuParameterTableModel)table.getModel()).getItems().get(row).typeCodeFragment.getPsiFile());
  }

  private static class VariableCompletionTableCellEditor extends StringTableCellEditor {
    public VariableCompletionTableCellEditor(Project project) {
      super(project);
    }

    public Component getTableCellEditorComponent(final JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 final int row,
                                                 int column) {
      final EditorTextField textField = (EditorTextField)super.getTableCellEditorComponent(table, value, isSelected, row, column);
      textField.registerKeyboardAction(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          PsiType type = getRowType(table, row);
          if (type != null) {
            completeVariable(textField, type);
          }
        }
      }, KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
      textField.setBorder(new LineBorder(table.getSelectionBackground()));
      return textField;
    }

    private static void completeVariable(EditorTextField editorTextField, PsiType type) {
      Editor editor = editorTextField.getEditor();
      String prefix = editorTextField.getText();
      if (prefix == null) prefix = "";
      Set<LookupElement> set = new LinkedHashSet<LookupElement>();
      JavaCompletionUtil.completeVariableNameForRefactoring(editorTextField.getProject(), set, prefix, type, VariableKind.PARAMETER);

      LookupElement[] lookupItems = set.toArray(new LookupElement[set.size()]);
      editor.getCaretModel().moveToOffset(prefix.length());
      editor.getSelectionModel().removeSelection();
      LookupManager.getInstance(editorTextField.getProject()).showLookup(editor, lookupItems, prefix);
    }
  }


  public static class JavaTypeColumn extends TypeColumn {
    public JavaTypeColumn(Project project) {
      super(project, GosuLanguage.instance().getAssociatedFileType());
    }

    @Override
    public TableCellEditor doCreateEditor(ParameterTableModelItem o) {
      return new LightEditorCellEditor(myProject, GosuLanguage.instance().getAssociatedFileType());
    }
  }

  public static class JavaNameColumn extends NameColumn {
    private final Project myProject;

    public JavaNameColumn(Project project) {
      super(project);
      myProject = project;
    }


    @Override
    public TableCellEditor doCreateEditor(ParameterTableModelItem o) {
      return new VariableCompletionTableCellEditor(myProject);
    }


    @Override
    public TableCellRenderer doCreateRenderer(ParameterTableModelItem item) {
      return new ColoredTableCellRenderer() {
        public void customizeCellRenderer(JTable table, Object value,
                                          boolean isSelected, boolean hasFocus, int row, int column) {
          if (value == null) return;
          if (isSelected || hasFocus) {
            acquireState(table, true, false, row, column);
            getCellState().updateRenderer(this);
            setPaintFocusBorder(false);
          }
          append((String)value, new SimpleTextAttributes(Font.PLAIN, null));
        }
      };
    }
  }
}
