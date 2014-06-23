/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.plugin.ij.refactor.intoduceField;

import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.codeInsight.completion.JavaCompletionUtil;
import com.intellij.ide.util.ClassFilter;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.event.DocumentAdapter;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.help.HelpManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.LanguageLevelProjectExtension;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Comparing;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiType;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.codeStyle.SuggestedNameInfo;
import com.intellij.psi.codeStyle.VariableKind;
import com.intellij.psi.impl.source.resolve.JavaResolveUtil;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.refactoring.HelpID;
import com.intellij.refactoring.JavaRefactoringSettings;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.introduceParameter.AbstractJavaInplaceIntroducer;
import com.intellij.refactoring.ui.NameSuggestionsField;
import com.intellij.refactoring.ui.NameSuggestionsGenerator;
import com.intellij.refactoring.ui.NameSuggestionsManager;
import com.intellij.refactoring.ui.TypeSelector;
import com.intellij.refactoring.ui.TypeSelectorManager;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import com.intellij.refactoring.util.EnumConstantsUtil;
import com.intellij.refactoring.util.RefactoringMessageUtil;
import com.intellij.ui.RecentsManager;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import com.intellij.ui.StateRestoringCheckBox;
import com.intellij.usageView.UsageViewUtil;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.ui.UIUtil;
import gnu.trove.THashSet;
import gw.plugin.ij.lang.psi.IGosuFile;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifier;
import gw.plugin.ij.refactor.GosuVisibilityPanel;
import org.jetbrains.annotations.NonNls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

class GosuIntroduceConstantDialog extends DialogWrapper {
  private static final Logger LOG = Logger.getInstance("#com.intellij.refactoring.introduceField.IntroduceConstantDialog");
  @NonNls
  private static final String RECENTS_KEY = "IntroduceConstantDialog.RECENTS_KEY";
  @NonNls
  protected static final String NONNLS_SELECTED_PROPERTY = "INTRODUCE_CONSTANT_NONNLS";

  private final Project myProject;
  private final PsiClass myParentClass;
  private final PsiExpression myInitializerExpression;
  private final PsiLocalVariable myLocalVariable;
  private final boolean myInvokedOnDeclaration;
  private final PsiExpression[] myOccurrences;
  private final String myEnteredName;
  private final int myOccurrencesCount;
  private PsiClass myTargetClass;
  private final TypeSelectorManager myTypeSelectorManager;

  private NameSuggestionsField myNameField;
  private JCheckBox myCbReplaceAll;

  private TypeSelector myTypeSelector;
  private StateRestoringCheckBox myCbDeleteVariable;
  private final JavaCodeStyleManager myCodeStyleManager;
  private ReferenceEditorComboWithBrowseButton myTfTargetClassName;
  private GosuBaseExpressionToFieldHandler.TargetDestination myDestinationClass;
  private JPanel myTypePanel;
  private JPanel myTargetClassNamePanel;
  private JPanel myPanel;
  private JLabel myTypeLabel;
  private JPanel myNameSuggestionPanel;
  private JLabel myNameSuggestionLabel;
  private JLabel myTargetClassNameLabel;
  private JCheckBox myCbNonNls;
  private JPanel myVisibilityPanel;
  private final GosuVisibilityPanel myVPanel;
  private final JCheckBox myIntroduceEnumConstantCb = new JCheckBox(RefactoringBundle.message("introduce.constant.enum.cb"), true);

