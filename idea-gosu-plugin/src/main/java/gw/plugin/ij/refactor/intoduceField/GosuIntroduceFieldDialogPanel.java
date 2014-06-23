/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.intoduceField;

import com.intellij.codeInsight.TestFrameworks;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.JavaRefactoringSettings;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.ui.TypeSelectorManager;
import com.intellij.ui.IdeBorderFactory;
import gw.plugin.ij.refactor.GosuRefactoringUtil;
import gw.plugin.ij.refactor.GosuVisibilityPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;

public class GosuIntroduceFieldDialogPanel extends GosuIntroduceFieldCentralPanel {
  private JRadioButton myRbInConstructor;
  private JRadioButton myRbInCurrentMethod;
  private JRadioButton myRbInFieldDeclaration;
  private JRadioButton myRbInSetUp;
  private GosuVisibilityPanel myVisibilityPanel;

  public GosuIntroduceFieldDialogPanel(PsiClass parentClass,
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

  protected void initializeControls(PsiExpression initializerExpression, GosuBaseExpressionToFieldHandler.InitializationPlace ourLastInitializerPlace) {
    initializeInitializerPlace(initializerExpression, ourLastInitializerPlace);
    String ourLastVisibility = JavaRefactoringSettings.getInstance().INTRODUCE_FIELD_VISIBILITY;
    myVisibilityPanel.setVisibility(ourLastVisibility);
    super.initializeControls(initializerExpression, ourLastInitializerPlace);
  }

  protected void initializeInitializerPlace(PsiExpression initializerExpression,
                                            GosuBaseExpressionToFieldHandler.InitializationPlace ourLastInitializerPlace) {
    if (initializerExpression != null) {
      setEnabledInitializationPlaces(initializerExpression, initializerExpression);
      if (!myAllowInitInMethod) {
        myRbInCurrentMethod.setEnabled(false);
      }
    } else {
      myRbInConstructor.setEnabled(false);
      myRbInCurrentMethod.setEnabled(false);
      myRbInFieldDeclaration.setEnabled(false);
      if (myRbInSetUp != null) {
        myRbInSetUp.setEnabled(false);
      }
    }

    final PsiMethod setUpMethod = TestFrameworks.getInstance().findSetUpMethod(myParentClass);
    if (myInitializerExpression != null && PsiTreeUtil.isAncestor(setUpMethod, myInitializerExpression, false) && myRbInSetUp.isEnabled() ||
            ourLastInitializerPlace == GosuBaseExpressionToFieldHandler.InitializationPlace.IN_SETUP_METHOD && TestFrameworks.getInstance().isTestClass(myParentClass) && myRbInSetUp.isEnabled()) {
      myRbInSetUp.setSelected(true);
    } else if (ourLastInitializerPlace == GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CONSTRUCTOR) {
      if (myRbInConstructor.isEnabled()) {
        myRbInConstructor.setSelected(true);
      } else {
        selectInCurrentMethod();
      }
    } else if (ourLastInitializerPlace == GosuBaseExpressionToFieldHandler.InitializationPlace.IN_FIELD_DECLARATION) {
      if (myRbInFieldDeclaration.isEnabled()) {
        myRbInFieldDeclaration.setSelected(true);
      } else {
        selectInCurrentMethod();
      }
    } else {
      selectInCurrentMethod();
    }
  }

  private void selectInCurrentMethod() {
    if (myRbInCurrentMethod.isEnabled()) {
      myRbInCurrentMethod.setSelected(true);
    } else if (myRbInFieldDeclaration.isEnabled()) {
      myRbInFieldDeclaration.setSelected(true);
    } else {
      myRbInCurrentMethod.setSelected(true);
    }
  }

  public GosuBaseExpressionToFieldHandler.InitializationPlace getInitializerPlace() {
    if (myRbInConstructor.isSelected()) {
      return GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CONSTRUCTOR;
    }
    if (myRbInCurrentMethod.isSelected()) {
      return GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CURRENT_METHOD;
    }
    if (myRbInFieldDeclaration.isSelected()) {
      return GosuBaseExpressionToFieldHandler.InitializationPlace.IN_FIELD_DECLARATION;
    }
    if (myRbInSetUp != null && myRbInSetUp.isSelected()) {
      return GosuBaseExpressionToFieldHandler.InitializationPlace.IN_SETUP_METHOD;
    }

    LOG.assertTrue(false);
    return GosuBaseExpressionToFieldHandler.InitializationPlace.IN_FIELD_DECLARATION;
  }

  public String getFieldVisibility() {
    return myVisibilityPanel.getVisibility();
  }

  protected JComponent createInitializerPlacePanel(ItemListener itemListener, ItemListener finalUpdater) {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());

    JPanel initializationPanel = new JPanel();
    initializationPanel.setBorder(IdeBorderFactory.createTitledBorder(RefactoringBundle.message("initialize.in.border.title"),
            true));
    initializationPanel.setLayout(new BoxLayout(initializationPanel, BoxLayout.Y_AXIS));


    myRbInCurrentMethod = new JRadioButton();
    myRbInCurrentMethod.setFocusable(false);
    myRbInCurrentMethod.setText(RefactoringBundle.message("current.method.radio"));
    myRbInCurrentMethod.setEnabled(myAllowInitInMethod);

    myRbInFieldDeclaration = new JRadioButton();
    myRbInFieldDeclaration.setFocusable(false);
    myRbInFieldDeclaration.setText(RefactoringBundle.message("field.declaration.radio"));

    myRbInConstructor = new JRadioButton();
    myRbInConstructor.setFocusable(false);
    myRbInConstructor.setText(RefactoringBundle.message("class.constructors.radio"));


    initializationPanel.add(myRbInCurrentMethod);
    initializationPanel.add(myRbInFieldDeclaration);
    initializationPanel.add(myRbInConstructor);

    if (TestFrameworks.getInstance().isTestClass(myParentClass)) {
      myRbInSetUp = new JRadioButton();
      myRbInSetUp.setFocusable(false);
      myRbInSetUp.setText(RefactoringBundle.message("setup.method.radio"));
      initializationPanel.add(myRbInSetUp);
    }

    ButtonGroup bg = new ButtonGroup();
    bg.add(myRbInCurrentMethod);
    bg.add(myRbInFieldDeclaration);
    bg.add(myRbInConstructor);
    if (myRbInSetUp != null) {
      bg.add(myRbInSetUp);
    }

    myRbInConstructor.addItemListener(itemListener);
    myRbInCurrentMethod.addItemListener(itemListener);
    myRbInFieldDeclaration.addItemListener(itemListener);
    myRbInConstructor.addItemListener(finalUpdater);
    myRbInCurrentMethod.addItemListener(finalUpdater);
    myRbInFieldDeclaration.addItemListener(finalUpdater);
    if (myRbInSetUp != null) {
      myRbInSetUp.addItemListener(finalUpdater);
    }

//    modifiersPanel.add(myCbFinal);
//    modifiersPanel.add(myCbStatic);

    JPanel groupPanel = new JPanel(new GridLayout(1, 2));
    groupPanel.add(initializationPanel);

    myVisibilityPanel = new GosuVisibilityPanel(false, false);
    groupPanel.add(myVisibilityPanel);

    mainPanel.add(groupPanel, BorderLayout.CENTER);

    return mainPanel;
  }

