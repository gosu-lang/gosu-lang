/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.plugin.ij.intentions;

import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInsight.daemon.QuickFixBundle;
import com.intellij.codeInsight.daemon.impl.quickfix.CreateClassFromNewFix;
import com.intellij.codeInsight.daemon.impl.quickfix.CreateFromUsageUtils;
import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateBuilderImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiExpressionStatement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.codeStyle.CodeEditUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuExpressionListImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuMethodCallExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuNewExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodBaseImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.GosuModuleUtil;
import gw.plugin.ij.util.JavaPsiFacadeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author mike
 */
public class CreateGosuClassFromNewFix extends CreateClassFromNewFix implements ITestableCreateClassFix {

  public CreateGosuClassFromNewFix(PsiNewExpression newExpression) {
    super(newExpression);
  }

  protected void invokeImpl(PsiClass targetClass) {
    assert ApplicationManager.getApplication().isWriteAccessAllowed();

    final PsiNewExpression newExpression = getNewExpression();

    final PsiJavaCodeReferenceElement referenceElement = getReferenceElement(newExpression);
    final Runnable runnable = new Runnable() {
      public void run() {
        final GosuTypeLiteralImpl typeLiteral = (GosuTypeLiteralImpl) referenceElement.getReferenceNameElement().getParent();
        final PsiClass psiClass = GosuCreateFromUsageUtils.createClass(typeLiteral, GosuCreateClassKind.GOSU_CLASS, getSuperClass(typeLiteral));
        new WriteCommandAction(newExpression.getProject()) {
          @Override
          protected void run(Result result) throws Throwable {
            typeLiteral.bindToElement(psiClass);
            setupClassFromNewExpression(psiClass, newExpression);
          }
        }.execute();
      }
    };
    if (ApplicationManager.getApplication().isUnitTestMode()) {
      runnable.run();
    } else {
      ApplicationManager.getApplication().invokeLater(runnable);
    }
  }

  public void invokeForTest(@NotNull String packageName) {
    assert ApplicationManager.getApplication().isWriteAccessAllowed();
    final PsiNewExpression newExpression = getNewExpression();
    final PsiPackage pkg = JavaPsiFacadeUtil.findPackage(newExpression.getProject(), packageName);
    final PsiDirectory dir = pkg.getDirectories()[0];
    final PsiJavaCodeReferenceElement referenceElement = getReferenceElement(newExpression);
    final GosuTypeLiteralImpl typeLiteral = (GosuTypeLiteralImpl) referenceElement.getReferenceNameElement().getParent();
    final PsiClass psiClass = GosuCreateFromUsageUtils.createClass(GosuCreateClassKind.GOSU_CLASS, dir, referenceElement.getReferenceName(), referenceElement.getManager(), referenceElement, referenceElement.getContainingFile(), getSuperClass(typeLiteral));
    new WriteCommandAction(newExpression.getProject()) {
      @Override
      protected void run(Result result) throws Throwable {
        typeLiteral.bindToElement(psiClass);
        setupClassFromNewExpression(psiClass, newExpression);
      }
    }.execute();
  }

  protected void setupClassFromNewExpression(final PsiClass psiClass, @NotNull final PsiNewExpression newExpression) {
    assert ApplicationManager.getApplication().isWriteAccessAllowed();

//    final PsiElementFactory elementFactory = JavaPsiFacade.getInstance(newExpression.getProject()).getElementFactory();
    PsiClass aClass = psiClass;
    if (aClass == null) return;

    final PsiJavaCodeReferenceElement classReference = newExpression.getClassReference();
    if (classReference != null) {
      classReference.bindToElement(aClass);
    }
//    setupInheritance(newExpression, aClass);

    PsiExpressionList argList = newExpression.getArgumentList();
    Project project = aClass.getProject();
    if (argList != null && argList.getExpressions().length > 0) {
      GosuMethodBaseImpl constructor = (GosuMethodBaseImpl) GosuPsiParseUtil.parseDeclaration(
          "construct() {\n}", aClass.getManager(), GosuModuleUtil.findModuleForPsiElement(aClass));
      CodeEditUtil.setOldIndentation(constructor.getNode(), 0); // this is to avoid a stupid exception
      constructor = (GosuMethodBaseImpl) aClass.add(constructor);

      TemplateBuilderImpl templateBuilder = new TemplateBuilderImpl(aClass);
      GosuCreateFromUsageUtils.setupMethodParameters(constructor, templateBuilder, argList, getTargetSubstitutor(newExpression));

//      setupSuperCall(aClass, constructor, templateBuilder);

//      getReferenceElement(newExpression).bindToElement(aClass);
      aClass = CodeInsightUtilBase.forcePsiPostprocessAndRestoreElement(aClass);
      Template template = templateBuilder.buildTemplate();
      template.setToReformat(true);

      PsiDocumentManager.getInstance(project).commitAllDocuments();
      Editor editor = positionCursor(project, aClass.getContainingFile(), aClass);
      TextRange textRange = aClass.getTextRange();
      editor.getDocument().deleteString(textRange.getStartOffset(), textRange.getEndOffset());

      startTemplate(editor, template, project);
    } else {
      positionCursor(project, aClass.getContainingFile(), aClass);
    }
  }