  GosuIntroduceConstantDialog(Project project,
                              PsiClass parentClass,
                              PsiExpression initializerExpression,
                              PsiLocalVariable localVariable,
                              boolean isInvokedOnDeclaration,
                              PsiExpression[] occurrences,
                              PsiClass targetClass,
                              TypeSelectorManager typeSelectorManager, String enteredName) {
    super(project, true);
    myProject = project;
    myParentClass = parentClass;
    myInitializerExpression = initializerExpression;
    myLocalVariable = localVariable;
    myInvokedOnDeclaration = isInvokedOnDeclaration;
    myOccurrences = occurrences;
    myEnteredName = enteredName;
    myOccurrencesCount = occurrences.length;
    myTargetClass = targetClass;
    myTypeSelectorManager = typeSelectorManager;
    myDestinationClass = null;

    setTitle(GosuIntroduceConstantHandler.REFACTORING_NAME);
    myCodeStyleManager = JavaCodeStyleManager.getInstance(myProject);
    myVPanel = new GosuVisibilityPanel(false, true);
    myVisibilityPanel.add(myVPanel, BorderLayout.CENTER);
    init();

    myVPanel.setVisibility(JavaRefactoringSettings.getInstance().INTRODUCE_CONSTANT_VISIBILITY);
    myIntroduceEnumConstantCb.setEnabled(EnumConstantsUtil.isSuitableForEnumConstant(getSelectedType(), myTargetClass));
    updateVisibilityPanel();
    updateButtons();
  }

  public String getEnteredName() {
    return myNameField.getEnteredName();
  }

  private String getTargetClassName() {
    return myTfTargetClassName.getText().trim();
  }

  public GosuBaseExpressionToFieldHandler.TargetDestination getDestinationClass() {
    return myDestinationClass;
  }

  public boolean introduceEnumConstant() {
    return myIntroduceEnumConstantCb.isEnabled() && myIntroduceEnumConstantCb.isSelected();
  }

  public String getFieldVisibility() {
    return myVPanel.getVisibility();
  }

  public boolean isReplaceAllOccurrences() {
    return myOccurrencesCount > 1 && myCbReplaceAll.isSelected();
  }

  public PsiType getSelectedType() {
    return myTypeSelector.getSelectedType();
  }

  protected Action[] createActions() {
    return new Action[]{getOKAction(), getCancelAction(), getHelpAction()};
  }

  protected void doHelpAction() {
    HelpManager.getInstance().invokeHelp(HelpID.INTRODUCE_CONSTANT);
  }

