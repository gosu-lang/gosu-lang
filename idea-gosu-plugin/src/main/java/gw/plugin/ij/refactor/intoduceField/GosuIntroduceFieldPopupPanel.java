/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.plugin.ij.refactor.intoduceField;

import com.intellij.codeInsight.TestFrameworks;
import com.intellij.ide.ui.ListCellRendererWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.JavaRefactoringSettings;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.introduce.inplace.KeyboardComboSwitcher;
import com.intellij.refactoring.ui.TypeSelectorManager;
import gw.plugin.ij.refactor.GosuRefactoringUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

public class GosuIntroduceFieldPopupPanel extends GosuIntroduceFieldCentralPanel {
  private
  @Nullable
  JComboBox myInitializerCombo;
  private JComboBox myVisibilityCombo;
  private DefaultComboBoxModel myInitialisersPlaceModel;

  public GosuIntroduceFieldPopupPanel(PsiClass parentClass,
                                      PsiExpression initializerExpression,
                                      PsiLocalVariable localVariable,
                                      boolean isCurrentMethodConstructor,
                                      boolean isInvokedOnDeclaration,
                                      boolean willBeDeclaredStatic,
                                      PsiExpression[] occurrences,
                                      boolean allowInitInMethod,
                                      boolean allowInitInMethodIfAll,
                                      TypeSelectorManager typeSelectorManager) {
    super(parentClass, initializerExpression, localVariable, isCurrentMethodConstructor, isInvokedOnDeclaration, willBeDeclaredStatic,
            occurrences, allowInitInMethod, allowInitInMethodIfAll, typeSelectorManager);
  }

  protected void initializeInitializerPlace(PsiExpression initializerExpression,
                                            GosuBaseExpressionToFieldHandler.InitializationPlace ourLastInitializerPlace) {
    if (initializerExpression != null) {
      setEnabledInitializationPlaces(initializerExpression, initializerExpression);
      if (!myAllowInitInMethod) {
        myInitialisersPlaceModel.removeElement(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CURRENT_METHOD);
      }
    } else {
      myInitialisersPlaceModel.removeAllElements();
    }

    final PsiMethod setUpMethod = TestFrameworks.getInstance().findSetUpMethod(myParentClass);
    final boolean setupEnabled = myInitialisersPlaceModel.getIndexOf(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_SETUP_METHOD) > -1;
    if (ourLastInitializerPlace == GosuBaseExpressionToFieldHandler.InitializationPlace.IN_SETUP_METHOD &&
            setupEnabled && (myInitializerExpression != null && PsiTreeUtil.isAncestor(setUpMethod, myInitializerExpression, false) ||
            TestFrameworks.getInstance().isTestClass(myParentClass))) {
      myInitialisersPlaceModel.setSelectedItem(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_SETUP_METHOD);
    } else if (ourLastInitializerPlace == GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CONSTRUCTOR &&
            myInitialisersPlaceModel.getIndexOf(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CONSTRUCTOR) > -1 && myParentClass.getConstructors().length > 0) {
      myInitialisersPlaceModel.setSelectedItem(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CONSTRUCTOR);
    } else if (ourLastInitializerPlace == GosuBaseExpressionToFieldHandler.InitializationPlace.IN_FIELD_DECLARATION &&
            myInitialisersPlaceModel.getIndexOf(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_FIELD_DECLARATION) > -1) {
      myInitialisersPlaceModel.setSelectedItem(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_FIELD_DECLARATION);
    } else {
      selectInCurrentMethod();
    }
  }

  @Override
  protected void initializeControls(PsiExpression initializerExpression,
                                    GosuBaseExpressionToFieldHandler.InitializationPlace ourLastInitializerPlace) {
  }

  @Override
  public boolean isDeclareFinal() {
    return allowFinal();
  }

  private void selectInCurrentMethod() {
    if (myInitialisersPlaceModel.getIndexOf(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CURRENT_METHOD) > -1) {
      myInitialisersPlaceModel.setSelectedItem(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CURRENT_METHOD);
    } else if (myInitialisersPlaceModel.getIndexOf(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_FIELD_DECLARATION) > -1) {
      myInitialisersPlaceModel.setSelectedItem(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_FIELD_DECLARATION);
    } else {
      myInitialisersPlaceModel.setSelectedItem(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CURRENT_METHOD);
    }
  }

  public GosuBaseExpressionToFieldHandler.InitializationPlace getInitializerPlace() {
    if (myInitializerCombo != null) {
      return (GosuBaseExpressionToFieldHandler.InitializationPlace) myInitializerCombo.getSelectedItem();
    }
    return (GosuBaseExpressionToFieldHandler.InitializationPlace) myInitialisersPlaceModel.getElementAt(0);
  }

  public String getFieldVisibility() {
    String visibility = JavaRefactoringSettings.getInstance().INTRODUCE_FIELD_VISIBILITY;
    if (visibility == null) {
      visibility = PsiModifier.PRIVATE;
    }
    return visibility;
  }

  protected JComponent createInitializerPlacePanel(final ItemListener itemListener, final ItemListener finalUpdater) {

    JPanel groupPanel = new JPanel(new GridBagLayout());
    final GridBagConstraints gridBagConstraints =
            new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0);

