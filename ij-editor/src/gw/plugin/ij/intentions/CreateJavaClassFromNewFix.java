/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.plugin.ij.intentions;

import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInsight.daemon.impl.quickfix.CreateClassFromNewFix;
import com.intellij.codeInsight.daemon.impl.quickfix.CreateClassKind;
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
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.PsiReferenceList;
import com.intellij.psi.PsiType;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import gw.plugin.ij.util.JavaPsiFacadeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author mike
 */
public class CreateJavaClassFromNewFix extends CreateClassFromNewFix implements ITestableCreateClassFix {

  public CreateJavaClassFromNewFix(PsiNewExpression newExpression) {
    super(newExpression);
  }

  @NotNull
  protected String getText(final String varName) {
    return "Create Java class '" + varName + "'";
//    return QuickFixBundle.message("create.class.from.new.text", varName);
  }

  protected void invokeImpl(PsiClass targetClass) {
    assert ApplicationManager.getApplication().isWriteAccessAllowed();

    final PsiNewExpression newExpression = getNewExpression();
    final PsiJavaCodeReferenceElement referenceElement = getReferenceElement(newExpression);
    final GosuTypeLiteralImpl typeLiteral = (GosuTypeLiteralImpl) newExpression.getChildren()[0];
    ApplicationManager.getApplication().invokeLater(new Runnable() {
      public void run() {
        final PsiClass psiClass = CreateFromUsageUtils.createClass(referenceElement, CreateClassKind.CLASS, null);
        new WriteCommandAction(newExpression.getProject()) {
          @Override
          protected void run(Result result) throws Throwable {
            setupClassFromNewExpression(psiClass, newExpression);
            typeLiteral.bindToElement(psiClass);
          }
        }.execute();
      }
    });
  }

  protected void setupClassFromNewExpression(final PsiClass psiClass, @NotNull final PsiNewExpression newExpression) {
    assert ApplicationManager.getApplication().isWriteAccessAllowed();

    final PsiElementFactory elementFactory = JavaPsiFacade.getInstance(newExpression.getProject()).getElementFactory();
    PsiClass aClass = psiClass;
    if (aClass == null) return;

    final PsiJavaCodeReferenceElement classReference = newExpression.getClassReference();
    if (classReference != null) {
      classReference.bindToElement(aClass);
    }
    setupInheritance(newExpression, aClass);

    PsiExpressionList argList = newExpression.getArgumentList();
    Project project = aClass.getProject();
    if (argList != null && argList.getExpressions().length > 0) {
      PsiMethod constructor = elementFactory.createConstructor();
      constructor = (PsiMethod) aClass.add(constructor);

      TemplateBuilderImpl templateBuilder = new TemplateBuilderImpl(aClass);
      CreateFromUsageUtils.setupMethodParameters(constructor, templateBuilder, argList, getTargetSubstitutor(newExpression));

      setupSuperCall(aClass, constructor, templateBuilder);

      getReferenceElement(newExpression).bindToElement(aClass);
      aClass = CodeInsightUtilBase.forcePsiPostprocessAndRestoreElement(aClass);
      Template template = templateBuilder.buildTemplate();
      template.setToReformat(true);

      Editor editor = positionCursor(project, aClass.getContainingFile(), aClass);
      TextRange textRange = aClass.getTextRange();
      editor.getDocument().deleteString(textRange.getStartOffset(), textRange.getEndOffset());

      startTemplate(editor, template, project);
    } else {
      positionCursor(project, aClass.getContainingFile(), aClass);
    }
  }

  private static void setupInheritance(@NotNull PsiNewExpression newExpression, @NotNull PsiClass targetClass) throws IncorrectOperationException {
    if (newExpression.getParent() instanceof PsiReferenceExpression) return;

    final GosuTypeLiteralImpl typeLiteral = (GosuTypeLiteralImpl) newExpression.getChildren()[0];
    PsiType type = CreateGosuClassFromNewFix.getSuperClass(typeLiteral);

    if (!(type instanceof PsiClassType)) return;
    final PsiClassType classType = (PsiClassType) type;
    PsiClass aClass = classType.resolve();
    if (aClass == null) return;
    if (aClass.equals(targetClass) || aClass.hasModifierProperty(PsiModifier.FINAL)) return;
    PsiElementFactory factory = JavaPsiFacade.getInstance(aClass.getProject()).getElementFactory();

    if (aClass.isInterface()) {
      PsiReferenceList implementsList = targetClass.getImplementsList();
      implementsList.add(factory.createReferenceElementByType(classType));
    } else {
      PsiReferenceList extendsList = targetClass.getExtendsList();
      if (extendsList.getReferencedTypes().length == 0 && !"java.lang.Object".equals(classType.getCanonicalText())) {
        extendsList.add(factory.createReferenceElementByType(classType));
      }
    }
  }

  public void invokeForTest(@NotNull String packageName) {
    assert ApplicationManager.getApplication().isWriteAccessAllowed();
    final PsiNewExpression newExpression = getNewExpression();
    final PsiPackage pkg = JavaPsiFacadeUtil.findPackage(newExpression.getProject(), packageName);
    final PsiDirectory dir = pkg.getDirectories()[0];
    final PsiJavaCodeReferenceElement referenceElement = getReferenceElement(newExpression);
    final GosuTypeLiteralImpl typeLiteral = (GosuTypeLiteralImpl) newExpression.getChildren()[0];
    final PsiClass psiClass = CreateFromUsageUtils.createClass(CreateClassKind.CLASS, dir, referenceElement.getReferenceName(), referenceElement.getManager(), referenceElement, referenceElement.getContainingFile(), null);
    new WriteCommandAction(newExpression.getProject()) {
      @Override
      protected void run(Result result) throws Throwable {
        setupClassFromNewExpression(psiClass, newExpression);
        typeLiteral.bindToElement(psiClass);
      }
    }.execute();
  }

}
