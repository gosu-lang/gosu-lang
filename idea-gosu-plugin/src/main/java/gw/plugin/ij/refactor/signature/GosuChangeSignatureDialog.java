
/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.signature;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.editor.event.DocumentAdapter;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.codeStyle.SuggestedNameInfo;
import com.intellij.psi.codeStyle.VariableKind;
import com.intellij.refactoring.BaseRefactoringProcessor;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.changeSignature.*;
import com.intellij.refactoring.changeSignature.inCallers.JavaCallerChooser;
import com.intellij.refactoring.ui.VisibilityPanelBase;
import com.intellij.refactoring.util.CanonicalTypes;
import com.intellij.refactoring.util.RefactoringMessageUtil;
import com.intellij.ui.DottedBorder;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.TableColumnAnimator;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.table.TableView;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.*;
import com.intellij.util.ui.DialogUtil;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.ui.table.JBTableRow;
import com.intellij.util.ui.table.JBTableRowEditor;
import com.jgoodies.common.base.Strings;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.completion.handlers.SymbolCompletionHandler;
import gw.plugin.ij.editors.LightweightGosuEditor;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuClassParseData;
import gw.plugin.ij.lang.psi.impl.GosuFragmentFileImpl;
import gw.plugin.ij.lang.psi.impl.types.CompletionVoter;
import gw.plugin.ij.refactor.GosuRefactoringUtil;
import gw.plugin.ij.util.GosuBundle;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

import static com.jgoodies.common.base.Strings.isBlank;
import static gw.lang.reflect.TypeSystem.getByFullNameIfValid;
import static gw.plugin.ij.refactor.GosuRefactoringUtil.findFragmentType;
import static gw.plugin.ij.util.ClassLord.purgeClassName;
import static gw.plugin.ij.util.GosuModuleUtil.findModuleForPsiElement;

public class GosuChangeSignatureDialog extends ChangeSignatureDialogBase {



  protected GosuChangeSignatureDialog(Project project, GosuMethodDescriptor descriptor, boolean allowDelegation, PsiElement context, ChangeSignatureHandler initSubstisutor) {
    super(project, descriptor, allowDelegation, context, initSubstisutor);

  }

  public static JavaChangeSignatureDialog createAndPreselectNew(final Project project,
                                                                final PsiMethod method,
                                                                final List<ParameterInfoImpl> parameterInfos,
                                                                final boolean allowDelegation, final PsiReferenceExpression refExpr) {
    return new JavaChangeSignatureDialog(project, method, allowDelegation, refExpr) {
      @Override
      protected int getSelectedIdx() {
        for (int i = 0; i < parameterInfos.size(); i++) {
          ParameterInfoImpl info = parameterInfos.get(i);
          if (info.oldParameterIndex < 0) {
            return i;
          }
        }
        return super.getSelectedIdx();
      }
    };
  }

  @Override
  protected VisibilityPanelBase<String> createVisibilityControl() {
    return new GosuComboBoxVisibilityPanel();
  }

  @Override
  protected JComponent createCenterPanel() {
    final JComponent centerPanel = super.createCenterPanel();
    myPropagateParamChangesButton.setVisible(true);
    return centerPanel;
  }

  @Override
  protected LanguageFileType getFileType() {
    return GosuLanguage.instance().getAssociatedFileType();
  }

  @Override
  protected GosuParameterTableModel createParametersInfoModel(GosuMethodDescriptor descriptor) {
    final PsiParameterList parameterList = descriptor.getMethod().getParameterList();
    return new GosuParameterTableModel(parameterList, myDefaultValueContext, this);
  }

  @Override
  protected boolean isListTableViewSupported() {
    return true;
  }

  @Override
  protected boolean isEmptyRow(ParameterTableModelItem row) {
    if (!StringUtil.isEmpty(row.parameter.getName())) return false;
    if (!StringUtil.isEmpty(row.parameter.getTypeText())) return false;
    return true;
  }

