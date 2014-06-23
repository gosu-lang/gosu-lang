/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.intoduceField;

import com.intellij.codeInsight.completion.JavaCompletionUtil;
import com.intellij.openapi.help.HelpManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiNameHelper;
import com.intellij.psi.PsiType;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.codeStyle.SuggestedNameInfo;
import com.intellij.psi.codeStyle.VariableKind;
import com.intellij.refactoring.HelpID;
import com.intellij.refactoring.JavaRefactoringSettings;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.introduceField.IntroduceFieldHandler;
import com.intellij.refactoring.introduceParameter.AbstractJavaInplaceIntroducer;
import com.intellij.refactoring.ui.NameSuggestionsField;
import com.intellij.refactoring.ui.NameSuggestionsGenerator;
import com.intellij.refactoring.ui.NameSuggestionsManager;
import com.intellij.refactoring.ui.TypeSelector;
import com.intellij.refactoring.ui.TypeSelectorManager;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import com.intellij.refactoring.util.RefactoringMessageUtil;
import com.intellij.util.ArrayUtil;
import gw.plugin.ij.lang.psi.impl.expressions.GosuBlockExpressionImpl;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class GosuIntroduceFieldDialog extends DialogWrapper {


  public static GosuBaseExpressionToFieldHandler.InitializationPlace ourLastInitializerPlace;

  private final Project myProject;
  private final PsiClass myParentClass;
  private final PsiExpression myInitializerExpression;
  private final String myEnteredName;
  private final PsiLocalVariable myLocalVariable;
  private final boolean myIsInvokedOnDeclaration;
  private final boolean myWillBeDeclaredStatic;
  private final TypeSelectorManager myTypeSelectorManager;

  private NameSuggestionsField myNameField;

  private final GosuIntroduceFieldDialogPanel myCentralPanel;

  private TypeSelector myTypeSelector;
  private NameSuggestionsManager myNameSuggestionsManager;
  private static final String REFACTORING_NAME = RefactoringBundle.message("introduce.field.title");

  public GosuIntroduceFieldDialog(Project project,
                                  PsiClass parentClass,
                                  PsiExpression initializerExpression,
                                  PsiLocalVariable localVariable,
                                  boolean isCurrentMethodConstructor, boolean isInvokedOnDeclaration, boolean willBeDeclaredStatic,
                                  PsiExpression[] occurrences, boolean allowInitInMethod, boolean allowInitInMethodIfAll,
                                  TypeSelectorManager typeSelectorManager, String enteredName) {
    super(project, true);
    myProject = project;
    myParentClass = parentClass;
    myInitializerExpression = initializerExpression;
    myEnteredName = enteredName;
    myCentralPanel =
            new GosuIntroduceFieldDialogPanel(parentClass, initializerExpression, localVariable, isCurrentMethodConstructor, isInvokedOnDeclaration,
                    willBeDeclaredStatic, occurrences, allowInitInMethod, allowInitInMethodIfAll,
                    typeSelectorManager);
    myLocalVariable = localVariable;
    myIsInvokedOnDeclaration = isInvokedOnDeclaration;
    myWillBeDeclaredStatic = willBeDeclaredStatic;

    myTypeSelectorManager = typeSelectorManager;

    setTitle(REFACTORING_NAME);
    init();

    myCentralPanel.initializeControls(initializerExpression, ourLastInitializerPlace);
    updateButtons();
  }

  public void setReplaceAllOccurrences(boolean replaceAll) {
    myCentralPanel.setReplaceAllOccurrences(replaceAll);
  }

  public String getEnteredName() {
    return myNameField.getEnteredName();
  }

  public GosuBaseExpressionToFieldHandler.InitializationPlace getInitializerPlace() {

    return myCentralPanel.getInitializerPlace();
  }

  @PsiModifier.ModifierConstant
  public String getFieldVisibility() {
    return myCentralPanel.getFieldVisibility();
  }

  public boolean isReplaceAllOccurrences() {
    return myCentralPanel.isReplaceAllOccurrences();

  }

  public boolean isDeleteVariable() {
    return myCentralPanel.isDeleteVariable();

  }

  public boolean isDeclareFinal() {
    return myCentralPanel.isDeclareFinal();
  }

  public PsiType getFieldType() {
    return myTypeSelector.getSelectedType();
  }


  protected Action[] createActions() {
    return new Action[]{getOKAction(), getCancelAction(), getHelpAction()};
  }


  protected JComponent createNorthPanel() {

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbConstraints = new GridBagConstraints();

    gbConstraints.insets = new Insets(4, 4, 4, 0);
    gbConstraints.anchor = GridBagConstraints.EAST;
    gbConstraints.fill = GridBagConstraints.BOTH;

    gbConstraints.gridwidth = 1;
    gbConstraints.weightx = 0;
    gbConstraints.weighty = 1;
    gbConstraints.gridx = 0;
    gbConstraints.gridy = 0;

    JLabel type = new JLabel(getTypeLabel());

    panel.add(type, gbConstraints);

    gbConstraints.gridx++;
    gbConstraints.insets = new Insets(4, 0, 4, 4);
    gbConstraints.weightx = 0;
    myTypeSelector = myTypeSelectorManager.getTypeSelector();
    panel.add(myTypeSelector.getComponent(), gbConstraints);

    gbConstraints.insets = new Insets(4, 4, 4, 0);
    gbConstraints.gridwidth = 1;
    gbConstraints.weightx = 0;
    gbConstraints.weighty = 1;
    gbConstraints.gridx = 0;
    gbConstraints.gridy = 1;
    JLabel namePrompt = new JLabel(RefactoringBundle.message("name.prompt"));
    panel.add(namePrompt, gbConstraints);

    gbConstraints.insets = new Insets(4, 0, 4, 4);
    gbConstraints.gridwidth = 1;
    gbConstraints.weightx = 1;
    gbConstraints.gridx = 1;
    gbConstraints.gridy = 1;
    myNameField = new NameSuggestionsField(myProject);
    panel.add(myNameField.getComponent(), gbConstraints);
    myNameField.addDataChangedListener(new NameSuggestionsField.DataChanged() {
      public void dataChanged() {
        updateButtons();
      }
    });
    namePrompt.setLabelFor(myNameField.getFocusableComponent());

    myNameSuggestionsManager = new NameSuggestionsManager(myTypeSelector, myNameField,
            createGenerator(myWillBeDeclaredStatic, myLocalVariable, myInitializerExpression, myIsInvokedOnDeclaration, myEnteredName,
                    myParentClass));
    myNameSuggestionsManager.setLabelsFor(type, namePrompt);

    return panel;
  }

  private void updateButtons() {
    setOKActionEnabled(JavaPsiFacade.getInstance(myProject).getNameHelper().isIdentifier(getEnteredName()));
  }

  private String getTypeLabel() {
    return myWillBeDeclaredStatic ?
            RefactoringBundle.message("introduce.field.static.field.of.type") :
            RefactoringBundle.message("introduce.field.field.of.type");
  }

  protected JComponent createCenterPanel() {
    return myCentralPanel.createCenterPanel();
  }

  static NameSuggestionsGenerator createGenerator(final boolean willBeDeclaredStatic,
                                                  final PsiLocalVariable localVariable,
                                                  final PsiExpression initializerExpression,
                                                  final boolean isInvokedOnDeclaration, @Nullable final String enteredName, final PsiClass parentClass) {
    return new NameSuggestionsGenerator() {
      private final JavaCodeStyleManager myCodeStyleManager = JavaCodeStyleManager.getInstance(localVariable != null ? localVariable.getProject()
              : initializerExpression.getProject());

      public SuggestedNameInfo getSuggestedNameInfo(PsiType type) {
        VariableKind variableKind = willBeDeclaredStatic ? VariableKind.STATIC_FIELD : VariableKind.FIELD;

        String propertyName = null;
        if (isInvokedOnDeclaration) {
          propertyName = myCodeStyleManager.variableNameToPropertyName(localVariable.getName(), VariableKind.LOCAL_VARIABLE);
        }

        SuggestedNameInfo nameInfo = null;
        if (initializerExpression instanceof GosuBlockExpressionImpl) {
          nameInfo = new SuggestedNameInfo(new String[]{"block" + PsiNameHelper.getShortClassName(type.getCanonicalText())}) {
            @Override
            public void nameChoosen(String name) {
              super.nameChoosen(name);
            }
          };
        } else {
          nameInfo = myCodeStyleManager.suggestVariableName(variableKind, propertyName, initializerExpression, type);
        }

        if (initializerExpression != null) {
          String[] names = nameInfo.names;
          for (int i = 0, namesLength = names.length; i < namesLength; i++) {
            String name = names[i];
            if (parentClass.findFieldByName(name, false) != null) {
              names[i] = myCodeStyleManager.suggestUniqueVariableName(name, initializerExpression, true);
            }
          }
        }
        String[] strings = AbstractJavaInplaceIntroducer.appendUnresolvedExprName(JavaCompletionUtil.completeVariableNameForRefactoring(myCodeStyleManager, type, VariableKind.LOCAL_VARIABLE, nameInfo), initializerExpression);
        return new SuggestedNameInfo.Delegate(enteredName != null ? ArrayUtil.mergeArrays(new String[]{enteredName}, strings) : strings, nameInfo);
      }
    };
  }


  protected void doOKAction() {
    String fieldName = getEnteredName();
    String errorString = null;
    if ("".equals(fieldName)) {
      errorString = RefactoringBundle.message("no.field.name.specified");
    } else if (!JavaPsiFacade.getInstance(myProject).getNameHelper().isIdentifier(fieldName)) {
      errorString = RefactoringMessageUtil.getIncorrectIdentifierMessage(fieldName);
    }
    if (errorString != null) {
      CommonRefactoringUtil.showErrorMessage(
              IntroduceFieldHandler.REFACTORING_NAME,
              errorString,
              HelpID.INTRODUCE_FIELD,
              myProject
      );
      return;
    }

    PsiField oldField = myParentClass.findFieldByName(fieldName, true);

    if (oldField != null) {
      int answer = Messages.showYesNoDialog(
              myProject,
              RefactoringBundle.message("field.exists", fieldName,
                      oldField.getContainingClass().getQualifiedName()),
              IntroduceFieldHandler.REFACTORING_NAME,
              Messages.getWarningIcon()
      );
      if (answer != 0) {
        return;
      }
    }

    myCentralPanel.saveFinalState();
    ourLastInitializerPlace = myCentralPanel.getInitializerPlace();
    JavaRefactoringSettings.getInstance().INTRODUCE_FIELD_VISIBILITY = getFieldVisibility();

    myNameSuggestionsManager.nameSelected();
    myTypeSelectorManager.typeSelected(getFieldType());
    super.doOKAction();
  }

  public JComponent getPreferredFocusedComponent() {
    return myNameField.getFocusableComponent();
  }

  protected void doHelpAction() {
    HelpManager.getInstance().invokeHelp(HelpID.INTRODUCE_FIELD);
  }
}
