/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.introduceVariable;

import com.intellij.openapi.help.HelpManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.intellij.psi.codeStyle.SuggestedNameInfo;
import com.intellij.refactoring.HelpID;
import com.intellij.refactoring.JavaRefactoringSettings;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.introduceVariable.IntroduceVariableBase;
import com.intellij.refactoring.introduceVariable.IntroduceVariableSettings;
import com.intellij.refactoring.ui.NameSuggestionsField;
import com.intellij.refactoring.ui.NameSuggestionsGenerator;
import com.intellij.refactoring.ui.NameSuggestionsManager;
import com.intellij.refactoring.ui.TypeSelector;
import com.intellij.refactoring.ui.TypeSelectorManager;
import com.intellij.ui.NonFocusableCheckBox;
import com.intellij.ui.StateRestoringCheckBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * copy of Intellij IntroduceVariableDialog
 */
class GosuIntroduceVariableDialog extends DialogWrapper implements IntroduceVariableSettings {
  private final Project myProject;
  private final PsiExpression myExpression;
  private final int myOccurrencesCount;
  private final boolean myAnyLValueOccurences;
  private final boolean myDeclareFinalIfAll;
  private final TypeSelectorManager myTypeSelectorManager;
  private final GosuIntroduceVariableHandler.Validator myValidator;

  private NameSuggestionsField myNameField;
  private JCheckBox myCbReplaceAll;
  private StateRestoringCheckBox myCbReplaceWrite = null;
  private JCheckBox myCbFinal;
  private boolean myCbFinalState;
  private TypeSelector myTypeSelector;
  private NameSuggestionsManager myNameSuggestionsManager;
  private static final String REFACTORING_NAME = RefactoringBundle.message("introduce.variable.title");
  private NameSuggestionsField.DataChanged myNameChangedListener;
  private ItemListener myReplaceAllListener;
  private ItemListener myFinalListener;

  public GosuIntroduceVariableDialog(Project project,
                                     PsiExpression expression, int occurrencesCount, boolean anyLValueOccurences,
                                     boolean declareFinalIfAll, TypeSelectorManager typeSelectorManager,
                                     GosuIntroduceVariableHandler.Validator validator) {
    super(project, true);
    myProject = project;
    myExpression = expression;
    myOccurrencesCount = occurrencesCount;
    myAnyLValueOccurences = anyLValueOccurences;
    myDeclareFinalIfAll = declareFinalIfAll;
    myTypeSelectorManager = typeSelectorManager;
    myValidator = validator;

    setTitle(REFACTORING_NAME);
    init();
  }

  protected void dispose() {
    myNameField.removeDataChangedListener(myNameChangedListener);
    if (myCbReplaceAll != null) {
      myCbReplaceAll.removeItemListener(myReplaceAllListener);
    }
    myCbFinal.removeItemListener(myFinalListener);
    super.dispose();
  }

  protected Action[] createActions() {
    return new Action[]{getOKAction(), getCancelAction(), getHelpAction()};
  }

  protected void init() {
    super.init();
    updateOkStatus();
  }

  public String getEnteredName() {
    return myNameField.getEnteredName();
  }

  public boolean isReplaceAllOccurrences() {
    if (myOccurrencesCount <= 1) {
      return false;
    }
    return myCbReplaceAll.isSelected();
  }

  public boolean isDeclareFinal() {
    return myCbFinal.isEnabled() && myCbFinalState;
  }

  public boolean isReplaceLValues() {
    if (myOccurrencesCount <= 1 || !myAnyLValueOccurences || myCbReplaceWrite == null) {
      return true;
    } else {
      return myCbReplaceWrite.isSelected();
    }
  }

  public PsiType getSelectedType() {
    return myTypeSelector.getSelectedType();
  }

  protected JComponent createNorthPanel() {
    myNameField = new NameSuggestionsField(myProject);
    myNameChangedListener = new NameSuggestionsField.DataChanged() {
      public void dataChanged() {
        updateOkStatus();
      }
    };
    myNameField.addDataChangedListener(myNameChangedListener);

    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbConstraints = new GridBagConstraints();

    gbConstraints.insets = new Insets(4, 4, 4, 4);
    gbConstraints.anchor = GridBagConstraints.WEST;
    gbConstraints.fill = GridBagConstraints.BOTH;

    gbConstraints.gridwidth = 1;
    gbConstraints.weightx = 0;
    gbConstraints.weighty = 0;
    gbConstraints.gridx = 0;
    gbConstraints.gridy = 0;
    JLabel type = new JLabel(RefactoringBundle.message("variable.of.type"));
//    panel.add(type, gbConstraints);

    gbConstraints.gridx++;
    myTypeSelector = myTypeSelectorManager.getTypeSelector();
//    panel.add(myTypeSelector.getComponent(), gbConstraints);

    gbConstraints.gridwidth = 1;
    gbConstraints.weightx = 0;
    gbConstraints.weighty = 0;
    gbConstraints.gridx = 0;
    gbConstraints.gridy = 1;
    JLabel namePrompt = new JLabel(RefactoringBundle.message("name.prompt"));
    namePrompt.setLabelFor(myNameField.getComponent());
    panel.add(namePrompt, gbConstraints);

    gbConstraints.gridwidth = 1;
    gbConstraints.weightx = 1;
    gbConstraints.gridx = 1;
    gbConstraints.gridy = 1;
    panel.add(myNameField.getComponent(), gbConstraints);

    myNameSuggestionsManager = new NameSuggestionsManager(myTypeSelector, myNameField,
            new NameSuggestionsGenerator() {
              public SuggestedNameInfo getSuggestedNameInfo(PsiType type) {
                return IntroduceVariableBase.getSuggestedName(type, myExpression);
              }
            });
    myNameSuggestionsManager.setLabelsFor(type, namePrompt);

    return panel;
  }