  @Override
  protected JComponent getRowPresentation(ParameterTableModelItem item, boolean selected, final boolean focused) {
    final JPanel panel = new JPanel(new BorderLayout());
    final String typeText = item.typeCodeFragment.getText();
    String name = item.parameter.getName();
    final String separator = StringUtil.repeatSymbol(' ', getNamesMaxLength() - name.length() + 2);
    String text = name + separator + ": " + typeText;
    final String defaultValue = item.defaultValueCodeFragment.getText();
    String tail = "";
    if (StringUtil.isNotEmpty(defaultValue)) {
      tail += " default value = " + defaultValue;
    }
    if (item.parameter.isUseAnySingleVariable()) {
      if (StringUtil.isNotEmpty(defaultValue)) {
        tail += ";";
      }
      tail += " Use any var.";
    }
    if (!StringUtil.isEmpty(tail)) {
      text += " //" + tail;
    }
    final EditorTextField field = new EditorTextField(" " + text, getProject(), getFileType()) {
      @Override
      protected boolean shouldHaveBorder() {
        return false;
      }
    };

    Font font = EditorColorsManager.getInstance().getGlobalScheme().getFont(EditorFontType.PLAIN);
    font = new Font(font.getFontName(), font.getStyle(), 12);
    field.setFont(font);

    if (selected && focused) {
      panel.setBackground(UIUtil.getTableSelectionBackground());
      field.setAsRendererWithSelection(UIUtil.getTableSelectionBackground(), UIUtil.getTableSelectionForeground());
    } else {
      panel.setBackground(UIUtil.getTableBackground());
      if (selected && !focused) {
        panel.setBorder(new DottedBorder(UIUtil.getTableForeground()));
      }
    }
    panel.add(field, BorderLayout.WEST);
    return panel;
  }

  private int getTypesMaxLength() {
    int len = 0;
    for (ParameterTableModelItem item : myParametersTableModel.getItems()) {
      final String text = item.typeCodeFragment == null ? null : item.typeCodeFragment.getText();
      len = Math.max(len, text == null ? 0 : text.length());
    }
    return len;
  }
  
  private int getNamesMaxLength() {
    int len = 0;
    for (ParameterTableModelItem item : myParametersTableModel.getItems()) {
      final String text = item.parameter.getName();
      len = Math.max(len, text == null ? 0 : text.length());
    }
    return len;
  }  
  
  private int getColumnWidth(int index) {
    int letters = getTypesMaxLength() + (index == 0 ? 1 : getNamesMaxLength() + 2);
    Font font = EditorColorsManager.getInstance().getGlobalScheme().getFont(EditorFontType.PLAIN);
    font = new Font(font.getFontName(), font.getStyle(), 12);
    return  letters * Toolkit.getDefaultToolkit().getFontMetrics(font).stringWidth("W");
  }

  private int getTypesColumnWidth() {
    return  getColumnWidth(0);
  }    
  
  private int getNamesColumnWidth() {
    return getColumnWidth(1);
  }