  @Nullable
  public static PsiMethod setupSuperCall(@NotNull PsiClass targetClass, @NotNull PsiMethod constructor, @NotNull TemplateBuilderImpl templateBuilder)
      throws IncorrectOperationException {
    PsiElementFactory elementFactory = JavaPsiFacade.getInstance(targetClass.getProject()).getElementFactory();
    PsiMethod supConstructor = null;
    PsiClass superClass = targetClass.getSuperClass();
    if (superClass != null && !"java.lang.Object".equals(superClass.getQualifiedName()) &&
        !"java.lang.Enum".equals(superClass.getQualifiedName())) {
      PsiMethod[] constructors = superClass.getConstructors();
      boolean hasDefaultConstructor = false;

      for (PsiMethod superConstructor : constructors) {
        if (superConstructor.getParameterList().getParametersCount() == 0) {
          hasDefaultConstructor = true;
          supConstructor = null;
          break;
        } else {
          supConstructor = superConstructor;
        }
      }

      if (!hasDefaultConstructor) {
        PsiExpressionStatement statement =
            (PsiExpressionStatement) elementFactory.createStatementFromText("super();", constructor);
        statement = (PsiExpressionStatement) constructor.getBody().add(statement);

        PsiMethodCallExpression call = (PsiMethodCallExpression) statement.getExpression();
        PsiExpressionList argumentList = call.getArgumentList();
        templateBuilder.setEndVariableAfter(argumentList.getFirstChild());
      }
    }

    templateBuilder.setEndVariableAfter(constructor.getBody().getLBrace());
    return supConstructor;
  }


  @Nullable
  private static PsiFile getTargetFile(PsiElement element) {
    PsiJavaCodeReferenceElement referenceElement = getReferenceElement((PsiNewExpression) element);

    PsiElement q = referenceElement.getQualifier();
    if (q instanceof PsiJavaCodeReferenceElement) {
      PsiJavaCodeReferenceElement qualifier = (PsiJavaCodeReferenceElement) q;
      PsiElement psiElement = qualifier.resolve();
      if (psiElement instanceof PsiClass) {
        PsiClass psiClass = (PsiClass) psiElement;
        return psiClass.getContainingFile();
      }
    }

    return null;
  }

  protected PsiElement getElement() {
    final PsiNewExpression expression = getNewExpression();
    if (expression == null || !expression.getManager().isInProject(expression)) return null;
    PsiJavaCodeReferenceElement referenceElement = getReferenceElement(expression);
    if (referenceElement == null) return null;
    if (referenceElement.getReferenceNameElement() instanceof PsiIdentifier) return expression;

    return null;
  }

  protected boolean isAllowOuterTargetClass() {
    return false;
  }

  protected boolean isValidElement(PsiElement element) {
    PsiJavaCodeReferenceElement ref = PsiTreeUtil.getChildOfType(element, PsiJavaCodeReferenceElement.class);
    return ref != null && ref.resolve() != null;
  }

  protected boolean isAvailableImpl(int offset) {
    PsiElement nameElement = getNameElement(getNewExpression());

    PsiFile targetFile = getTargetFile(getNewExpression());
    if (targetFile != null && !targetFile.getManager().isInProject(targetFile)) {
      return false;
    }

    if (CreateFromUsageUtils.shouldShowTag(offset, nameElement, getNewExpression())) {
      String varName = nameElement.getText();
      setText(getText(varName));
      return true;
    }

    return false;
  }

  @NotNull
  protected String getText(final String varName) {
    return "Create Gosu class '" + varName + "'";
//    return QuickFixBundle.message("create.class.from.new.text", varName);
  }

  @Nullable
  protected static PsiJavaCodeReferenceElement getReferenceElement(@NotNull PsiNewExpression expression) {
    return expression.getClassOrAnonymousClassReference();
  }

  @Nullable
  private static PsiElement getNameElement(@NotNull PsiNewExpression targetElement) {
    PsiJavaCodeReferenceElement referenceElement = getReferenceElement(targetElement);
    if (referenceElement == null) return null;
    return referenceElement.getReferenceNameElement();
  }

  @NotNull
  public String getFamilyName() {
    return QuickFixBundle.message("create.class.from.new.family");
  }

  @Nullable
  public static PsiType getSuperClass(@NotNull final GosuTypeLiteralImpl element) {
    PsiElement parent1 = element.getParent();
    PsiElement parent2 = parent1.getParent();
    PsiElement parent3 = parent2.getParent();

    if (parent1 instanceof GosuNewExpressionImpl &&
        parent2 instanceof GosuExpressionListImpl &&
        parent3 instanceof GosuMethodCallExpressionImpl) {
      int i = findChild(parent1, parent2);
      IMethodCallExpression parsedElement = ((GosuMethodCallExpressionImpl) parent3).getParsedElement();
      IFunctionType functionType = parsedElement.getFunctionType();
      if (functionType == null) {
        return null;
      }
      IType paramType = functionType.getParameterTypes()[i];
      return GosuBaseElementImpl.createType(paramType, element);
    }

    return null;
  }

  private static int findChild(PsiElement child, @NotNull PsiElement parent) {
    PsiElement[] children = parent.getChildren();
    for (int i = 0; i < children.length; i++) {
      if (children[i] == child) {
        return i;
      }
    }
    return -1;
  }
}
