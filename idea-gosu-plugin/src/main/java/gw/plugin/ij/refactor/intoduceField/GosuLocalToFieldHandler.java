/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.intoduceField;

import com.intellij.codeInsight.TestFrameworks;
import com.intellij.codeInsight.navigation.NavigationUtil;
import com.intellij.ide.util.PsiClassListCellRenderer;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.JspPsiUtil;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnonymousClass;
import com.intellij.psi.PsiAssignmentExpression;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassInitializer;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiExpressionStatement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiStatement;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.GeneratedMarkerVisitor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.HelpID;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import com.intellij.refactoring.util.EnumConstantsUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.VisibilityUtil;
import gw.plugin.ij.lang.psi.impl.statements.GosuFieldImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.refactor.GosuCodeInsightUtil;
import gw.plugin.ij.refactor.GosuRefactoringUtil;
import gw.plugin.ij.util.ClassLord;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static gw.plugin.ij.refactor.intoduceField.GosuBaseExpressionToFieldHandler.InitializationPlace.IN_CONSTRUCTOR;
import static gw.plugin.ij.refactor.intoduceField.GosuBaseExpressionToFieldHandler.InitializationPlace.IN_FIELD_DECLARATION;


public abstract class GosuLocalToFieldHandler {
  private static final Logger LOG = Logger.getInstance("#com.intellij.refactoring.introduceField.LocalToFieldHandler");

  private static final String REFACTORING_NAME = RefactoringBundle.message("convert.local.to.field.title");
  private final Project myProject;
  private final boolean myIsConstant;
  private int fff;

  public GosuLocalToFieldHandler(Project project, boolean isConstant) {
    myProject = project;
    myIsConstant = isConstant;
  }

  protected abstract GosuBaseExpressionToFieldHandler.Settings showRefactoringDialog(PsiClass aClass, PsiLocalVariable local, PsiExpression[] occurences, boolean isStatic);

  public boolean convertLocalToField(final PsiLocalVariable local, final Editor editor) {
    boolean tempIsStatic = myIsConstant;
    PsiElement parent = local.getParent();
    List<PsiClass> classes = new ArrayList<>();

    if ("gr".equalsIgnoreCase(local.getContainingFile().getVirtualFile().getExtension())) {
      //this is special ij GW studio check for rule files
      CommonRefactoringUtil.showErrorHint(myProject, editor, "Introduce field is not supported for rule files", REFACTORING_NAME, HelpID.LOCAL_TO_FIELD);
      return false;
    }

    while (parent != null && parent.getContainingFile() != null) {
      if (parent instanceof PsiClass && !(myIsConstant && parent instanceof PsiAnonymousClass)) {
        classes.add((PsiClass) parent);
      }
      if (parent instanceof PsiFile && JspPsiUtil.isInJspFile(parent)) {
        String message = RefactoringBundle.message("error.not.supported.for.jsp", REFACTORING_NAME);
        CommonRefactoringUtil.showErrorHint(myProject, editor, message, REFACTORING_NAME, HelpID.LOCAL_TO_FIELD);
        return false;
      }
      if (parent instanceof PsiModifierListOwner && ((PsiModifierListOwner) parent).hasModifierProperty(PsiModifier.STATIC)) {
        tempIsStatic = true;
      }
      parent = parent.getParent();
    }

    if (classes.isEmpty()) {
      return false;
    }
    if (classes.size() == 1 || ApplicationManager.getApplication().isUnitTestMode()) {
      if (convertLocalToField(local, classes.get(getChosenClassIndex(classes)), editor, tempIsStatic)) {
        return false;
      }
    } else {
      final boolean isStatic = tempIsStatic;
      NavigationUtil.getPsiElementPopup(classes.toArray(new PsiClass[classes.size()]), new PsiClassListCellRenderer(), "Choose class to introduce " + (myIsConstant ? "constant" : "field"), new PsiElementProcessor<PsiClass>() {
        @Override
        public boolean execute(@NotNull PsiClass aClass) {
          convertLocalToField(local, aClass, editor, isStatic);
          return false;
        }
      }).showInBestPositionFor(editor);
    }

    return true;
  }

  protected int getChosenClassIndex(List<PsiClass> classes) {
    return classes.size() - 1;
  }