  @Override
  protected JBTableRowEditor getTableEditor(final JTable t, final ParameterTableModelItem item) {
    return new JBTableRowEditor() {
      private EditorTextField myTypeEditor;
      private EditorTextField myNameEditor;
      private EditorTextField myDefaultValueEditor;      
      private JCheckBox myAnyVar;

      class MyDocumentListener extends DocumentAdapter {
        private int myColumn;

        private MyDocumentListener(int column) {
          myColumn = column;
        }

        @Override
        public void documentChanged(DocumentEvent e) {
          fireDocumentChanged(e, myColumn);
        }
      }

      @Override
      public void prepareEditor(JTable table, int row) {
        setLayout(new BorderLayout());
        final JPanel typePanel = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.TOP, 4, 2, true, false));
        final Document document = item.typeCodeFragment.getDocument();
        myTypeEditor = new EditorTextField(document, getProject(), getFileType());
        myTypeEditor.addDocumentListener(mySignatureUpdater);
        final JBLabel typeLabel = new JBLabel("Type:", UIUtil.ComponentStyle.SMALL);
        IJSwingUtilities.adjustComponentsOnMac(typeLabel, myTypeEditor);
        typePanel.add(typeLabel);
        typePanel.add(myTypeEditor);
        myTypeEditor.setPreferredWidth(t.getWidth() / 2);
        myTypeEditor.addDocumentListener(new MyDocumentListener(0));
        add(typePanel, BorderLayout.WEST);

        final JPanel namePanel = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.TOP, 4, 2, true, false));
        myNameEditor = new EditorTextField(item.parameter.getName(), getProject(), getFileType());
        myNameEditor.addDocumentListener(mySignatureUpdater);
        myNameEditor.addDocumentListener(new MyDocumentListener(1));
        final JBLabel nameLabel = new JBLabel(GosuBundle.message("new.dlg.name"), UIUtil.ComponentStyle.SMALL);
        IJSwingUtilities.adjustComponentsOnMac(nameLabel, myNameEditor);
        namePanel.add(nameLabel);
        namePanel.add(myNameEditor);
        add(namePanel, BorderLayout.CENTER);
        new TextFieldCompletionProvider() {

          @Override
          protected void addCompletionVariants(@NotNull String text,
                                               int offset,
                                               @NotNull String prefix,
                                               @NotNull CompletionResultSet result) {
            final PsiType type;
            try {
              type = findFragmentType(item.typeCodeFragment.getPsiFile());
            }
            catch (Exception e) {
              return;
            }

            final SuggestedNameInfo info = JavaCodeStyleManager.getInstance(myProject)
              .suggestVariableName(VariableKind.PARAMETER, null, null, type);

            for (String completionVariant : info.names) {
              final LookupElementBuilder element = LookupElementBuilder.create(completionVariant);
              result.addElement(element.withLookupString(completionVariant.toLowerCase()));
            }
          }
        }.apply(myNameEditor, item.parameter.getName());

        if (item.parameter.getOldIndex() == -1) {
          final JPanel additionalPanel = new JPanel(new BorderLayout());
          final JPanel defaultValuePanel = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.TOP, 4, 2, true, false));
          final Document doc = item.defaultValueCodeFragment.getDocument();
          myDefaultValueEditor = new EditorTextField(doc, getProject(), getFileType());