  protected JComponent createCenterPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints gbConstraints = new GridBagConstraints();
    gbConstraints.fill = GridBagConstraints.HORIZONTAL;
    gbConstraints.weightx = 1;
    gbConstraints.weighty = 0;
    gbConstraints.gridwidth = 1;
    gbConstraints.gridx = 0;
    gbConstraints.gridy = 0;
    gbConstraints.insets = new Insets(0, 0, 0, 0);

    if (myOccurrencesCount > 1) {
      myCbReplaceAll = new NonFocusableCheckBox();
      myCbReplaceAll.setText(RefactoringBundle.message("replace.all.occurences", myOccurrencesCount));

      panel.add(myCbReplaceAll, gbConstraints);
      myReplaceAllListener = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          updateControls();
        }
      };
      myCbReplaceAll.addItemListener(myReplaceAllListener);

      if (myAnyLValueOccurences) {
        myCbReplaceWrite = new StateRestoringCheckBox();
        myCbReplaceWrite.setText(RefactoringBundle.message("replace.write.access.occurrences"));
        gbConstraints.insets = new Insets(0, 8, 0, 0);
        gbConstraints.gridy++;
        panel.add(myCbReplaceWrite, gbConstraints);
        myCbReplaceWrite.addItemListener(myReplaceAllListener);
      }
    }

    myCbFinal = new NonFocusableCheckBox();
    myCbFinal.setText(RefactoringBundle.message("declare.final"));
    final Boolean createFinals = JavaRefactoringSettings.getInstance().INTRODUCE_LOCAL_CREATE_FINALS;
    myCbFinalState = createFinals == null ?
            CodeStyleSettingsManager.getSettings(myProject).GENERATE_FINAL_LOCALS :
            createFinals.booleanValue();

    gbConstraints.insets = new Insets(0, 0, 0, 0);
    gbConstraints.gridy++;
    panel.add(myCbFinal, gbConstraints);
    myFinalListener = new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (myCbFinal.isEnabled()) {
          myCbFinalState = myCbFinal.isSelected();
        }
      }
    };
    myCbFinal.addItemListener(myFinalListener);

    updateControls();

    return panel;
  }

  private void updateControls() {
    if (myCbReplaceWrite != null) {
      if (myCbReplaceAll.isSelected()) {
        myCbReplaceWrite.makeSelectable();
      } else {
        myCbReplaceWrite.makeUnselectable(true);
      }
    }

    if (myCbReplaceAll != null) {
      myTypeSelectorManager.setAllOccurrences(myCbReplaceAll.isSelected());
    } else {
      myTypeSelectorManager.setAllOccurrences(false);
    }

    if (myDeclareFinalIfAll && myCbReplaceAll != null && myCbReplaceAll.isSelected()) {
      myCbFinal.setEnabled(false);
      myCbFinal.setSelected(true);
    } else if (myCbReplaceWrite != null && myCbReplaceWrite.isEnabled() && myCbReplaceWrite.isSelected()) {
      myCbFinal.setEnabled(false);
      myCbFinal.setSelected(false);
    } else {
      myCbFinal.setEnabled(true);
      myCbFinal.setSelected(myCbFinalState);
    }
  }

  protected void doOKAction() {
    if (!myValidator.isOK(this)) {
      return;
    }
    myNameSuggestionsManager.nameSelected();
    myTypeSelectorManager.typeSelected(getSelectedType());
    if (myCbFinal.isEnabled()) {
      JavaRefactoringSettings.getInstance().INTRODUCE_LOCAL_CREATE_FINALS = myCbFinalState;
    }
    super.doOKAction();
  }

  private void updateOkStatus() {
    String text = getEnteredName();
    setOKActionEnabled(JavaPsiFacade.getInstance(myProject).getNameHelper().isIdentifier(text));
  }

  public JComponent getPreferredFocusedComponent() {
    return myNameField.getFocusableComponent();
  }

  protected void doHelpAction() {
    HelpManager.getInstance().invokeHelp(HelpID.INTRODUCE_VARIABLE);
  }
}