  private boolean convertLocalToField(PsiLocalVariable local, PsiClass aClass, Editor editor, boolean isStatic) {
    final PsiExpression[] occurences = GosuCodeInsightUtil.findReferenceExpressions(GosuRefactoringUtil.getVariableScope(local), local);
    if (editor != null) {
      GosuRefactoringUtil.highlightAllOccurrences(myProject, occurences, editor);
    }

    final GosuBaseExpressionToFieldHandler.Settings settings = showRefactoringDialog(aClass, local, occurences, isStatic);
    if (settings == null) {
      return true;
    }
    //LocalToFieldDialog dialog = new LocalToFieldDialog(project, aClass, local, isStatic);
    final PsiClass destinationClass = settings.getDestinationClass();
    boolean rebindNeeded = false;
    if (destinationClass != null) {
      aClass = destinationClass;
      rebindNeeded = true;
    }

    final PsiClass aaClass = aClass;
    final boolean rebindNeeded1 = rebindNeeded;
    final Runnable runnable =
            new IntroduceFieldRunnable(rebindNeeded1, local, aaClass, settings, isStatic, occurences);
    CommandProcessor.getInstance().executeCommand(myProject, new Runnable() {
      public void run() {
        ApplicationManager.getApplication().runWriteAction(runnable);
      }
    }, REFACTORING_NAME, null);
    return false;
  }

  private static PsiField createField(PsiLocalVariable local, PsiType forcedType, String fieldName, boolean includeInitializer) {
    @NonNls StringBuilder pattern = new StringBuilder();
    pattern.append("var ");
    pattern.append(fieldName);

    PsiType type = local.getType();
    if (local.getInitializer() == null) {
      includeInitializer = false;
    } else {
      type = local.getInitializer().getType();
    }

    pattern.append(": ").append(type.getPresentableText());
    if (includeInitializer) {
      pattern.append(" = ").append(local.getInitializer().getText());
    }

    final PsiElement psiElement = GosuPsiParseUtil.parseProgramm(pattern.toString(), PsiManager.getInstance(local.getProject()), null);

    final GosuFieldImpl field = PsiTreeUtil.findChildOfType(psiElement, GosuFieldImpl.class);

    GeneratedMarkerVisitor.markGenerated(field);

    try {
      final PsiModifierList modifierList = local.getModifierList();
      if (modifierList != null) {
        for (PsiAnnotation annotation : modifierList.getAnnotations()) {
          field.getModifierList().add(annotation.copy());
        }
      }
      return field;
    } catch (IncorrectOperationException e) {
      LOG.error(e);
      return null;
    }
  }

  private static PsiStatement createAssignment(PsiLocalVariable local, String fieldname, PsiElementFactory factory) {
    try {
      PsiAssignmentExpression assignmentExpression = GosuPsiParseUtil.createAssignmentStatement(local.getProject(), fieldname, local.getInitializer());
      return (PsiStatement) CodeStyleManager.getInstance(local.getProject()).reformat(assignmentExpression);
    } catch (IncorrectOperationException e) {
      LOG.error(e);
      return null;
    }
  }

  private static PsiStatement addInitializationToSetUp(final PsiLocalVariable local, final PsiField field, final PsiElementFactory factory)
          throws IncorrectOperationException {
    PsiMethod inClass = TestFrameworks.getInstance().findOrCreateSetUpMethod(field.getContainingClass());
    assert inClass != null;
    PsiStatement assignment = createAssignment(local, field.getName(), factory);
    final PsiCodeBlock body = inClass.getBody();
    assert body != null;
    if (PsiTreeUtil.isAncestor(body, local, false)) {
      assignment = (PsiStatement) body.addBefore(assignment, PsiTreeUtil.getParentOfType(local, PsiStatement.class));
    } else {
      assignment = (PsiStatement) body.add(assignment);
    }
    local.delete();
    return assignment;
  }