    myInitialisersPlaceModel = new DefaultComboBoxModel();
    myInitialisersPlaceModel.addElement(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CURRENT_METHOD);
    myInitialisersPlaceModel.addElement(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_FIELD_DECLARATION);
    myInitialisersPlaceModel.addElement(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CONSTRUCTOR);
    if (TestFrameworks.getInstance().isTestClass(myParentClass)) {
      myInitialisersPlaceModel.addElement(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_SETUP_METHOD);
    }
    initializeInitializerPlace(myInitializerExpression, GosuInplaceIntroduceFieldPopup.ourLastInitializerPlace);
    if (myInitialisersPlaceModel.getSize() > 1) {
      final JLabel initLabel = new JLabel(RefactoringBundle.message("initialize.in.border.title") + ":");
      initLabel.setDisplayedMnemonic('i');
      gridBagConstraints.insets.left = 5;
      gridBagConstraints.anchor = GridBagConstraints.WEST;
      groupPanel.add(initLabel, gridBagConstraints);
      JComboBox initializersCombo = new JComboBox(myInitialisersPlaceModel);
      KeyboardComboSwitcher.setupActions(initializersCombo, myParentClass.getProject());
      initLabel.setLabelFor(initializersCombo);
      initializersCombo.setRenderer(new ListCellRendererWrapper<GosuBaseExpressionToFieldHandler.InitializationPlace>(initializersCombo) {
        @Override
        public void customize(JList list,
                              GosuBaseExpressionToFieldHandler.InitializationPlace value,
                              int index,
                              boolean selected,
                              boolean hasFocus) {
          setText(getPresentableText(value));
        }
      });
      initializersCombo.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          itemListener.itemStateChanged(null);
          finalUpdater.itemStateChanged(null);
        }
      });
      gridBagConstraints.gridx = 1;
      gridBagConstraints.insets.top = 0;
      gridBagConstraints.insets.left = 0;
      groupPanel.add(initializersCombo, gridBagConstraints);
      myInitializerCombo = initializersCombo;
    }
    return groupPanel;
  }

  @Nullable
  private static String getPresentableText(GosuBaseExpressionToFieldHandler.InitializationPlace value) {
    if (value == GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CURRENT_METHOD) {
      return "current method";
    } else if (value == GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CONSTRUCTOR) {
      return "constructor";
    } else if (value == GosuBaseExpressionToFieldHandler.InitializationPlace.IN_FIELD_DECLARATION) {
      return "field declaration";
    } else if (value == GosuBaseExpressionToFieldHandler.InitializationPlace.IN_SETUP_METHOD) {
      return "setUp";
    }
    return null;
  }

  protected boolean setEnabledInitializationPlaces(PsiElement initializerPart, PsiElement initializer) {
    if (GosuRefactoringUtil.isPsiReferenceExpression(initializerPart)) {
      PsiReference refExpr = (PsiReference) initializerPart;
//      if (refExpr.getQualifierExpression() == null) {
      PsiElement refElement = refExpr.resolve();
      if (refElement == null ||
              (refElement instanceof PsiLocalVariable || refElement instanceof PsiParameter) &&
                      !PsiTreeUtil.isAncestor(initializer, refElement, true)) {
        myInitialisersPlaceModel.removeElement(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_FIELD_DECLARATION);
        myInitialisersPlaceModel.removeElement(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CONSTRUCTOR);
        myInitialisersPlaceModel.removeElement(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_SETUP_METHOD);
        return false;
//        }
      }
    }
    PsiElement[] children = initializerPart.getChildren();
    for (PsiElement child : children) {
      if (!setEnabledInitializationPlaces(child, initializer)) {
        return false;
      }
    }
    return true;
  }

  public void setInitializeInFieldDeclaration() {
    LOG.assertTrue(myInitializerCombo != null);
    myInitializerCombo.setSelectedItem(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_FIELD_DECLARATION);
  }

  public void setVisibility(String visibility) {
    myVisibilityCombo.setSelectedItem(visibility);
  }

  @Override
  protected void updateCbFinal() {
  }

  @Override
  protected boolean allowFinal() {
    final Object selectedItem = getInitializerPlace();
    boolean allowFinal = selectedItem == GosuBaseExpressionToFieldHandler.InitializationPlace.IN_FIELD_DECLARATION ||
            (selectedItem == GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CONSTRUCTOR && !myWillBeDeclaredStatic);
    if (selectedItem == GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CURRENT_METHOD && myIsCurrentMethodConstructor) {
      final PsiMethod[] constructors = myParentClass.getConstructors();
      allowFinal = constructors.length <= 1;
    }
    return super.allowFinal() && allowFinal;
  }

  @Override
  protected void updateInitializerSelection() {
    if (myAllowInitInMethodIfAll || !isReplaceAllOccurrences()) {
      if (myInitialisersPlaceModel.getIndexOf(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CURRENT_METHOD) == -1) {
        myInitialisersPlaceModel.insertElementAt(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CURRENT_METHOD, 0);
      }
    } else {
      myInitialisersPlaceModel.removeElement(GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CURRENT_METHOD);
    }
  }

  @Override
  protected boolean shouldUpdateTypeSelector() {
    return false;
  }

  @Override
  protected JPanel appendCheckboxes(ItemListener itemListener) {
    final JPanel panel = new JPanel(new GridBagLayout());
    appendOccurrences(itemListener, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0), panel);
    return panel;
  }

  protected JPanel composeWholePanel(JComponent initializerPlacePanel, JPanel checkboxPanel) {
    final JPanel panel = new JPanel(new GridBagLayout());
    final GridBagConstraints constraints =
            new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0);
    panel.add(initializerPlacePanel, constraints);
    constraints.gridy++;
    panel.add(checkboxPanel, constraints);
    return panel;
  }
}
