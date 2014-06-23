/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.codeInspection.expression;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.ex.BaseLocalInspectionTool;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import gw.lang.parser.IExpression;
import gw.lang.parser.exceptions.IWarningSuppressor;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;
import gw.plugin.ij.intentions.ObjectEqualsAsOpFix;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.expressions.GosuBeanMethodCallExpressionImpl;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class GosuObjectEqualsInspection extends BaseLocalInspectionTool implements IWarningSuppressor {

  public static final String SUPPRESS_WARNING_CODE = "EqualsMethodToOperator";

  @Nls
  @NotNull
  @Override
  public String getGroupDisplayName() {
    return GosuBundle.message("inspection.group.name.expression.issues");
  }

  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return GosuBundle.message("inspection.object.equals.as.op");
  }

  @NotNull
  @Override
  public String getShortName() {
    return "GosuObjectEqualsInspection";
  }


  @Override
  public boolean isEnabledByDefault() {
    return true;
  }


  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly, @NotNull LocalInspectionToolSession session) {
    return new GosuElementVisitor() {

      @Override
      public void visitBeanMethodCallExpression(GosuBeanMethodCallExpressionImpl callExpression) {
        IBeanMethodCallExpression parsedElement = callExpression.getParsedElement();
        if(parsedElement == null) {
          return;
        }
        if( parsedElement.isSuppressed( GosuObjectEqualsInspection.this ) ) {
          return;
        }
        IFunctionType functionType = parsedElement.getFunctionType();
        if(functionType == null) {
          return;
        }
        IType[] parameterTypes = functionType.getParameterTypes();
        String displayName = functionType.getDisplayName();
        if(displayName != null && parameterTypes != null &&
           displayName.equals("equals") &&
           functionType.getReturnType() == JavaTypes.pBOOLEAN() &&
           parameterTypes.length == 1 &&
           parameterTypes[0] == JavaTypes.OBJECT())
        {
          IExpression[] args = parsedElement.getArgs();
          IType rootType = parsedElement.getRootType();
          if(args != null && args.length == 1 && args[0].getType() == rootType) {
            holder.registerProblem(callExpression, GosuBundle.message("inspection.object.equals.as.op"),
                          ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                          new GosuObjectEqualsInspectionFix(callExpression));
          }
        }
      }
    };
  }

  @Override
  public boolean isSuppressed( String warningCode ) {
    return SUPPRESS_WARNING_CODE.equals( warningCode ) || "all".equals( warningCode );
  }


  private class GosuObjectEqualsInspectionFix implements LocalQuickFix {
    private final ObjectEqualsAsOpFix myQuickFix;

    public GosuObjectEqualsInspectionFix(GosuBeanMethodCallExpressionImpl callExpression) {
      myQuickFix = new ObjectEqualsAsOpFix(callExpression);
    }

    @NotNull
    public String getName() {
      return myQuickFix.getText();
    }

    @NotNull
    public String getFamilyName() {
      return GosuBundle.message("inspection.group.name.expression.issues");
    }

    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
      PsiElement element = descriptor.getPsiElement();
      if (element == null) return;
      final PsiFile psiFile = element.getContainingFile();
      if (myQuickFix.isAvailable(project, null, psiFile)) {
        myQuickFix.invoke(project, null, psiFile);
      }
    }
  }

}
