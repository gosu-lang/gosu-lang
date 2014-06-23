/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.intoduceField;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.codeStyle.SuggestedNameInfo;
import com.intellij.psi.codeStyle.VariableKind;
import com.intellij.psi.util.PsiUtil;
import com.intellij.refactoring.JavaRefactoringSettings;
import com.intellij.refactoring.introduceField.AbstractInplaceIntroduceFieldPopup;
import com.intellij.refactoring.introduceField.IntroduceFieldHandler;
import com.intellij.refactoring.ui.TypeSelectorManagerImpl;
import com.intellij.refactoring.util.occurrences.OccurrenceManager;
import gw.plugin.ij.refactor.GosuRenderFuction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class GosuInplaceIntroduceFieldPopup extends AbstractInplaceIntroduceFieldPopup {

  private final boolean myStatic;

  private final GosuIntroduceFieldPopupPanel myIntroduceFieldPanel;

  static GosuBaseExpressionToFieldHandler.InitializationPlace ourLastInitializerPlace;

  public GosuInplaceIntroduceFieldPopup(PsiLocalVariable localVariable,
                                        PsiClass parentClass,
                                        boolean aStatic,
                                        boolean currentMethodConstructor, PsiExpression[] occurrences,
                                        PsiExpression initializerExpression,
                                        TypeSelectorManagerImpl typeSelectorManager,
                                        Editor editor,
                                        final boolean allowInitInMethod,
                                        boolean allowInitInMethodIfAll, final PsiElement anchorElement,
                                        final PsiElement anchorElementIfAll,
                                        final OccurrenceManager occurrenceManager, Project project) {
    super(project, editor, initializerExpression, localVariable, occurrences, typeSelectorManager,
            IntroduceFieldHandler.REFACTORING_NAME, parentClass, anchorElement, occurrenceManager, anchorElementIfAll);
    myStatic = aStatic;
    myIntroduceFieldPanel =
            new GosuIntroduceFieldPopupPanel(parentClass, initializerExpression, localVariable, currentMethodConstructor, localVariable != null, aStatic,
                    myOccurrences, allowInitInMethod, allowInitInMethodIfAll, typeSelectorManager);

    final GridBagConstraints constraints =
            new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0);
    myWholePanel.add(getPreviewComponent(), constraints);

    final JComponent centerPanel = myIntroduceFieldPanel.createCenterPanel();

    myWholePanel.add(centerPanel, constraints);

    myIntroduceFieldPanel.initializeControls(initializerExpression, ourLastInitializerPlace);
  }

  protected PsiField createFieldToStartTemplateOn(final String[] names,
                                                  final PsiType defaultType) {
    final PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(myProject);
    return ApplicationManager.getApplication().runWriteAction(new Computable<PsiField>() {
      @Override
      public PsiField compute() {
        PsiField field = elementFactory.createField(getInputName() != null ? getInputName() : names[0], defaultType);
        field = (PsiField) myParentClass.add(field);
        if (myExprText != null) {
          updateInitializer(elementFactory, field);
        }
        PsiUtil.setModifierProperty(field, PsiModifier.FINAL, myIntroduceFieldPanel.isDeclareFinal());
        final String visibility = myIntroduceFieldPanel.getFieldVisibility();
        if (visibility != null) {
          PsiUtil.setModifierProperty(field, visibility, true);
        }
        myFieldRangeStart = myEditor.getDocument().createRangeMarker(field.getTextRange());
        return field;
      }
    });
  }

  @Override
  protected String[] suggestNames(PsiType defaultType, String propName) {
    return suggestFieldName(defaultType, (PsiLocalVariable) getLocalVariable(), myExpr, myStatic, myParentClass).names;
  }

  @Override
  protected VariableKind getVariableKind() {
    return VariableKind.FIELD;
  }

  public static SuggestedNameInfo suggestFieldName(@Nullable PsiType defaultType,
                                                   @Nullable final PsiLocalVariable localVariable,
                                                   final PsiExpression initializer,
                                                   final boolean forStatic,
                                                   @NotNull final PsiClass parentClass) {
    return GosuIntroduceFieldDialog.
            createGenerator(forStatic, localVariable, initializer, localVariable != null, null, parentClass).
            getSuggestedNameInfo(defaultType);
  }

  public void setReplaceAllOccurrences(boolean replaceAllOccurrences) {
    myIntroduceFieldPanel.setReplaceAllOccurrences(replaceAllOccurrences);
  }

  @Override
  protected void updateTitle(@Nullable PsiVariable variable, String value) {
    if (variable == null || !variable.hasInitializer()) {
      super.updateTitle(variable, value);
    } else {

      final PsiExpression initializer = variable.getInitializer();
      assert initializer != null;
      String text = variable.getText().replace(variable.getName(), value);
      text = text.replace(initializer.getText(), GosuRenderFuction.render(initializer));
      setPreviewText(text);
      revalidate();
    }
  }

  @Override
  protected void updateTitle(@Nullable PsiVariable variable) {
    if (variable != null) {
      updateTitle(variable, variable.getName());
    }
  }

  public void setVisibility(String visibility) {
    myIntroduceFieldPanel.setVisibility(visibility);
  }


  @Override
  public boolean isReplaceAllOccurrences() {
    return myIntroduceFieldPanel.isReplaceAllOccurrences();
  }

  @Override
  protected void saveSettings(@NotNull PsiVariable psiVariable) {
    super.saveSettings(psiVariable);
    JavaRefactoringSettings.getInstance().INTRODUCE_FIELD_VISIBILITY = myIntroduceFieldPanel.getFieldVisibility();
    myIntroduceFieldPanel.saveFinalState();
  }

  @Override
  protected JComponent getComponent() {
    myIntroduceFieldPanel.addOccurrenceListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        restartInplaceIntroduceTemplate();
      }
    });

    return myWholePanel;
  }

  private void updateInitializer(PsiElementFactory elementFactory, PsiField variable) {
    if (variable != null) {
      if (myIntroduceFieldPanel.getInitializerPlace() == GosuBaseExpressionToFieldHandler.InitializationPlace.IN_FIELD_DECLARATION) {
        variable.setInitializer(elementFactory.createExpressionFromText(myExprText, variable));
      } else {
        variable.setInitializer(null);
      }
    }
  }

  @Override
  protected String getActionName() {
    return "IntroduceField";
  }

  public GosuBaseExpressionToFieldHandler.InitializationPlace getInitializerPlace() {
    return myIntroduceFieldPanel.getInitializerPlace();
  }

  protected void performIntroduce() {
    ourLastInitializerPlace = myIntroduceFieldPanel.getInitializerPlace();
    final GosuBaseExpressionToFieldHandler.Settings settings =
            new GosuBaseExpressionToFieldHandler.Settings(getInputName(),
                    getExpr(),
                    getOccurrences(),
                    myIntroduceFieldPanel.isReplaceAllOccurrences(), myStatic,
                    myIntroduceFieldPanel.isDeclareFinal(),
                    myIntroduceFieldPanel.getInitializerPlace(),
                    myIntroduceFieldPanel.getFieldVisibility(), (PsiLocalVariable) getLocalVariable(),
                    getType(),
                    myIntroduceFieldPanel.isDeleteVariable(),
                    myParentClass, false, false);
    new WriteCommandAction(myProject, getCommandName(), getCommandName()) {
      @Override
      protected void run(Result result) throws Throwable {
        if (getLocalVariable() != null) {
          final GosuLocalToFieldHandler.IntroduceFieldRunnable fieldRunnable =
                  new GosuLocalToFieldHandler.IntroduceFieldRunnable(false, (PsiLocalVariable) getLocalVariable(), myParentClass, settings, myStatic, myOccurrences);
          fieldRunnable.run();
        } else {
          final GosuBaseExpressionToFieldHandler.ConvertToFieldRunnable convertToFieldRunnable =
                  new GosuBaseExpressionToFieldHandler.ConvertToFieldRunnable(myExpr, settings, settings.getForcedType(),
                          myOccurrences, myOccurrenceManager,
                          getAnchorElementIfAll(),
                          getAnchorElement(), myEditor,
                          myParentClass);
          convertToFieldRunnable.run();
        }
      }
    }.execute();
  }
}