  private static PsiStatement addInitializationToConstructors(PsiLocalVariable local, PsiField field, PsiMethod enclosingConstructor,
                                                              PsiElementFactory factory) throws IncorrectOperationException {
    PsiClass aClass = field.getContainingClass();
    PsiMethod[] constructors = aClass.getConstructors();
    PsiStatement assignment = createAssignment(local, field.getName(), factory);
    boolean added = false;
    for (PsiMethod constructor : constructors) {
      if (constructor == enclosingConstructor) {
        continue;
      }
      PsiCodeBlock body = constructor.getBody();
      if (body == null) {
        continue;
      }
      PsiStatement[] statements = body.getStatements();
      if (statements.length > 0) {
        PsiStatement first = statements[0];
        if (first instanceof PsiExpressionStatement) {
          PsiExpression expression = ((PsiExpressionStatement) first).getExpression();
          if (expression instanceof PsiMethodCallExpression) {
            @NonNls String text = ((PsiMethodCallExpression) expression).getMethodExpression().getText();
            if ("this".equals(text)) {
              continue;
            }
            if ("super".equals(text) && enclosingConstructor == null && PsiTreeUtil.isAncestor(constructor, local, false)) {
              local.delete();
              return (PsiStatement) body.addAfter(assignment, first);
            }
          }
        }
        if (enclosingConstructor == null && PsiTreeUtil.isAncestor(constructor, local, false)) {
          local.delete();
          return (PsiStatement) body.addBefore(assignment, first);
        }
      }

      assignment = (PsiStatement) body.add(assignment);
      added = true;
    }
    if (!added && enclosingConstructor == null) {
      if (aClass instanceof PsiAnonymousClass) {
        final PsiClassInitializer classInitializer = (PsiClassInitializer) aClass.addAfter(factory.createClassInitializer(), field);
        assignment = (PsiStatement) classInitializer.getBody().add(assignment);
      } else {
        PsiMethod constructor = (PsiMethod) aClass.add(GosuPsiParseUtil.createConstructor(aClass.getName(), aClass.getManager(), null));
        assignment = (PsiStatement) constructor.getBody().add(assignment);
      }
    }

    if (enclosingConstructor == null) {
      local.delete();
    }
    return assignment;
  }

  static class IntroduceFieldRunnable implements Runnable {
    private static final String LOCAL_VARIABLE_WITH_THIS_NAME_ALREADY_EXISTS = "Local variable with this name already exists";
    private final String myVariableName;
    private final String myFieldName;
    private final boolean myRebindNeeded;
    private final PsiLocalVariable myLocal;
    private final Project myProject;
    private final PsiClass myDestinationClass;
    private final GosuBaseExpressionToFieldHandler.Settings mySettings;
    private final GosuBaseExpressionToFieldHandler.InitializationPlace myInitializerPlace;
    private final PsiExpression[] myOccurences;
    private PsiField myField;
    private PsiStatement myAssignmentStatement;

    public IntroduceFieldRunnable(boolean rebindNeeded,
                                  PsiLocalVariable local,
                                  PsiClass aClass,
                                  GosuBaseExpressionToFieldHandler.Settings settings,
                                  boolean isStatic,
                                  PsiExpression[] occurrences) {
      myVariableName = local.getName();
      myFieldName = settings.getFieldName();
      myRebindNeeded = rebindNeeded;
      myLocal = local;
      myProject = local.getProject();
      myDestinationClass = aClass;
      mySettings = settings;
      myInitializerPlace = settings.getInitializerPlace();
      myOccurences = occurrences;
    }