//          ((PsiExpressionCodeFragment)item.defaultValueCodeFragment).setExpectedType(getRowType(item));
          final JBLabel defaultValueLabel = new JBLabel("Default value:", UIUtil.ComponentStyle.SMALL);
          IJSwingUtilities.adjustComponentsOnMac(defaultValueLabel, myDefaultValueEditor);
          defaultValuePanel.add(defaultValueLabel);
          defaultValuePanel.add(myDefaultValueEditor);
          myDefaultValueEditor.setPreferredWidth(t.getWidth() / 2);
          myDefaultValueEditor.addDocumentListener(new MyDocumentListener(2));
          additionalPanel.add(defaultValuePanel, BorderLayout.WEST);

          if (!isGenerateDelegate()) {
            myAnyVar = new JCheckBox("&Use Any Var");
            UIUtil.applyStyle(UIUtil.ComponentStyle.SMALL, myAnyVar);
            DialogUtil.registerMnemonic(myAnyVar, '&');
            myAnyVar.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                item.parameter.setUseAnySingleVariable(myAnyVar.isSelected());
              }
            });
            final JPanel anyVarPanel = new JPanel(new BorderLayout());
            anyVarPanel.add(myAnyVar, BorderLayout.SOUTH);
            UIUtil.addInsets(anyVarPanel, new Insets(0,0,8,0));
            additionalPanel.add(anyVarPanel, BorderLayout.CENTER);
            //additionalPanel.setPreferredSize(new Dimension(t.getWidth() / 3, -1));
          }
          add(additionalPanel, BorderLayout.SOUTH);
        }
      }

      @Override
      public JBTableRow getValue() {
        return new JBTableRow() {
          @Override
          public Object getValueAt(int column) {
            switch (column) {
              case 0: return item.typeCodeFragment;
              case 1: return myNameEditor.getText().trim();
              case 2: return item.defaultValueCodeFragment;
              case 3: return myAnyVar != null && myAnyVar.isSelected();
            }
            return null;
          }
        };
      }

      @Override
      public JComponent getPreferredFocusedComponent() {
        final MouseEvent me = getMouseEvent();
        if (me == null) {
          return myTypeEditor.getFocusTarget();
        }
        final double x = me.getPoint().getX();
        return x <= getTypesColumnWidth()
               ? myTypeEditor.getFocusTarget()
               : myDefaultValueEditor == null || x <= getNamesColumnWidth()
                 ? myNameEditor.getFocusTarget()
                 : myDefaultValueEditor.getFocusTarget();
      }

      @Override
      public JComponent[] getFocusableComponents() {
        final List<JComponent> focusable = new ArrayList<JComponent>();
        focusable.add(myTypeEditor.getFocusTarget());
        focusable.add(myNameEditor.getFocusTarget());
        if (myDefaultValueEditor != null) {
          focusable.add(myDefaultValueEditor.getFocusTarget());
        }
        if (myAnyVar != null) {
          focusable.add(myAnyVar);
        }
        return focusable.toArray(new JComponent[focusable.size()]);
      }
    };
  }
  
  @Nullable
  private static PsiType getRowType(ParameterTableModelItemBase<ParameterInfoImpl> item) {
    try {
      return ((PsiTypeCodeFragment)item.typeCodeFragment).getType();
    }
    catch (PsiTypeCodeFragment.TypeSyntaxException e) {
      return null;
    }
    catch (PsiTypeCodeFragment.NoTypeException e) {
      return null;
    }
  }    

  @Override
  protected void customizeParametersTable(TableView<ParameterTableModelItem> table) {
    final JTable t = table.getComponent();
    final TableColumn defaultValue = t.getColumnModel().getColumn(2);
    final TableColumn varArg = t.getColumnModel().getColumn(3);
    t.removeColumn(defaultValue);
    t.removeColumn(varArg);
    t.getModel().addTableModelListener(new TableModelListener() {
      @Override
      public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.INSERT) {
          t.getModel().removeTableModelListener(this);
          final TableColumnAnimator animator = new TableColumnAnimator(t);
          animator.setStep(48);
          animator.addColumn(defaultValue, (t.getWidth() - 48) / 3);
          animator.addColumn(varArg, 48);
          animator.startAndDoWhenDone(new Runnable() {
            @Override
            public void run() {
              t.editCellAt(t.getRowCount() - 1, 0);
            }
          });
          animator.start();
        }
      }
    });
  }

  @Override
  protected BaseRefactoringProcessor createRefactoringProcessor() {
    final List<ParameterInfoImpl> parameters = getParameters();
    return new GosuChangeSignatureProcessor(myProject,
                                        myMethod.getMethod(),
                                        isGenerateDelegate(),
                                        getVisibility(),
                                        getMethodName(),
                                        getReturnType(),
                                        parameters.toArray(new ParameterInfoImpl[parameters.size()]),
                                        new ThrownExceptionInfo[0],
                                        (Set) myMethodsToPropagateParameters,
                                        Collections.<PsiMethod>emptySet(),
                                        changeSignatureHandler);
  }

  @Nullable
  protected CanonicalTypes.Type getReturnType() {
    if (myReturnTypeField != null) {
      final PsiType type = findFragmentType(myReturnTypeCodeFragment);
      if (type != null) {
        return CanonicalTypes.createTypeWrapper(type);
      }
    }
    return null;
  }

  @Override
  protected PsiCodeFragment createReturnTypeCodeFragment() {
    final String returnTypeText = StringUtil.notNullize(myMethod.getReturnTypeText());
    GosuFragmentFileImpl frag = new GosuFragmentFileImpl(myProject, returnTypeText, myMethod.getMethod());
    frag.setAllowedCompletionHandlers(SymbolCompletionHandler.class);
    frag.setAllowedCompletionTypes(CompletionVoter.Type.TYPE);
    return frag;
  }

  @Nullable
  @Override
  protected CallerChooserBase<PsiMethod> createCallerChooser(String title, Tree treeToReuse, Consumer<Set<IGosuMethod>> callback) {
    return new JavaCallerChooser(myMethod.getMethod(), myProject, title, treeToReuse, (Consumer) callback);
  }

  @Override
  protected String validateAndCommitData() {
    PsiManager manager = PsiManager.getInstance(myProject);
    PsiElementFactory factory = JavaPsiFacade.getInstance(manager.getProject()).getElementFactory();

    String name = getMethodName();
    if (!JavaPsiFacade.getInstance(manager.getProject()).getNameHelper().isIdentifier(name)) {
      return RefactoringMessageUtil.getIncorrectIdentifierMessage(name);
    }

    if (myMethod.canChangeReturnType() == MethodDescriptor.ReadWriteOption.ReadWrite) {
      if (!isBlank(myReturnTypeCodeFragment.getText())) {
        PsiType returnType = findFragmentType(myReturnTypeCodeFragment);
        IType tsType = getByFullNameIfValid(purgeClassName(returnType.getCanonicalText()),
                findModuleForPsiElement(myMethod.getMethod()));
        if (tsType == null) {
          myReturnTypeField.requestFocus();
          return RefactoringBundle.message("changeSignature.wrong.return.type", myReturnTypeCodeFragment.getText());
        }
      }
    }

    List<ParameterTableModelItem> parameterInfos = myParametersTableModel.getItems();
    final int newParametersNumber = parameterInfos.size();

    for (int i = 0; i < newParametersNumber; i++) {
      final ParameterTableModelItem item = parameterInfos.get(i);

      if (!JavaPsiFacade.getInstance(manager.getProject()).getNameHelper().isIdentifier(item.parameter.getName())) {
        return RefactoringMessageUtil.getIncorrectIdentifierMessage(item.parameter.getName());
      }

      final PsiType type;

      LightweightGosuEditor editor = parameterInfos.get(i).typeCodeFragment;
      Document document = editor.getDocument();


      if (isBlank(document.getText())) {
        return RefactoringBundle.message("changeSignature.no.type.for.parameter", item.parameter.getName());
      } else {
        IType tsType = null;
        type = findFragmentType(editor.getPsiFile());
        if (type != null) {
          tsType = getByFullNameIfValid(purgeClassName(type.getCanonicalText()), findModuleForPsiElement(myMethod.getMethod()));
        }
        if (tsType == null) {
          return RefactoringBundle.message("changeSignature.wrong.type.for.parameter",
                  document.getText(),
                  item.parameter.getName());
        }
      }

      item.parameter.setType(type);

      if (type instanceof PsiEllipsisType && i != newParametersNumber - 1) {
        return RefactoringBundle.message("changeSignature.vararg.not.last");
      }

      if (item.parameter.oldParameterIndex < 0) {
        String def = ApplicationManager.getApplication().runWriteAction(new Computable<String>() {
          @Override
          public String compute() {
            return JavaCodeStyleManager.getInstance(myProject).qualifyClassReferences(item.defaultValueCodeFragment.getPsiFile()).getText();
          }
        });
        item.parameter.setDefaultValue(def);
        def = def.trim();
        if (!(type instanceof PsiEllipsisType)) {
          try {
            if (!StringUtil.isEmpty(def)) {
              factory.createExpressionFromText(def, null);
            }
          }
          catch (IncorrectOperationException e) {
            return e.getMessage();
          }
        }
      }
    }

    // warnings
    if (myMethod.canChangeReturnType() == MethodDescriptor.ReadWriteOption.ReadWrite) {
      if (!resolveType(myReturnTypeCodeFragment)) {
        if (Messages.showOkCancelDialog(myProject, RefactoringBundle
          .message("changeSignature.cannot.resolve.return.type", myReturnTypeCodeFragment.getText()),
                                        RefactoringBundle.message("changeSignature.refactoring.name"), Messages.getWarningIcon()) != 0) {
          return EXIT_SILENTLY;
        }
      }
    }
    for (ParameterTableModelItem item : parameterInfos) {
      if (!resolveType(item.typeCodeFragment.getPsiFile())) {
        if (Messages.showOkCancelDialog(myProject, RefactoringBundle
          .message("changeSignature.cannot.resolve.parameter.type", item.typeCodeFragment.getText(), item.parameter.getName()),
                                        RefactoringBundle.message("changeSignature.refactoring.name"), Messages.getWarningIcon()) !=
            0) {
          return EXIT_SILENTLY;
        }
      }
    }
    return null;
  }

  public boolean resolveType(PsiElement fragment) {
    String typeText = fragment.getText();
    if (Strings.isBlank(typeText)) {
      return fragment == myReturnTypeCodeFragment;
    }
    return GosuRefactoringUtil.isResolvableType(findFragmentType(fragment)) ||  resolveByRelativeName(typeText);
  }

  public boolean resolveByRelativeName(String typeText) {
    IModule module = getModule();
    try {
      TypeSystem.pushModule(module);
      ITypeUsesMap typeUsesMap = getTypeUsesMap();
      if (typeUsesMap == null) {
        TypeSystem.getByRelativeName(typeText);
      } else {
        TypeSystem.getByRelativeName(typeText, typeUsesMap);
      }
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    } finally {
      TypeSystem.popModule(module);
    }
  }

  public ITypeUsesMap getTypeUsesMap() {
    PsiFile containingFile = myMethod.getMethod().getContainingFile();
    if (containingFile instanceof  AbstractGosuClassFileImpl) {
      GosuClassParseData parseData = ((AbstractGosuClassFileImpl) containingFile).getParseData();
      if (parseData != null) {
        IClassFileStatement classFileStatement = parseData.getClassFileStatement();
        if (classFileStatement != null) {
          IGosuClass gosuClass = classFileStatement.getGosuClass();
          if (gosuClass  != null) {
            return gosuClass.getTypeUsesMap();
          }
        }
      }
    }
    return null;
  }

  @Override
  protected ValidationInfo doValidate() {
    if (!getTableComponent().isEditing()) {
      for (final ParameterTableModelItem item : myParametersTableModel.getItems()) {
        if (item.parameter.oldParameterIndex < 0) {
//          if (StringUtil.isEmpty(item.defaultValueCodeFragment.getText()))
            //return new ValidationInfo("Default value is missing. In the method call place new parameter value would be leaved blank");
        }
      }
    }
    return super.doValidate();
  }

  @Override
  protected boolean postponeValidation() {
    return false;
  }

  @Override
  protected String calculateSignature() {
    return doCalculateSignature(myMethod.getMethod());
  }

  protected String doCalculateSignature(PsiMethod method) {
    final StringBuilder buffer = new StringBuilder();
    final PsiModifierList modifierList = method.getModifierList();

    String modifiers = "";
    final String oldModifier = VisibilityUtil.getVisibilityModifier(modifierList);
    final String newModifier = getVisibility();
    String newModifierStr = VisibilityUtil.getVisibilityString(newModifier);
    if (!newModifier.equals(oldModifier)) {
      int index = modifiers.indexOf(oldModifier);
      if (index >= 0) {
        final StringBuilder buf = new StringBuilder(modifiers);
        buf.replace(index,
                    index + oldModifier.length() + ("".equals(newModifierStr) ? 1 : 0),
                    newModifierStr);
        modifiers = buf.toString();
      } else {
        if (!StringUtil.isEmpty(newModifierStr)) {
          newModifierStr += " ";
        }
        modifiers = newModifierStr + modifiers;
      }
    }

    buffer.append(modifiers);
    if (modifiers.length() > 0 &&
        !StringUtil.endsWithChar(modifiers, '\n') &&
        !StringUtil.endsWithChar(modifiers, '\r') &&
        !StringUtil.endsWithChar(modifiers, ' ')) {
      buffer.append(" ");
    }

    buffer.append("function ").append(getMethodName());
    buffer.append("(");
    int paramIndent = buffer.toString().length();

    char[] chars = new char[paramIndent];
    Arrays.fill(chars, ' ');

    String indent = new String(chars);
    List<ParameterTableModelItem> items = myParametersTableModel.getItems();
    int curIndent = indent.length();
    for (int i = 0; i < items.size(); i++) {
      final ParameterTableModelItem item = items.get(i);
      if (i > 0) {
        buffer.append(",");
        buffer.append("\n");
        buffer.append(indent);
      }
      final String text = item.typeCodeFragment.getText();
      final String name = item.parameter.getName();
      buffer.append(name).append(" : ").append(text);
      curIndent = indent.length() + text.length() + 1 + name.length();
    }
    //if (!items.isEmpty()) {
    //  buffer.append("\n");
    //}
    buffer.append(")");
    if (!method.isConstructor()) {

      final CanonicalTypes.Type type = getReturnType();
      if (type != null) {
        buffer.append(": ").append(type.getTypeText());
      }
    }

    return buffer.toString();
  }

  public IModule getModule() {
    return GosuModuleUtil.findModuleForPsiElement(myMethod.getMethod());
  }

  public ChangeSignatureHandler getSubstitutor() {
    return changeSignatureHandler;
  }
}
