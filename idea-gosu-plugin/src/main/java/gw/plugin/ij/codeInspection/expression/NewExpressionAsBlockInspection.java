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
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import gw.internal.gosu.parser.GosuClassTypeInfo;
import gw.lang.parser.exceptions.IWarningSuppressor;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.intentions.NewExpressionAsBlockFix;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.expressions.GosuNewExpressionImpl;
import gw.plugin.ij.util.GosuBundle;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class NewExpressionAsBlockInspection extends BaseLocalInspectionTool implements IWarningSuppressor {

  public static final String SUPPRESS_WARNING_CODE = "NewExprToBlock";

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
    return GosuBundle.message("inspection.expression.as.block");
  }

  @NotNull
  @Override
  public String getShortName() {
    return "NewExpressionAsBlockInspection";
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
      public void visitNewExpression(GosuNewExpressionImpl newExpression) {
        INewExpression parsedElement = newExpression.getParsedElement();
        VirtualFile virtualFile = holder.getFile().getVirtualFile();
        if (parsedElement != null && virtualFile != null && parsedElement.isAnonymousClass()) {
          if( parsedElement.isSuppressed( NewExpressionAsBlockInspection.this ) ) {
            return;
          }
          IModule module = GosuModuleUtil.findModuleForFile(virtualFile, holder.getProject());
          if (module == null) {
            return;
          }
          TypeSystem.pushModule(module);
          try {
            ITypeLiteralExpression typeLiteral = parsedElement.getTypeLiteral();
            IType literalType = typeLiteral.getType().getType();
            if (literalType.isInterface() && interfaceHasOneMethod(literalType)) {
              IFeatureInfo container = parsedElement.getConstructor().getContainer();
              if (container instanceof GosuClassTypeInfo) {
                List<? extends IMethodInfo> declaredMethods = ((GosuClassTypeInfo) container).getDeclaredMethods();
                List<? extends IPropertyInfo> declaredProperties = ((GosuClassTypeInfo) container).getDeclaredProperties();
                if (declaredMethods.size() == 1 && declaredProperties.size() == 1) {
                  holder.registerProblem(newExpression, GosuBundle.message("inspection.expression.as.block"),
                          ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                          new NewExpressionAsBlockInspectionFix(newExpression));
                }
              }
            }
          } finally {
            TypeSystem.popModule(module);
          }
        }
      }

      private boolean interfaceHasOneMethod(IType literalType) {
        List<IMethodInfo> methods = new LinkedList<>();
        IRelativeTypeInfo typeInfo = (IRelativeTypeInfo) literalType.getTypeInfo();
        methods.addAll(typeInfo.getDeclaredMethods());
        for (IType i : literalType.getInterfaces()) {
          typeInfo = (IRelativeTypeInfo) i.getTypeInfo();
          methods.addAll(typeInfo.getDeclaredMethods());
        }
        HashSet<String> objMethodSig = new HashSet<>();
        for (IMethodInfo info : JavaTypes.OBJECT().getTypeInfo().getMethods()) {
          objMethodSig.add(trim(info.getName()));
        }
        objMethodSig.add("@IntrinsicType()");
        Iterator<IMethodInfo> iter = methods.iterator();
        while (iter.hasNext()) {
          IMethodInfo info = iter.next();
          if (info.isStatic() || info.isDefaultImpl() || objMethodSig.contains(trim(info.getName()))) {
            iter.remove();
          }
        }
        return methods.size() == 1;
      }

      private String trim(String name) {
        return name.replace(" ", "");
      }
    };
  }

  @Override
  public boolean isSuppressed( String warningCode ) {
    return SUPPRESS_WARNING_CODE.equals( warningCode ) || "all".equals( warningCode );
  }

  private class NewExpressionAsBlockInspectionFix implements LocalQuickFix {
    private final NewExpressionAsBlockFix myQuickFix;

    public NewExpressionAsBlockInspectionFix(GosuNewExpressionImpl newExpr) {
      myQuickFix = new NewExpressionAsBlockFix(newExpr);
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

