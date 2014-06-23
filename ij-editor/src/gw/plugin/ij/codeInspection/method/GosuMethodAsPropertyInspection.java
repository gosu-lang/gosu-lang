/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.codeInspection.method;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.ex.BaseLocalInspectionTool;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import gw.internal.gosu.parser.statements.FunctionStatement;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.exceptions.IWarningSuppressor;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.intentions.MethodAsPropertyFix;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierImpl;
import gw.plugin.ij.util.GosuBundle;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class GosuMethodAsPropertyInspection extends BaseLocalInspectionTool implements IWarningSuppressor {

  public static final String SUPPRESS_WARNING_CODE = "MethodAsProperty";

  @Nls
  @NotNull
  @Override
  public String getGroupDisplayName() {
    return GosuBundle.message("inspection.group.name.method.issues");
  }

  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return GosuBundle.message("inspection.method.as.property");
  }

  @NotNull
  @Override
  public String getShortName() {
    return "GosuMethodAsPropertyInspection";
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
      public void visitMethod(IGosuMethod method) {
        IModule module = GosuModuleUtil.findModuleForPsiElement(method);
        if (module == null) {
          module = TypeSystem.getGlobalModule();
        }
        TypeSystem.pushModule(module);
        try {
          IParsedElement parsedElement = method.getParsedElement();
          if( parsedElement == null ) {
            return;
          }
          if( parsedElement.isSuppressed( GosuMethodAsPropertyInspection.this ) ) {
            return;
          }
          if (parsedElement instanceof FunctionStatement) {
            PsiElement ident = getMethodIdentifier(method);
            FunctionStatement functionStatement = (FunctionStatement) parsedElement;
            String functionName = functionStatement.getFunctionName();
            if (functionName == null) {
              return;
            }
            int numPar = functionStatement.getParameters().size();
            IType returnType = functionStatement.getReturnType();

            boolean isFixable = false;
            boolean isGetter = false;
            if (functionName.startsWith("get") &&
                functionName.length() > 3 &&
                Character.isAlphabetic(functionName.charAt(3)) &&
                numPar == 0 &&
                returnType != JavaTypes.pVOID()) {
              isGetter = true;
              isFixable = true;
            } else if (functionName.startsWith("set") &&
                functionName.length() > 3 &&
                Character.isAlphabetic(functionName.charAt(3)) &&
                numPar == 1 &&
                returnType == JavaTypes.pVOID()) {
              isFixable = true;
            }
            if (isFixable) {
              holder.registerProblem(ident, GosuBundle.message("inspection.method.as.property"),
                  ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                  new PropertyFix(method, functionName, isGetter));
            }
          }
        } finally {
          TypeSystem.popModule(module);
        }
      }

      private PsiElement getMethodIdentifier(IGosuMethod method) {
        ASTNode ident = method.getNode().findChildByType(GosuElementTypes.TT_IDENTIFIER);
        if (ident instanceof GosuIdentifierImpl) {
          return (GosuIdentifierImpl) ident;
        }
        return method;
      }
    };
  }

  @Override
  public boolean isSuppressed( String warningCode ) {
    return SUPPRESS_WARNING_CODE.equals( warningCode ) || "all".equals( warningCode );
  }


  private class PropertyFix implements LocalQuickFix {
    private final MethodAsPropertyFix myQuickFix;

    public PropertyFix(IGosuMethod method, String functionName, boolean isGetter) {
      myQuickFix = new MethodAsPropertyFix(method, functionName, isGetter);
    }

    @NotNull
    public String getName() {
      return myQuickFix.getText();
    }

    @NotNull
    public String getFamilyName() {
      return GosuBundle.message("inspection.group.name.method.issues");
    }

    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
      PsiElement element = descriptor.getPsiElement();
      if (element == null) {
        return;
      }
      final PsiFile psiFile = element.getContainingFile();
      if (myQuickFix.isAvailable(project, null, psiFile)) {
        myQuickFix.invoke(project, null, psiFile);
      }
    }
  }

}