  protected JComponent createNorthPanel() {
    myTypeSelector = myTypeSelectorManager.getTypeSelector();
    myTypePanel.setLayout(new BorderLayout());
    myTypePanel.add(myTypeSelector.getComponent(), BorderLayout.CENTER);
    if (myTypeSelector.getFocusableComponent() != null) {
      myTypeLabel.setLabelFor(myTypeSelector.getFocusableComponent());
    }

    myNameField = new NameSuggestionsField(myProject);
    myNameSuggestionPanel.setLayout(new BorderLayout());
    myNameField.addDataChangedListener(new NameSuggestionsField.DataChanged() {
      public void dataChanged() {
        updateButtons();
      }
    });
    myNameSuggestionPanel.add(myNameField.getComponent(), BorderLayout.CENTER);
    myNameSuggestionLabel.setLabelFor(myNameField.getFocusableComponent());

    Set<String> possibleClassNames = new LinkedHashSet<>();
    for (final PsiExpression occurrence : myOccurrences) {
      final PsiClass parentClass = new GosuIntroduceConstantHandler().getParentClass(occurrence);
      if (parentClass != null && parentClass.getQualifiedName() != null) {
        possibleClassNames.add(parentClass.getQualifiedName());
      }
    }
    myTfTargetClassName =
            new ReferenceEditorComboWithBrowseButton(new ChooseClassAction(), "", myProject, true, RECENTS_KEY);
    myTargetClassNamePanel.setLayout(new BorderLayout());
    myTargetClassNamePanel.add(myTfTargetClassName, BorderLayout.CENTER);
    myTargetClassNameLabel.setLabelFor(myTfTargetClassName);
    for (String possibleClassName : possibleClassNames) {
      myTfTargetClassName.prependItem(possibleClassName);
    }
    myTfTargetClassName.getChildComponent().addDocumentListener(new DocumentAdapter() {
      public void documentChanged(DocumentEvent e) {
        targetClassChanged();
        enableEnumDependant(introduceEnumConstant());
      }
    });
    myIntroduceEnumConstantCb.addActionListener(new ActionListener() {
      public void actionPerformed(final ActionEvent e) {
        enableEnumDependant(introduceEnumConstant());
      }
    });
    final JPanel enumPanel = new JPanel(new BorderLayout());
    enumPanel.add(myIntroduceEnumConstantCb, BorderLayout.EAST);
    myTargetClassNamePanel.add(enumPanel, BorderLayout.SOUTH);

    final String propertyName;
    if (myLocalVariable != null) {
      propertyName = myCodeStyleManager.variableNameToPropertyName(myLocalVariable.getName(), VariableKind.LOCAL_VARIABLE);
    } else {
      propertyName = null;
    }
    final NameSuggestionsManager nameSuggestionsManager =
            new NameSuggestionsManager(myTypeSelector, myNameField, createNameSuggestionGenerator(propertyName, myInitializerExpression,
                    myCodeStyleManager, myEnteredName, myParentClass));

    nameSuggestionsManager.setLabelsFor(myTypeLabel, myNameSuggestionLabel);
    //////////
    if (myOccurrencesCount > 1) {
      myCbReplaceAll.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          updateTypeSelector();

          myNameField.requestFocusInWindow();
        }
      });
      myCbReplaceAll.setText(RefactoringBundle.message("replace.all.occurences", myOccurrencesCount));
    } else {
      myCbReplaceAll.setVisible(false);
    }

    if (myLocalVariable != null) {
      if (myInvokedOnDeclaration) {
        myCbDeleteVariable.setEnabled(false);
        myCbDeleteVariable.setSelected(true);
      } else if (myCbReplaceAll != null) {
        updateCbDeleteVariable();
        myCbReplaceAll.addItemListener(
                new ItemListener() {
                  public void itemStateChanged(ItemEvent e) {
                    updateCbDeleteVariable();
                  }
                });
      }
    } else {
      myCbDeleteVariable.setVisible(false);
    }

    final PsiManager psiManager = PsiManager.getInstance(myProject);
    if ((myTypeSelectorManager.isSuggestedType("java.lang.String") || (myLocalVariable != null && AnnotationUtil.isAnnotated(myLocalVariable, AnnotationUtil.NON_NLS, false))) &&
            LanguageLevelProjectExtension.getInstance(psiManager.getProject()).getLanguageLevel().hasEnumKeywordAndAutoboxing() &&
            JavaPsiFacade.getInstance(psiManager.getProject()).findClass(AnnotationUtil.NON_NLS, myParentClass.getResolveScope()) != null) {
      final PropertiesComponent component = PropertiesComponent.getInstance(myProject);
      myCbNonNls.setSelected(component.isTrueValue(NONNLS_SELECTED_PROPERTY));
      myCbNonNls.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          component.setValue(NONNLS_SELECTED_PROPERTY, Boolean.toString(myCbNonNls.isSelected()));
        }
      });
    } else {
      myCbNonNls.setVisible(false);
    }

    updateTypeSelector();

    enableEnumDependant(introduceEnumConstant());
    return myPanel;
  }

  public void setReplaceAllOccurrences(boolean replaceAllOccurrences) {
    if (myCbReplaceAll != null) {
      myCbReplaceAll.setSelected(replaceAllOccurrences);
    }
  }

  protected static NameSuggestionsGenerator createNameSuggestionGenerator(final String propertyName,
                                                                          final PsiExpression psiExpression,
                                                                          final JavaCodeStyleManager codeStyleManager,
                                                                          final String enteredName, final PsiClass parentClass) {
    return new NameSuggestionsGenerator() {
      public SuggestedNameInfo getSuggestedNameInfo(PsiType type) {
        SuggestedNameInfo nameInfo =
                codeStyleManager.suggestVariableName(VariableKind.STATIC_FINAL_FIELD, propertyName, psiExpression, type);
        if (psiExpression != null) {
          String[] names = nameInfo.names;
          for (int i = 0, namesLength = names.length; i < namesLength; i++) {
            String name = names[i];
            if (parentClass.findFieldByName(name, false) != null) {
              names[i] = codeStyleManager.suggestUniqueVariableName(name, psiExpression, true);
            }
          }
        }
        final String[] strings = AbstractJavaInplaceIntroducer.appendUnresolvedExprName(JavaCompletionUtil
                .completeVariableNameForRefactoring(codeStyleManager, type, VariableKind.LOCAL_VARIABLE, nameInfo), psiExpression);
        return new SuggestedNameInfo.Delegate(enteredName != null ? ArrayUtil.mergeArrays(new String[]{enteredName}, strings) : strings, nameInfo);
      }

    };
  }

  private void updateButtons() {
    setOKActionEnabled(JavaPsiFacade.getInstance(myProject).getNameHelper().isIdentifier(getEnteredName()));
  }

  private void targetClassChanged() {
    final String targetClassName = getTargetClassName();
    myTargetClass = JavaPsiFacade.getInstance(myProject).findClass(targetClassName, GlobalSearchScope.projectScope(myProject));
    updateVisibilityPanel();
    myIntroduceEnumConstantCb.setEnabled(EnumConstantsUtil.isSuitableForEnumConstant(getSelectedType(), myTargetClass));
  }

  private void enableEnumDependant(boolean enable) {
    if (enable) {
      myVPanel.disableAllButPublic();
    } else {
      updateVisibilityPanel();
    }
    myCbNonNls.setEnabled(!enable);
  }

  protected JComponent createCenterPanel() {
    return new JPanel();
  }

  public boolean isDeleteVariable() {
    return myInvokedOnDeclaration || myCbDeleteVariable != null && myCbDeleteVariable.isSelected();
  }

  public boolean isAnnotateAsNonNls() {
    return myCbNonNls != null && myCbNonNls.isSelected();
  }

  private void updateCbDeleteVariable() {
    if (!myCbReplaceAll.isSelected()) {
      myCbDeleteVariable.makeUnselectable(false);
    } else {
      myCbDeleteVariable.makeSelectable();
    }
  }

  private void updateTypeSelector() {
    if (myCbReplaceAll != null) {
      myTypeSelectorManager.setAllOccurrences(myCbReplaceAll.isSelected());
    } else {
      myTypeSelectorManager.setAllOccurrences(false);
    }
  }

  private void updateVisibilityPanel() {
    if (myTargetClass != null && myTargetClass.isInterface()) {
      myVPanel.disableAllButPublic();
    } else {
      UIUtil.setEnabled(myVisibilityPanel, true, true);
      // exclude all modifiers not visible from all occurences
      final Set<String> visible = new THashSet<>();
      visible.add(IGosuModifier.PRIVATE);
      visible.add(IGosuModifier.PROTECTED);
      visible.add(IGosuModifier.INTERNAL);
      visible.add(IGosuModifier.PUBLIC);
      for (PsiExpression occurrence : myOccurrences) {
        final PsiManager psiManager = PsiManager.getInstance(myProject);
        for (Iterator<String> iterator = visible.iterator(); iterator.hasNext(); ) {
          String modifier = iterator.next();

          try {
            final String modifierText = IGosuModifier.INTERNAL.equals(modifier) ? "" : modifier + " ";
            final PsiField field = JavaPsiFacade.getInstance(psiManager.getProject()).getElementFactory().createFieldFromText(modifierText + "int xxx;", myTargetClass);
            if (!JavaResolveUtil.isAccessible(field, myTargetClass, field.getModifierList(), occurrence, myTargetClass, null)) {
              iterator.remove();
            }
          } catch (IncorrectOperationException e) {
            LOG.error(e);
          }
        }
      }
      if (!visible.contains(getFieldVisibility())) {
        if (visible.contains(IGosuModifier.PUBLIC)) {
          myVPanel.setVisibility(IGosuModifier.PUBLIC);
        }
        if (visible.contains(IGosuModifier.INTERNAL)) {
          myVPanel.setVisibility(IGosuModifier.INTERNAL);
        }
        if (visible.contains(IGosuModifier.PROTECTED)) {
          myVPanel.setVisibility(IGosuModifier.PROTECTED);
        }
        if (visible.contains(IGosuModifier.PRIVATE)) {
          myVPanel.setVisibility(IGosuModifier.PRIVATE);
        }
      }
    }
  }

  protected void doOKAction() {
    final String targetClassName = getTargetClassName();
    PsiClass newClass = myParentClass;

    if (!"".equals(targetClassName) && !Comparing.strEqual(targetClassName, myParentClass.getQualifiedName())) {
      newClass = JavaPsiFacade.getInstance(myProject).findClass(targetClassName, GlobalSearchScope.projectScope(myProject));
      if (newClass == null) {
        if (Messages.showOkCancelDialog(myProject, RefactoringBundle.message("class.does.not.exist.in.the.project"), GosuIntroduceConstantHandler.REFACTORING_NAME, Messages.getErrorIcon()) != OK_EXIT_CODE) {
          return;
        }
        myDestinationClass = new GosuBaseExpressionToFieldHandler.TargetDestination(targetClassName, myParentClass);
      } else {
        myDestinationClass = new GosuBaseExpressionToFieldHandler.TargetDestination(newClass);
      }
    }

    String fieldName = getEnteredName();
    String errorString = null;
    if ("".equals(fieldName)) {
      errorString = RefactoringBundle.message("no.field.name.specified");
    } else if (!JavaPsiFacade.getInstance(myProject).getNameHelper().isIdentifier(fieldName)) {
      errorString = RefactoringMessageUtil.getIncorrectIdentifierMessage(fieldName);
    } else if (newClass != null && !myParentClass.getLanguage().equals(newClass.getLanguage())) {
      errorString = RefactoringBundle.message("move.to.different.language", UsageViewUtil.getType(myParentClass),
              myParentClass.getQualifiedName(), newClass.getQualifiedName());
    }
    if (errorString != null) {
      CommonRefactoringUtil.showErrorMessage(
              GosuIntroduceFieldHandler.REFACTORING_NAME,
              errorString,
              HelpID.INTRODUCE_FIELD,
              myProject);
      return;
    }
    if (newClass != null) {
      PsiField oldField = newClass.findFieldByName(fieldName, true);

      if (oldField != null) {
        int answer = Messages.showYesNoDialog(
                myProject,
                RefactoringBundle.message("field.exists", fieldName, oldField.getContainingClass().getQualifiedName()),
                GosuIntroduceFieldHandler.REFACTORING_NAME,
                Messages.getWarningIcon()
        );
        if (answer != 0) {
          return;
        }
      }
    }

    JavaRefactoringSettings.getInstance().INTRODUCE_CONSTANT_VISIBILITY = getFieldVisibility();

    RecentsManager.getInstance(myProject).registerRecentEntry(RECENTS_KEY, targetClassName);
    super.doOKAction();
  }

  public JComponent getPreferredFocusedComponent() {
    return myNameField.getFocusableComponent();
  }

  private class ChooseClassAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      TreeClassChooser chooser = TreeClassChooserFactory.getInstance(myProject).createWithInnerClassesScopeChooser(RefactoringBundle.message("choose.destination.class"), GlobalSearchScope.projectScope(myProject), new ClassFilter() {
        public boolean isAccepted(PsiClass aClass) {
          return aClass.getParent() instanceof PsiJavaFile || aClass.getParent() instanceof IGosuFile || aClass.hasModifierProperty(IGosuModifier.STATIC);
        }
      }, null);
      if (myTargetClass != null) {
        chooser.selectDirectory(myTargetClass.getContainingFile().getContainingDirectory());
      }
      chooser.showDialog();
      PsiClass aClass = chooser.getSelected();
      if (aClass != null) {
        myTfTargetClassName.setText(aClass.getQualifiedName());
      }
    }
  }
}