    public void run() {
      try {
        final boolean rebindNeeded2 = !myVariableName.equals(myFieldName) || myRebindNeeded;
        final PsiReference[] refs;
        if (rebindNeeded2) {
          refs = ReferencesSearch.search(myLocal, GlobalSearchScope.projectScope(myProject), false).toArray(new PsiReference[0]);
        } else {
          refs = null;
        }

        final JavaPsiFacade facade = JavaPsiFacade.getInstance(myProject);
        PsiVariable psiVariable = facade.getResolveHelper().resolveAccessibleReferencedVariable(myFieldName, myLocal);
        if (psiVariable != null && (!psiVariable.equals(myLocal))) {
          CommonRefactoringUtil.showErrorMessage(
                  GosuIntroduceFieldHandler.REFACTORING_NAME,
                  LOCAL_VARIABLE_WITH_THIS_NAME_ALREADY_EXISTS,
                  HelpID.INTRODUCE_FIELD,
                  myProject
          );
          return;
        }


        if (refs != null) {

          for (PsiReference occur : refs) {
            psiVariable = facade.getResolveHelper().resolveAccessibleReferencedVariable(myFieldName, (PsiElement) occur);
            if (psiVariable != null && (psiVariable.getName().equals(myFieldName))) {
              CommonRefactoringUtil.showErrorMessage(
                      GosuIntroduceFieldHandler.REFACTORING_NAME,
                      LOCAL_VARIABLE_WITH_THIS_NAME_ALREADY_EXISTS,
                      HelpID.INTRODUCE_FIELD,
                      myProject
              );
              return;
            }
          }
        }


        final PsiMethod enclosingConstructor = GosuBaseExpressionToFieldHandler.getEnclosingConstructor(myDestinationClass, myLocal);
        myField = mySettings.isIntroduceEnumConstant() ? EnumConstantsUtil.createEnumConstant(myDestinationClass, myLocal, myFieldName)
                : createField(myLocal, mySettings.getForcedType(), myFieldName, myInitializerPlace == IN_FIELD_DECLARATION);
        myField = (PsiField) myDestinationClass.add(myField);
        GosuBaseExpressionToFieldHandler.setModifiers(myField, mySettings);
        if (!mySettings.isIntroduceEnumConstant()) {
          VisibilityUtil.fixVisibility(myOccurences, myField, mySettings.getFieldVisibility());
        }

        myLocal.normalizeDeclaration();
//        PsiDeclarationStatement declarationStatement = (PsiDeclarationStatement) myLocal.getParent();
        final GosuBaseExpressionToFieldHandler.InitializationPlace finalInitializerPlace;
        if (myLocal.getInitializer() == null) {
          finalInitializerPlace = IN_FIELD_DECLARATION;
        } else {
          finalInitializerPlace = myInitializerPlace;
        }
        final PsiElementFactory factory = JavaPsiFacade.getInstance(myProject).getElementFactory();

        switch (finalInitializerPlace) {
          case IN_FIELD_DECLARATION:
//            declarationStatement.delete();
            myLocal.delete();
            break;

          case IN_CURRENT_METHOD:
            PsiStatement statement = createAssignment(myLocal, myFieldName, null);
//            myAssignmentStatement = (PsiStatement) declarationStatement.replace(statement);
            myAssignmentStatement = (PsiStatement) myLocal.replace(statement);
            GeneratedMarkerVisitor.markGenerated(myAssignmentStatement);
            break;

          case IN_CONSTRUCTOR:
            myAssignmentStatement = addInitializationToConstructors(myLocal, myField, enclosingConstructor, factory);
            GeneratedMarkerVisitor.markGenerated(myAssignmentStatement);
            break;
          case IN_SETUP_METHOD:
//            myAssignmentStatement = addInitializationToSetUp(myLocal, myField, factory);
        }

        if (enclosingConstructor != null && myInitializerPlace == IN_CONSTRUCTOR) {
          PsiStatement statement = createAssignment(myLocal, myFieldName, null);
//          myAssignmentStatement = (PsiStatement) declarationStatement.replace(statement);
          myAssignmentStatement = (PsiStatement) myLocal.replace(statement);
          GeneratedMarkerVisitor.markGenerated(myAssignmentStatement);
        }

        if (rebindNeeded2) {
          for (final PsiReference reference : refs) {
            if (reference != null) {
              //expr = GosuRefactoringUtil.outermostParenthesizedExpression(expr);
              GosuRefactoringUtil.replaceOccurenceWithFieldRef((PsiExpression) reference, myField, myDestinationClass);
              //replaceOccurenceWithFieldRef((PsiExpression)reference, field, aaClass);
            }
          }
          //GosuRefactoringUtil.renameVariableReferences(local, pPrefix + fieldName, GlobalSearchScope.projectScope(myProject));
        }
        ClassLord.doImportAndStick(mySettings.getForcedType().getCanonicalText(), myDestinationClass.getContainingFile());
      } catch (IncorrectOperationException e) {
        LOG.error(e);
      }
    }

    public PsiField getField() {
      return myField;
    }

    public PsiStatement getAssignmentStatement() {
      return myAssignmentStatement;
    }
  }
}