  protected boolean setEnabledInitializationPlaces(PsiElement initializerPart, PsiElement initializer) {
    if (GosuRefactoringUtil.isPsiReferenceExpression(initializerPart)) {
      PsiReference refExpr = (PsiReference) initializerPart;
//      if (refExpr.getQualifierExpression() == null) {
      //todo change this
      PsiElement refElement = refExpr.resolve();
      if (refElement == null ||
              (refElement instanceof PsiLocalVariable || refElement instanceof PsiParameter) &&
                      !PsiTreeUtil.isAncestor(initializer, refElement, true)) {
        myRbInFieldDeclaration.setEnabled(false);
        myRbInConstructor.setEnabled(false);
        if (myRbInSetUp != null) {
          myRbInSetUp.setEnabled(false);
        }
        enableFinal(false);
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
    myRbInFieldDeclaration.setSelected(true);
  }

  public void setVisibility(String visibility) {
    myVisibilityPanel.setVisibility(visibility);
  }

  @Override
  protected boolean allowFinal() {
    boolean allowFinal = myRbInFieldDeclaration.isSelected() || (myRbInConstructor.isSelected() && !myWillBeDeclaredStatic);
    if (myRbInCurrentMethod.isSelected() && myIsCurrentMethodConstructor) {
      final PsiMethod[] constructors = myParentClass.getConstructors();
      allowFinal = constructors.length <= 1;
    }
    return super.allowFinal() && allowFinal;
  }

  @Override
  protected void updateInitializerSelection() {
    myRbInCurrentMethod.setEnabled(myAllowInitInMethodIfAll || !isReplaceAllOccurrences());
    if (!myRbInCurrentMethod.isEnabled() && myRbInCurrentMethod.isSelected()) {
      myRbInCurrentMethod.setSelected(false);
      myRbInFieldDeclaration.setSelected(true);
    }
  }

  protected JPanel composeWholePanel(JComponent initializerPlacePanel, JPanel checkboxPanel) {
    JPanel panel = new JPanel(new GridBagLayout());
    final GridBagConstraints constraints =
            new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0);
    panel.add(initializerPlacePanel, constraints);
    panel.add(checkboxPanel, constraints);
    return panel;
  }

}
